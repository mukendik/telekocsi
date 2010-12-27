package com.alma.telekocsi.dao.profil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.os.AsyncTask;
import android.util.Log;

import com.alma.telekocsi.dao.EndPoint;
import com.alma.telekocsi.dao.IAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProfilConsumer {
	
	private static final DefaultHttpClient httpClient = new DefaultHttpClient();
	
	static {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		httpClient.setParams(params); 
	}
	
	private static final Gson gson = new Gson();

	private static final String JSON_CONTENT_TYPE 	= "application/json; charset=UTF-8";
	private static final String ENCODING_UTF_8 		= "UTF-8";

	private Object lock = new Object();
	private IAdapter<Profil> adapter;
	
	public ProfilConsumer(IAdapter<Profil> adapter) {
		this.adapter = adapter;
	}
	
	
	/**
	 * Classe abstraite pour l'envoi de requetes asynchrones au serveur.
	 */
	private abstract class AbstractTask 
			extends AsyncTask<HttpRequestBase, Void, HttpResponse> {
		
		/**
		 * Appele avant le lancement du traitement en arriere plan.
		 * Cette methode est execute dans le Thread appelant.
		 */
		protected void onPreExecute() {
			adapter.showDialog(IAdapter.DIALOG_LOADING);
		}
		
		/**
		 * Traitement en arriere plan.
		 * Cette methode est execute dans un Thread different
		 * du Thread appelant.
		 */
		protected HttpResponse doInBackground(final HttpRequestBase...requests) {
			HttpResponse response = null;
			synchronized (lock) {
				try {
					response = httpClient.execute(requests[0]);
				} catch (Exception e) {
					Log.e(ProfilConsumer.class.getSimpleName(), 
							"Erreur d'appel au serveur", e);
				}
			}
			return response;
	    }

		/**
		 * Appele apres la fin du traitement en arriere plan.
		 */
		protected void onPostExecute(final HttpResponse response) {
			adapter.dismissDialog(IAdapter.DIALOG_LOADING);
			if (response == null 
					|| !(response.getStatusLine()
							.getStatusCode() == HttpStatus.SC_OK)) {
				adapter.showDialog(IAdapter.DIALOG_ERROR);
			} else {
				try {
					handleJson(response.getEntity().getContent());
				} catch (IOException e) {
					Log.e(ProfilConsumer.class.getSimpleName(), 
							"Erreur de flux", e);
				}
			}
		}
		
		/**
		 * Traitement specifique du JSON
		 * @param in Le contenu de la reponse HTTP OK
		 */
		protected abstract void handleJson(final InputStream in);

	};
	
	/**
	 * Recuperation de la liste des Profils
	 */
	private class GetListTask extends AbstractTask {

		protected void handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Profil>>(){}.getType();
			List<Profil> profils = null;
			synchronized (lock) {
				profils = gson.fromJson(new InputStreamReader(in), collectionType);
			}
			// La liste recuperee initialement est la reference
			// des Profils pour l'application Android
			adapter.setList(profils);
		}
	};
	
	/**
	 * Recuperation d'un profil
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask {

		protected void handleJson(final InputStream in) {
			Profil profil = null;
			synchronized (lock) {
				profil = gson.fromJson(new InputStreamReader(in), Profil.class);
			}
			adapter.setFiche(profil);
		}
	};

	/**
	 * Recuperation d'un profil
	 * deja reference localement
	 */
	private class LoginTask extends AbstractTask {

		protected void handleJson(final InputStream in) {
			Profil profil = null;
			synchronized (lock) {
				profil = gson.fromJson(new InputStreamReader(in), Profil.class);
			}
			adapter.login(profil);
		}
	};	
	
	/**
	 * Recuperation d'un nouveau profil
	 * non encore reference localement
	 */
	private class AddTask extends AbstractTask {

		protected void handleJson(final InputStream in) {
			Profil profil = null;
			synchronized (lock) {
				profil = gson.fromJson(new InputStreamReader(in), Profil.class);
			}
			adapter.add(profil);
		}

	};
	
	/**
	 * Suppression d'un profil
	 * reference localement
	 */
	private class DeleteTask extends AbstractTask {

		protected void handleJson(final InputStream in) {
			Profil fakeProfil = new Profil();
			try {
				fakeProfil.setId(new BufferedReader(
						new InputStreamReader(in, ENCODING_UTF_8)).readLine());
			} catch (Exception e) {}
			adapter.remove(fakeProfil);
		}
	};

	/**
	 * Suppression de tous les profils
	 * reference localement
	 */
	private class ClearTask extends AbstractTask {

		protected void handleJson(final InputStream in) {
			String nb = "";
			try {
				nb = new BufferedReader(new InputStreamReader(in, ENCODING_UTF_8)).readLine();
			} catch (Exception e) {}
			adapter.clear(nb);
		}
	};	
	
    /**
     * Creation d'un profil
     * @param Profil
     */
    public void insert(Profil profil) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(EndPoint.getProfilEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", JSON_CONTENT_TYPE);
    	synchronized (lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					gson.toJson(profil), ENCODING_UTF_8));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	(new AddTask()).execute(request);
    }

    public void update(Profil profil) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Profil a mettre a jour
    	HttpPost request = new HttpPost(
    			EndPoint.getProfilEndPoint() + "/" + profil.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", JSON_CONTENT_TYPE);
    	synchronized (lock) {
    		try {
    			// l'objet de type Profil serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					gson.toJson(profil), ENCODING_UTF_8));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	(new GetFicheTask()).execute(request);
    }
    
    /**
     * Action Supprimer
     * @param Profil
     */
    public void delete(Profil profil) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Profil a supprimer
		(new DeleteTask()).execute(new HttpDelete(
				EndPoint.getProfilEndPoint() + "/" + profil.getId()));
    }

    
    /**
     * Action Supprimer
     * @param Profil
     */
    public void clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Profils
		(new ClearTask()).execute(new HttpDelete(
				EndPoint.getProfilEndPoint() + "/clear"));
    }    
    
    
    public void getList() {
    	// recuperation de tous les profils
		(new GetListTask()).execute(new HttpGet(EndPoint.getProfilEndPoint()));
    }
    
    
    public void login(String pseudo, String passWord) {
    	// Recuperation du profil lie au pseudo + password
		(new LoginTask()).execute(new HttpGet(EndPoint.getProfilEndPoint() + "/login/" + pseudo + "/" + passWord));
    }
}
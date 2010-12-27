package com.alma.telekocsi.dao.trajet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import com.alma.telekocsi.dao.AbstractTask;
import com.alma.telekocsi.dao.GAE;
import com.google.gson.reflect.TypeToken;


public class TrajetDAO {
	
	
	public TrajetDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste de Trajets
	 */
	private class GetListTask extends AbstractTask<List<Trajet>>  {

		protected List<Trajet> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Trajet>>(){}.getType();
			List<Trajet> trajets = null;
			synchronized (lock) {
				trajets = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return trajets;
		}
	};
	
	
	/**
	 * Recuperation d'un trajet
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<Trajet> {

		protected Trajet handleJson(final InputStream in) {
			Trajet trajet = null;
			synchronized (lock) {
				trajet = GAE.getGson().fromJson(new InputStreamReader(in), Trajet.class);
			}
			return trajet;
		}
	};
	
	
	/**
	 * Suppression d'un trajet
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idTrajet = "";
			try {
				idTrajet =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idTrajet;
		}
	};

	
	/**
	 * Suppression de tous les trajets
	 * reference localement
	 */
	private class GetNbFicheTask extends AbstractTask<Integer> {

		protected Integer handleJson(final InputStream in) {
			Integer nb = 0;
			try {
				String res = new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
				nb = Integer.parseInt(res);
			} catch (Exception e) {}
			
			return nb;
		}
	};
	
	
    /**
     * Creation d'un trajet
     * @param Trajet
     */
    public Trajet insert(Trajet trajet) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getTrajetEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(trajet), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    public Trajet update(Trajet trajet) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Trajet a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getTrajetEndPoint() + "/" + trajet.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Trajet serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(trajet), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param Trajet
     */
    public String delete(Trajet trajet) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Trajet a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getTrajetEndPoint() + "/" + trajet.getId()));
    }

    
    /**
     * Action Supprimer
     * @param Trajet
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Trajets
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getTrajetEndPoint() + "/clear"));
    }    
    
    
    public List<Trajet> getList(String idProfil) {
    	// recuperation de tous les trajets
		return (new GetListTask()).execute(new HttpGet(GAE.getTrajetEndPoint() + "/profil/" + idProfil));
    }
    
}
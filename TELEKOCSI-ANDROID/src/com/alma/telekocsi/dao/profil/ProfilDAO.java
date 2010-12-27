package com.alma.telekocsi.dao.profil;

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


public class ProfilDAO {
	
	
	public ProfilDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste de Profils
	 */
	private class GetListTask extends AbstractTask<List<Profil>>  {

		protected List<Profil> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Profil>>(){}.getType();
			List<Profil> profils = null;
			synchronized (lock) {
				profils = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return profils;
		}
	};
	
	
	/**
	 * Recuperation d'un profil
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<Profil> {

		protected Profil handleJson(final InputStream in) {
			Profil profil = null;
			synchronized (lock) {
				profil = GAE.getGson().fromJson(new InputStreamReader(in), Profil.class);
			}
			return profil;
		}
	};
	
	
	/**
	 * Suppression d'un profil
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idProfil = "";
			try {
				idProfil =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idProfil;
		}
	};

	
	/**
	 * Suppression de tous les profils
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
     * Creation d'un profil
     * @param Profil
     */
    public Profil insert(Profil profil) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getProfilEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(profil), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    public Profil update(Profil profil) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Profil a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getProfilEndPoint() + "/" + profil.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Profil serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(profil), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param Profil
     */
    public String delete(Profil profil) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Profil a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getProfilEndPoint() + "/" + profil.getId()));
    }

    
    /**
     * Action Supprimer
     * @param Profil
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Profils
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getProfilEndPoint() + "/clear"));
    }    
    
    
    public List<Profil> getList() {
    	// recuperation de tous les profils
		return (new GetListTask()).execute(new HttpGet(GAE.getProfilEndPoint()));
    }
    
    
    public Profil login(String pseudo, String passWord) {
    	// Recuperation du profil lie au pseudo + password
		return (new GetFicheTask()).execute(new HttpGet(GAE.getProfilEndPoint() + "/login/" + pseudo + "/" + passWord));
    }
}
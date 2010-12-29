package com.alma.telekocsi.dao.localisation;

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


public class LocalisationDAO {
	
	
	public LocalisationDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste de Localisations
	 */
	private class GetListTask extends AbstractTask<List<Localisation>>  {

		protected List<Localisation> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Localisation>>(){}.getType();
			List<Localisation> localisation = null;
			synchronized (lock) {
				localisation = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return localisation;
		}
	};
	
	
	/**
	 * Recuperation d'une localisation
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<Localisation> {

		protected Localisation handleJson(final InputStream in) {
			Localisation localisation = null;
			synchronized (lock) {
				localisation = GAE.getGson().fromJson(new InputStreamReader(in), Localisation.class);
			}
			return localisation;
		}
	};
	
	
	/**
	 * Suppression d'une localisation
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idLocalisation = "";
			try {
				idLocalisation =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idLocalisation;
		}
	};

	
	/**
	 * Suppression de toutes les localisations
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
     * Creation d'une localisation
     * @param Localisation a inserer
     * @return Localisation inseree dans la BDD
     */
    public Localisation insert(Localisation localisation) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getLocalisationEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(localisation), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    /**
     * Modification de la localisation dans la BDD
     * @param localisation a reporter dans la BDD
     * @return localisation memorisee dans la BDD
     */
    public Localisation update(Localisation localisation) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Localisation a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getLocalisationEndPoint() + "/" + localisation.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Localisation serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(localisation), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param Localisation
     * @return id de la localisation supprimee
     */
    public String delete(Localisation localisation) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Localisation a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getLocalisationEndPoint() + "/" + localisation.getId()));
    }

    /**
     * Suppression de toutes les localisations d'un profil
     * @param idProfil
     */
    public Integer clear(String idProfil) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Localisations
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getLocalisationEndPoint() + "/clear/"  + idProfil));
    } 
    
    
    /**
     * Suppression de toutes les localisations
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Localisations
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getLocalisationEndPoint() + "/clear"));
    }
    
    
    /**
     * Recuperation d'une localisation a partir de son id
     * @param idLocalisation
     * @return Localisation
     */
    public Localisation getLocalisation(String idLocalisation) {
    	
		return (new GetFicheTask()).execute(new HttpGet(GAE.getLocalisationEndPoint() + "/" + idLocalisation));
    }     
    
    
    /**
     * Recupere les localisations liees a un profil
     * Normalement, une seule
     * 
     * @param idProfil
     * @return liste des localisations
     */
    public List<Localisation> getList(String idProfil) {
    	// recuperation des localisations liees a un profil
		return (new GetListTask()).execute(new HttpGet(GAE.getLocalisationEndPoint() + "/profil/" + idProfil));
    }
}
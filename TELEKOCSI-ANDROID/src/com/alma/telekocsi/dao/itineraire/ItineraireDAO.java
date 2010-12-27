package com.alma.telekocsi.dao.itineraire;

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


public class ItineraireDAO {
	
	
	public ItineraireDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste de Itineraires
	 */
	private class GetListTask extends AbstractTask<List<Itineraire>>  {

		protected List<Itineraire> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Itineraire>>(){}.getType();
			List<Itineraire> itineraires = null;
			synchronized (lock) {
				itineraires = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return itineraires;
		}
	};
	
	
	/**
	 * Recuperation d'un itineraire
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<Itineraire> {

		protected Itineraire handleJson(final InputStream in) {
			Itineraire itineraire = null;
			synchronized (lock) {
				itineraire = GAE.getGson().fromJson(new InputStreamReader(in), Itineraire.class);
			}
			return itineraire;
		}
	};
	
	
	/**
	 * Suppression d'un itineraire
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idItineraire = "";
			try {
				idItineraire =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idItineraire;
		}
	};

	
	/**
	 * Suppression de tous les itineraires
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
     * Creation d'un itineraire
     * @param Itineraire
     */
    public Itineraire insert(Itineraire itineraire) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getItineraireEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(itineraire), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    public Itineraire update(Itineraire itineraire) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Itineraire a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getItineraireEndPoint() + "/" + itineraire.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Itineraire serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(itineraire), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param Itineraire
     */
    public String delete(Itineraire itineraire) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Itineraire a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getItineraireEndPoint() + "/" + itineraire.getId()));
    }

    
    /**
     * Action Supprimer
     * @param Itineraire
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Itineraires
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getItineraireEndPoint() + "/clear"));
    }    
    
    
    public List<Itineraire> getList(String idProfil) {
    	// recuperation de tous les itineraires
		return (new GetListTask()).execute(new HttpGet(GAE.getItineraireEndPoint() + "/profil/" + idProfil));
    }
    
}
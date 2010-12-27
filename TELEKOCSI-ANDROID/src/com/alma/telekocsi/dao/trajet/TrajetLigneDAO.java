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


public class TrajetLigneDAO {
	
	
	public TrajetLigneDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste de TrajetLignes
	 */
	private class GetListTask extends AbstractTask<List<TrajetLigne>>  {

		protected List<TrajetLigne> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<TrajetLigne>>(){}.getType();
			List<TrajetLigne> trajetLignes = null;
			synchronized (lock) {
				trajetLignes = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return trajetLignes;
		}
	};
	
	
	/**
	 * Recuperation d'un trajetLigne
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<TrajetLigne> {

		protected TrajetLigne handleJson(final InputStream in) {
			TrajetLigne trajetLigne = null;
			synchronized (lock) {
				trajetLigne = GAE.getGson().fromJson(new InputStreamReader(in), TrajetLigne.class);
			}
			return trajetLigne;
		}
	};
	
	
	/**
	 * Suppression d'un trajetLigne
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idTrajetLigne = "";
			try {
				idTrajetLigne =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idTrajetLigne;
		}
	};

	
	/**
	 * Suppression de tous les trajetLignes
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
     * Creation d'un trajetLigne
     * @param TrajetLigne
     */
    public TrajetLigne insert(TrajetLigne trajetLigne) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getTrajetLigneEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(trajetLigne), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    public TrajetLigne update(TrajetLigne trajetLigne) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du TrajetLigne a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getTrajetLigneEndPoint() + "/" + trajetLigne.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type TrajetLigne serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(trajetLigne), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param TrajetLigne
     */
    public String delete(TrajetLigne trajetLigne) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au TrajetLigne a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getTrajetLigneEndPoint() + "/" + trajetLigne.getId()));
    }

    
    /**
     * Action Supprimer
     * @param TrajetLigne
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les TrajetLignes
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getTrajetLigneEndPoint() + "/clear"));
    }    
    
    
    public List<TrajetLigne> getList(String idTrajet) {
    	// recuperation de tous les trajetLignes
		return (new GetListTask()).execute(new HttpGet(GAE.getTrajetLigneEndPoint() + "/trajet/" + idTrajet));
    }
    
}
package com.alma.telekocsi.dao.avis;

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


public class AvisDAO {
	
	
	public AvisDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste d'Avis
	 */
	private class GetListTask extends AbstractTask<List<Avis>>  {

		protected List<Avis> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Avis>>(){}.getType();
			List<Avis> avis = null;
			synchronized (lock) {
				avis = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return avis;
		}
	};
	
	
	/**
	 * Recuperation d'un avis
	 */
	private class GetFicheTask extends AbstractTask<Avis> {

		protected Avis handleJson(final InputStream in) {
			Avis avis = null;
			synchronized (lock) {
				avis = GAE.getGson().fromJson(new InputStreamReader(in), Avis.class);
			}
			return avis;
		}
	};
	
	
	/**
	 * Recupere l'id de l'avis traite 
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idAvis = "";
			try {
				idAvis =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idAvis;
		}
	};

	
	/**
	 * Retourne le nbre d'avis traites
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
     * Creation d'un avis dans la BDD
     * @param Avis a inserer
     * @return avis insere dans la BDD
     */
    public Avis insert(Avis avis) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getAvisEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(avis), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    /**
     * Modification d'un avis dans la BDD
     * @param avis a modifier
     * @return avis modifie recupere de la BDD
     */
    public Avis update(Avis avis) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Avis a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getAvisEndPoint() + "/" + avis.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Avis serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(avis), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Suppressino d'un avis en BDD
     * @param Avis a supprimer
     * @return id de l'avis supprime
     */
    public String delete(Avis avis) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Avis a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getAvisEndPoint() + "/" + avis.getId()));
    }

    
    /**
     * Suppression de tous les avis de la BDD
     * @return nombre d'avis supprimes
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Aviss
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getAvisEndPoint() + "/clear"));
    }    
    

    /**
     * Recuperation d'un avis a partir de son id
     * @param idAvis
     * @return Avis
     */
    public Avis getAvis(String idAvis) {
    	
		return (new GetFicheTask()).execute(new HttpGet(GAE.getAvisEndPoint() + "/" + idAvis));
    }


    /**
     * Liste des avis deposes par un profil
     * @param idProfil id du profil ayant depose les avis 
     * @return liste des avis
     */
    public List<Avis> getListFrom(String idProfil) {
		return (new GetListTask()).execute(new HttpGet(GAE.getAvisEndPoint() + "/profil/from/" + idProfil));
    }
    
    
    /**
     * Recupere une liste d'avis a destination d'un profil
     * @param idProfil profils concerne par les avis
     * @return liste des avis
     */
    public List<Avis> getListTo(String idProfil) {
    	// recuperation des avis to
		return (new GetListTask()).execute(new HttpGet(GAE.getAvisEndPoint() + "/profil/to/" + idProfil));
    }    
}
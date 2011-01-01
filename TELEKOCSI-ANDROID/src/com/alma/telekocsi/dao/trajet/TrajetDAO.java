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
	 * Pour la recuperation d'une liste de Trajets
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
	 * Pour la recuperation d'un trajet
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
	 * Pour la Suppression d'un trajet
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
	 * Pour connaitre le nombre de trajets traites
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
     * Creation d'un trajet dans la BDD
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

    
    /**
     * Modification du trajet dans la BDD
     * @param trajet a modifier dans la BDD
     * @return trajet provenant de la BDD apres modif
     */
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
     * Suppression d'un trajet dans la BDD
     * @param Trajet a supprimer
     * @return id du trajet supprime
     */
    public String delete(Trajet trajet) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Trajet a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getTrajetEndPoint() + "/" + trajet.getId()));
    }

    
    /**
     * Suppression de tous les trajets dans la BDD
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Trajets
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getTrajetEndPoint() + "/clear"));
    }
    
    
    /**
     * Recuperation d'un trajet a partir de son id
     * @param idTrajet
     * @return trajet
     */
    public Trajet getTrajet(String idTrajet) {
    	
		return (new GetFicheTask()).execute(new HttpGet(GAE.getTrajetEndPoint() + "/" + idTrajet));
    }
    
    
    /**
     * Recupere la liste des trajets associes a un profil
     * @param idProfil
     * @return
     */
    public List<Trajet> getList(String idProfil) {

    	return (new GetListTask()).execute(new HttpGet(GAE.getTrajetEndPoint() + "/profil/" + idProfil));
    }
    
    
    /**
     * Recupere la liste des trajets disponibles
     * @param Trajet de reference (lieu depart, lieu arrivee et date)
     * 
     * @return liste des trajets disponibles
     */
    public List<Trajet> getTrajetDispo(Trajet trajetModel) {
    	
    	HttpPut request = new HttpPut(GAE.getTrajetEndPoint() + "/trajetDispo");
    	request.setHeader("Content-Type", GAE.getContentType());
    	
    	synchronized (AbstractTask.lock) {
    		try {
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(trajetModel), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetListTask()).execute(request);
    }    
    
    
    /**
     * Generation automatique des trajets habituels des conducteurs pour une date
     * @param date au format dd/MM/yyyy
     * @return nb de trajets generes
     */
    public int generate(String date) {

    	String dateRef = date.replaceAll("/", "-"); // Non supporte sinon change le chemin http !
		return (new GetNbFicheTask()).execute(new HttpGet(GAE.getTrajetEndPoint() + "/generate/" + dateRef));
    }
    
}
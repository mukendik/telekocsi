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
	 * Pour la recuperation d'une liste de Profils
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
	 * Pour la recuperation d'un profil
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
	 * Pour la suppression d'un profil
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
	 * Pour la suppression de plusieurs profils
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
     * Creation d'un profil dans la BDD
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

    
    /**
     * Actualisation du profil dans la BDD
     * @param profil
     * @return profil actualise provenant de la BDD
     */
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
     * Suppression d'un profil
     * @param Profil
     */
    public String delete(Profil profil) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Profil a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getProfilEndPoint() + "/" + profil.getId()));
    }

    
    /**
     * Suppression de tous les profils de la base
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Profils
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getProfilEndPoint() + "/clear"));
    }    
    
    
    /**
     * Recuperation de tous les profils existants
     * @return liste des profils
     */
    public List<Profil> getList() {

    	return (new GetListTask()).execute(new HttpGet(GAE.getProfilEndPoint()));
    }

    
    /**
     * Recuperation d'un profil a partir de son id
     * @param idProfil
     * @return
     */
    public Profil getProfil(String idProfil) {
    	
		return (new GetFicheTask()).execute(new HttpGet(GAE.getProfilEndPoint() + "/" + idProfil));
    }
    
    
    /**
     * Recuperation du profil lie au pseudo + password
     * @param pseudo pseudo
     * @param passWord mot de passe
     * @return profil associe
     */
    public Profil login(String pseudo, String passWord) {
    	
		return (new GetFicheTask()).execute(new HttpGet(GAE.getProfilEndPoint() + "/login/" + pseudo + "/" + passWord));
    }
}
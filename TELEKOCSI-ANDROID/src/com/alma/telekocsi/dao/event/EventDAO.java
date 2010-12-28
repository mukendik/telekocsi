package com.alma.telekocsi.dao.event;

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

public class EventDAO {
	
	public EventDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste d'Events
	 */
	private class GetListTask extends AbstractTask<List<Event>>  {

		protected List<Event> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Event>>(){}.getType();
			List<Event> event = null;
			synchronized (lock) {
				event = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return event;
		}
	};
	
	
	/**
	 * Recuperation d'un event
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<Event> {

		protected Event handleJson(final InputStream in) {
			Event event = null;
			synchronized (lock) {
				event = GAE.getGson().fromJson(new InputStreamReader(in), Event.class);
			}
			return event;
		}
	};
	
	
	/**
	 * Suppression d'un event
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idEvent = "";
			try {
				idEvent =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idEvent;
		}
	};

	
	/**
	 * Suppression de tous les event
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
     * Creation d'un event
     * @param Event
     */
    public Event insert(Event event) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getEventEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(event), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    public Event update(Event event) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Event a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getEventEndPoint() + "/" + event.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Event serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(event), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param Event
     */
    public String delete(Event event) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Event a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getEventEndPoint() + "/" + event.getId()));
    }

    
    /**
     * Action Supprimer
     * @param Event
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Events
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getEventEndPoint() + "/clear"));
    }    
    
    
    public List<Event> getListFrom(String idProfil) {
    	// recuperation des event from
		return (new GetListTask()).execute(new HttpGet(GAE.getEventEndPoint() + "/profil/from/" + idProfil));
    }

    
    public List<Event> getListTo(String idProfil) {
    	// recuperation des event to
		return (new GetListTask()).execute(new HttpGet(GAE.getEventEndPoint() + "/profil/to/" + idProfil));
    }    
}
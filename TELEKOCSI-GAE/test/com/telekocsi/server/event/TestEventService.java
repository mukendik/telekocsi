package com.telekocsi.server.event;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.profil.Profil;
import com.telekocsi.server.profil.TestProfilService;
import com.telekocsi.server.util.Tools;

public class TestEventService {

	private static final String URL = Tools.getURL("/event");
	
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private Event event;
	private TestProfilService testProfilService;
	
	public TestEventService() {

		testProfilService = new TestProfilService();
		
		/* Creation d'un client Jersey */
		client = Client.create();
	}
	
	private void run() {
		
		insert();
		update();
		chargeEntity();
		delete();
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TestEventService().run();
	}
	
	
	public void insert() {
		
		/* Recuperation des profils */
		testProfilService.insert();
		Profil profilFrom = testProfilService.getProfil();

		testProfilService.insert();
		Profil profilTo = testProfilService.getProfil();

		/* Test d'insertion d'une event */
		webResource = client.resource(URL);
		event = new Event();
		event.setIdProfilFrom(profilFrom.getId());
		event.setIdProfilTo(profilTo.getId());
		event.setTypeEvent(1);
		event.setDescription("Appel en absence");
		event.setEtat("NP"); // pour non présenté
		event.setDateEvent("26/12/2010");
		event.setHeureEvent("08:00");

		event = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Event.class, event);
		
		System.out.println("Event créé : " + event);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du event */
		webResource = client.resource(URL + "/" + event.getId());
		event.setEtat("PT"); // Présenté et traité
		event = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Event.class, event);
		
		System.out.println("Event modifié : " + event);
	}
	
	public void chargeEntity() {
		/* Test de recuperation d'un event */
		webResource = client.resource(URL + "/" + event.getId());
		event = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Event.class);
		
		System.out.println("Event récupéré : " + event);
	}
	
	
	public void delete() {
		/* Test de suppression d'un event */
		webResource = client.resource(URL + "/" + event.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public Event getEvent() {
		return event;
	}
	
}

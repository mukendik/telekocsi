package com.telekocsi.server.localisation;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.profil.Profil;
import com.telekocsi.server.profil.TestProfilService;
import com.telekocsi.server.util.Tools;

public class TestLocalisationService {

	private static final String URL = Tools.getURL("/localisation");
	
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private Localisation localisation;
	private TestProfilService testProfilService;
	
	public TestLocalisationService() {

		testProfilService = new TestProfilService();
		testProfilService.insert();
		
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
		new TestLocalisationService().run();
	}
	
	
	public void insert() {
		
		/* Recuperation d'un itineraire */
		Profil profil = testProfilService.getProfil();
		
		/* Test d'insertion d'une localisation */
		webResource = client.resource(URL);
		localisation = new Localisation();
		localisation.setIdProfil(profil.getId());
		localisation.setPointGPS("458-5587");
		localisation.setDateLocalisation("26/12/2010");
		localisation.setHeureLocalisation("08:00");

		localisation = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Localisation.class, localisation);
		
		System.out.println("Localisation créée : " + localisation);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du localisation */
		webResource = client.resource(URL + "/" + localisation.getId());
		localisation.setPointGPS("459-6588");
		localisation.setHeureLocalisation("08:32");
		localisation = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Localisation.class, localisation);
		
		System.out.println("Localisation modifiée : " + localisation);
	}
	
	public void chargeEntity() {
		/* Test de recuperation d'un localisation */
		webResource = client.resource(URL + "/" + localisation.getId());
		localisation = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Localisation.class);
		
		System.out.println("Localisation récupérée : " + localisation);
	}
	
	
	public void delete() {
		/* Test de suppression d'un localisation */
		webResource = client.resource(URL + "/" + localisation.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public Localisation getLocalisation() {
		return localisation;
	}
	
}

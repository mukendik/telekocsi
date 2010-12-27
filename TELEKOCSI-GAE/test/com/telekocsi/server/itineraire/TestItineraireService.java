package com.telekocsi.server.itineraire;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.profil.TestProfilService;
import com.telekocsi.server.util.Tools;

public class TestItineraireService {

	private static final String URL = Tools.getURL("/itineraire");
		
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private Itineraire itineraire;
	private TestProfilService testProfilService;
	
	
	public TestItineraireService() {

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
		new TestItineraireService().run();
	}
	
	
	public void insert() {
		
		/* Test d'insertion d'un itineraire */
		webResource = client.resource(URL);
		itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("Universite de Nantes - Pôle scientifique");
		itineraire.setFrequenceTrajet("LMMJV");
		itineraire.setHoraireArrivee("09H00");
		itineraire.setHoraireDepart("07H45");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("NANTES");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(testProfilService.getProfil().getId());
		itineraire.setVariableDepart("5");
		
		itineraire = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Itineraire.class, itineraire);
		
		System.out.println("Itineraire créé : " + itineraire);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du itineraire */
		webResource = client.resource(URL + "/" + itineraire.getId());
		itineraire.setNbrePoint(4);
		itineraire = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Itineraire.class, itineraire);
		
		System.out.println("Itineraire modifié : " + itineraire);
	}
	
	public void chargeEntity() {
		/* Test de recuperation d'un itineraire */
		webResource = client.resource(URL + "/" + itineraire.getId());
		itineraire = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Itineraire.class);
		
		System.out.println("Itineraire récupéré : " + itineraire);
	}
	
	
	public void delete() {
		/* Test de suppression d'un itineraire */
		webResource = client.resource(URL + "/" + itineraire.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public Itineraire getItineraire() {
		return itineraire;
	}
	
}

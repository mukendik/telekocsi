package com.telekocsi.server.trajet;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.profil.Profil;
import com.telekocsi.server.profil.TestProfilService;
import com.telekocsi.server.util.Tools;

public class TestTrajetLigneService {

	private static final String URL = Tools.getURL("/trajet/ligne");
	
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private TrajetLigne trajetLigne;
	private TestTrajetService testTrajetService;
	private TestProfilService testProfilService;
	Profil profilPassager;
	Trajet trajet;
	
	
	public TestTrajetLigneService() {

		testTrajetService = new TestTrajetService();
		testTrajetService.insert();

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
		new TestTrajetLigneService().run();
	}
	
	
	public void insert() {
		
		/* Recuperation d'un itineraire */
		trajet = testTrajetService.getTrajet();
		profilPassager = testProfilService.getProfil();
		
		/* Test d'insertion d'une ligne trajet */
		webResource = client.resource(URL);
		trajetLigne = new TrajetLigne();
		trajetLigne.setIdProfilPassager(profilPassager.getId());
		trajetLigne.setIdTrajet(trajet.getId());
		trajetLigne.setNbrePoint(2);
		trajetLigne.setPlaceOccupee(1);
		
		trajetLigne = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(TrajetLigne.class, trajetLigne);
		
		System.out.println("Ligne de Trajet créée : " + trajetLigne);
	}
	
	
	public void update() {
		
		/* Test de mise a jour de la ligne de trajet */
		webResource = client.resource(URL + "/" + trajetLigne.getId());
		trajetLigne.setNbrePoint(3);
		trajetLigne = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(TrajetLigne.class, trajetLigne);
		
		System.out.println("Ligne de Trajet modifiée : " + trajetLigne);
	}
	
	public void chargeEntity() {
		/* Test de recuperation d'une ligne trajet */
		webResource = client.resource(URL + "/" + trajetLigne.getId());
		trajetLigne = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(TrajetLigne.class);
		
		System.out.println("Ligne de Trajet récupérée : " + trajetLigne);
		
		
		webResource = client.resource(URL + "/passagers/" + trajet.getId() + "/" + profilPassager.getId());
		trajetLigne = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(TrajetLigne.class);
		
		System.out.println("Ligne de Trajet récupérée : " + trajetLigne);
	}
	
	
	
	// passagers
	
	public void delete() {
		/* Test de suppression d'une ligne de trajet */
		webResource = client.resource(URL + "/" + trajetLigne.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public TrajetLigne getTrajetLigne() {
		return trajetLigne;
	}
	
}

package com.telekocsi.server.avis;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.profil.Profil;
import com.telekocsi.server.profil.TestProfilService;
import com.telekocsi.server.util.Tools;

public class TestAvisService {

	private static final String URL = Tools.getURL("/avis");
	
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private Avis avis;
	private TestProfilService testProfilService;
	
	public TestAvisService() {

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
		new TestAvisService().run();
	}
	
	
	public void insert() {
		
		/* Recuperation des profils */
		testProfilService.insert();
		Profil profilFrom = testProfilService.getProfil();

		testProfilService.insert();
		Profil profilTo = testProfilService.getProfil();

		/* Test d'insertion d'une avis */
		webResource = client.resource(URL);
		avis = new Avis();
		avis.setIdProfilFrom(profilFrom.getId());
		avis.setIdProfilTo(profilTo.getId());
		avis.setClassement(3);
		avis.setCommentaire("poncutel et agréable");
		avis.setChecked(false);
		avis.setEtat("A"); // pour attente
		avis.setDateAvis("26/12/2010");
		avis.setHeureAvis("08:00");

		avis = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Avis.class, avis);
		
		System.out.println("Avis créé : " + avis);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du avis */
		webResource = client.resource(URL + "/" + avis.getId());
		avis.setEtat("V");
		avis.setChecked(true);
		avis = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Avis.class, avis);
		
		System.out.println("Avis modifié : " + avis);
	}
	
	public void chargeEntity() {
		/* Test de recuperation d'un avis */
		webResource = client.resource(URL + "/" + avis.getId());
		avis = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Avis.class);
		
		System.out.println("Avis récupéré : " + avis);
	}
	
	
	public void delete() {
		/* Test de suppression d'un avis */
		webResource = client.resource(URL + "/" + avis.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public Avis getAvis() {
		return avis;
	}
	
}

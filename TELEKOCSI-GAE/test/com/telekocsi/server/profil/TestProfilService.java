package com.telekocsi.server.profil;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.util.Tools;

public class TestProfilService {

	private static final String URL = Tools.getURL("/profil");

	private Client client;
	private WebResource webResource;
	private Profil profil;
	
	
	public TestProfilService() {
		client = Client.create();
	}
	
	private void run() {
		insert();
		update();
		chargeEntity();
		login("pepito", "alma");
		delete();
		clear();
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TestProfilService().run();
	}
	
	
	public void insert() {
		/* Test d'insertion d'un profil */
		webResource = client.resource(URL);
		profil = new Profil();
		profil.setNom("BELIN");
		profil.setPrenom("");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setNombreAvis(5);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("10/06/1965");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("bbelin.sigal@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(10);
		profil.setPseudo("pepito");
		profil.setSexe("M");
		profil.setTelephone("02.51.64.97.07");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("207");
		
		profil = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Profil.class, profil);
		
		System.out.println("Profil créé : " + profil);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du profil */
		webResource = client.resource(URL + "/" + profil.getId());
		profil.setPrenom("Bruno");
		profil.setConnecte(true);
		profil = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Profil.class, profil);
		
		System.out.println("Profil modifié : " + profil);
	}
	
	
	public void chargeEntity() {
		
		/* Test de recuperation d'un profil */
		webResource = client.resource(URL + "/" + profil.getId());
		profil = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Profil.class);
		
		System.out.println("Profil récupéré : " + profil);
	}
	
	public void login(String pseudo, String passWord) {
		
		/* Test de recuperation d'un profil a partir du pseudo */
		webResource = client.resource(URL + "/login/" + pseudo + "/" + passWord);
		profil = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Profil.class);
		
		System.out.println("Login : " + profil);
	}
	
	
	public void delete() {
		/* Test de suppression d'un profil */
		webResource = client.resource(URL + "/" + profil.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
		
		System.out.println("Profil supprimé");
	}

	
	public void clear() {
		/* Test de suppression de tous les profils */
		webResource = client.resource(URL + "/clear");
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
		
		System.out.println("Tous les Profils ont été supprimés");
	}
	
	
	public static String getURLProfil() {
		return URL;
	}
	
	
	public Profil getProfil() {
		return profil;
	}
}

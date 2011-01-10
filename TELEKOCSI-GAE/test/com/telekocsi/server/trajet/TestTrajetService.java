package com.telekocsi.server.trajet;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.itineraire.Itineraire;
import com.telekocsi.server.itineraire.TestItineraireService;
import com.telekocsi.server.util.Tools;

public class TestTrajetService {

	private static final String URL = Tools.getURL("/trajet");
	
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private Trajet trajet;
	private TestItineraireService testItineraireService;
	
	public TestTrajetService() {

		testItineraireService = new TestItineraireService();
		testItineraireService.insert();
		
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
		new TestTrajetService().run();
	}
	
	
	public void insert() {
		
		/* Recuperation d'un itineraire */
		Itineraire itineraire = testItineraireService.getItineraire();
		
		/* Test d'insertion d'un trajet */
		webResource = client.resource(URL);
		trajet = new Trajet();
		trajet.setAutoroute(itineraire.isAutoroute());
		trajet.setCommentaire(itineraire.getCommentaire());
		trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
		trajet.setHoraireArrivee(itineraire.getHoraireArrivee());
		trajet.setHoraireDepart(itineraire.getHoraireDepart());
		trajet.setLieuDepart(itineraire.getLieuDepart());
		trajet.setLieuDestination(itineraire.getLieuDestination());
		trajet.setNbrePoint(itineraire.getNbrePoint());
		trajet.setPlaceDispo(itineraire.getPlaceDispo());
		trajet.setIdProfilConducteur(itineraire.getIdProfil());
		trajet.setIdItineraire(itineraire.getId());
		trajet.setVariableDepart(itineraire.getVariableDepart());
		trajet.setDateTrajet("26/12/2010");
		trajet.setSoldePlaceDispo(trajet.getPlaceDispo());
		trajet.setActif(true);
		
		trajet = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Trajet.class, trajet);
		
		System.out.println("Trajet créé : " + trajet);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du trajet */
		webResource = client.resource(URL + "/" + trajet.getId());
		trajet.setSoldePlaceDispo(trajet.getSoldePlaceDispo()-1);
		trajet = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Trajet.class, trajet);
		
		System.out.println("Trajet modifié : " + trajet);
	}
	
	public void chargeEntity() {
		chargeEntity(trajet.getId());
	}
	
	public Trajet chargeEntity(String id) {
		/* Test de recuperation d'un trajet */
		webResource = client.resource(URL + "/" + id);
		trajet = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Trajet.class);
		
		System.out.println("Trajet récupéré : " + trajet);
		return trajet;
	}
	
	
	public void delete() {
		/* Test de suppression d'un trajet */
		webResource = client.resource(URL + "/" + trajet.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public Trajet getTrajet() {
		return trajet;
	}
	
}

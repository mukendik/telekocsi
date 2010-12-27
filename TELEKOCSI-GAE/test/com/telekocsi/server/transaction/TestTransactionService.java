package com.telekocsi.server.transaction;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telekocsi.server.trajet.TestTrajetLigneService;
import com.telekocsi.server.trajet.TestTrajetService;
import com.telekocsi.server.trajet.Trajet;
import com.telekocsi.server.trajet.TrajetLigne;
import com.telekocsi.server.util.Tools;

public class TestTransactionService {

	private static final String URL = Tools.getURL("/transaction");
	
	/* Creation d'un client Jersey */
	private Client client;
	private WebResource webResource;
	private Transaction transaction;
	private TestTrajetLigneService testTrajetLigneService;
	private TestTrajetService testTrajetService;

	
	public TestTransactionService() {

		testTrajetLigneService = new TestTrajetLigneService();
		testTrajetLigneService.insert();

		testTrajetService = new TestTrajetService();

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
		new TestTransactionService().run();
	}
	
	
	public void insert() {
		
		/* Recuperation d'un itineraire */
		TrajetLigne trajetLigne = testTrajetLigneService.getTrajetLigne();
		Trajet trajet = testTrajetService.chargeEntity(trajetLigne.getIdTrajet());
		
		/* Test d'insertion d'une transaction */
		webResource = client.resource(URL);
		transaction = new Transaction();
		transaction.setIdTrajetLigne(trajetLigne.getId());
		transaction.setIdProfilConducteur(trajet.getIdProfilConducteur());
		transaction.setIdProfilPassager(trajetLigne.getIdProfilPassager());
		transaction.setDateTransaction("26/12/2010");
		transaction.setHeureTransaction("09:00");
		transaction.setPointEchange(2);

		transaction = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.put(Transaction.class, transaction);
		
		System.out.println("Transaction créée : " + transaction);
	}
	
	
	public void update() {
		
		/* Test de mise a jour du transaction */
		webResource = client.resource(URL + "/" + transaction.getId());
		transaction.setPointEchange(3);
		transaction = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(Transaction.class, transaction);
		
		System.out.println("Transaction modifiée : " + transaction);
	}
	
	public void chargeEntity() {
		/* Test de recuperation d'un transaction */
		webResource = client.resource(URL + "/" + transaction.getId());
		transaction = webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Transaction.class);
		
		System.out.println("Transaction récupérée : " + transaction);
	}
	
	
	public void delete() {
		/* Test de suppression d'un transaction */
		webResource = client.resource(URL + "/" + transaction.getId());
		webResource.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete();
	}

	
	public static String getURL() {
		return URL;
	}
	
	
	public Transaction getTransaction() {
		return transaction;
	}
	
}

package com.alma.telekocsi.dao.transaction;

import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.dao.trajet.TrajetLigne;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;

import android.util.Log;


public class TransactionTestDAO {

	ProfilDAO profilDAO;
	ItineraireDAO itineraireDAO;
	TrajetDAO trajetDAO;
	TrajetLigneDAO trajetLigneDAO;
	TransactionDAO transactionDAO;
	
	Profil profilConducteur;
	Profil profilPassager;
	Itineraire itineraire;
	Trajet trajet;
	TrajetLigne trajetLigne;
	
	
	public TransactionTestDAO() {
		profilDAO = new ProfilDAO();
		itineraireDAO = new ItineraireDAO();
		trajetDAO = new TrajetDAO();
		trajetLigneDAO = new TrajetLigneDAO();
		transactionDAO = new TransactionDAO();
		
		profilConducteur = new Profil();
		profilConducteur.setNom("BELIN");
		profilConducteur.setPrenom("Bruno");
		profilConducteur.setAnimaux("N");
		profilConducteur.setClassementMoyen(4);
		profilConducteur.setClasseVehicule(3);
		profilConducteur.setConnecte(false);
		profilConducteur.setDateNaissance("10/06/1965");
		profilConducteur.setDetours("N");
		profilConducteur.setDiscussion("O");
		profilConducteur.setEmail("bbelin.sigal@gmail.com");
		profilConducteur.setFumeur("N");
		profilConducteur.setMotDePasse("alma");
		profilConducteur.setMusique("O");
		profilConducteur.setPathPhoto("");
		profilConducteur.setPointsDispo(10);
		profilConducteur.setPseudo("pepito");
		profilConducteur.setSexe("M");
		profilConducteur.setTelephone("02.51.64.97.07");
		profilConducteur.setTypeProfil("C");
		profilConducteur.setTypeProfilHabituel("C");
		profilConducteur.setVehicule("207");		
		profilConducteur = profilDAO.insert(profilConducteur);
		
		Log.i(TransactionTestDAO.class.getSimpleName(), "insert profil conducteur : " + profilConducteur);		

		profilPassager = new Profil();
		profilPassager.setNom("TUPRIN");
		profilPassager.setPrenom("Sylvie");
		profilPassager.setAnimaux("N");
		profilPassager.setClassementMoyen(4);
		profilPassager.setClasseVehicule(3);
		profilPassager.setConnecte(false);
		profilPassager.setDateNaissance("10/06/1965");
		profilPassager.setDetours("N");
		profilPassager.setDiscussion("O");
		profilPassager.setEmail("sylvie.belin@gmail.com");
		profilPassager.setFumeur("N");
		profilPassager.setMotDePasse("alma");
		profilPassager.setMusique("O");
		profilPassager.setPathPhoto("");
		profilPassager.setPointsDispo(10);
		profilPassager.setPseudo("turpin");
		profilPassager.setSexe("M");
		profilPassager.setTelephone("02.51.64.87.07");
		profilPassager.setTypeProfil("C");
		profilPassager.setTypeProfilHabituel("C");
		profilPassager.setVehicule("POLO");		
		profilPassager = profilDAO.insert(profilPassager);
		
		Log.i(TransactionTestDAO.class.getSimpleName(), "insert profil passager : " + profilPassager);			
		
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
		itineraire.setIdProfil(profilConducteur.getId());
		itineraire.setVariableDepart("5");
		
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TransactionTestDAO.class.getSimpleName(), "insert : " + itineraire);
		
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
		trajet = trajetDAO.insert(trajet);
		Log.i(TransactionTestDAO.class.getSimpleName(), "insert trajet : " + trajet);
		
		trajetLigne = new TrajetLigne();
		trajetLigne.setIdProfilPassager(profilPassager.getId());
		trajetLigne.setIdTrajet(trajet.getId());
		trajetLigne.setNbrePoint(2);
		trajetLigne.setPlaceOccupee(1);
		trajetLigne = trajetLigneDAO.insert(trajetLigne);
		Log.i(TransactionTestDAO.class.getSimpleName(), "insert ligne trajet : " + trajetLigne);	
		
	}
	
	
	public void insert() {	
		
		Transaction transaction = new Transaction();
		transaction.setIdTrajetLigne(trajetLigne.getId());
		transaction.setIdProfilConducteur(trajet.getIdProfilConducteur());
		transaction.setIdProfilPassager(trajetLigne.getIdProfilPassager());
		transaction.setDateTransaction("26/12/2010");
		transaction.setHeureTransaction("09:00");
		transaction.setPointEchange(2);

		transactionDAO.insert(transaction);
	}
	
	
	public void getList() {
		List<Transaction> list = transactionDAO.getListConducteur(profilConducteur.getId());
		int cpt1 = 0;
		Log.i(TransactionTestDAO.class.getSimpleName(), "list des transactions conducteur ");
		for (Transaction transaction : list) {
			Log.i(TransactionTestDAO.class.getSimpleName(), "Transaction (" + (++cpt1) + ") : " + transaction.toString());
		}
		
		list = transactionDAO.getListPassager(profilPassager.getId());
		cpt1 = 0;
		Log.i(TransactionTestDAO.class.getSimpleName(), "list des transactions passager ");
		for (Transaction transaction : list) {
			Log.i(TransactionTestDAO.class.getSimpleName(), "Transaction (" + (++cpt1) + ") : " + transaction.toString());
		}
	}

	
	public void clear() {
		Integer nb = transactionDAO.clear();
		Log.i(TransactionTestDAO.class.getSimpleName(), "clear transactions : " + nb);
	}
	
}

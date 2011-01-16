package com.alma.telekocsi.init;

import java.util.List;

import android.util.Log;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.localisation.Localisation;
import com.alma.telekocsi.dao.localisation.LocalisationDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.dao.trajet.TrajetLigne;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.dao.trajet.TrajetTestDAO;
import com.alma.telekocsi.util.LocalDate;


public class LoadDataRomain {

	ProfilDAO profilDAO;
	ItineraireDAO itineraireDAO;
	TrajetDAO trajetDAO;
	TrajetLigneDAO trajetLigneDAO;
	LocalisationDAO localisationDAO;


	public LoadDataRomain() {
		profilDAO = new ProfilDAO();
		itineraireDAO = new ItineraireDAO();
		trajetDAO = new TrajetDAO();
		trajetLigneDAO = new TrajetLigneDAO();
		localisationDAO = new LocalisationDAO();
	}


	public void load() {

		clearMyProfils();

		insertMyProfils();
		insertMyItineraires();
		generateTrajet();

		initTrajetLigne();
		insertLocalisation();
	}




	private void clearMyProfils() {

		Log.i(LoadDataRomain.class.getSimpleName(), "debut suppression de mes profils de test");

		List<Profil> profils = profilDAO.getList();
		for (Profil profil : profils) {
			if (profil.getPseudo() == null)
				continue;

			if (profil.getPseudo().equalsIgnoreCase("rgournay@gmail.com") ||
					profil.getPseudo().equalsIgnoreCase("rg@passager.com") ||
					profil.getPseudo().equalsIgnoreCase("rg@passager.fr")) {

				Log.i(LoadDataRomain.class.getSimpleName(), "debut delete profil : " + profil.getPseudo());

				clearTrajet(profil.getId());
				clearItineraire(profil.getId());
				profilDAO.delete(profil);

				Log.i(LoadDataRomain.class.getSimpleName(), "fin delete profil : " + profil.getPseudo());
			}
		}
		Log.i(LoadDataRomain.class.getSimpleName(), "fin suppression de mes profils de test");
	}


	private void clearItineraire(String idProfil) {

		Log.i(LoadDataRomain.class.getSimpleName(), "debut suppression des itineraires de test pour le profil : " + idProfil);

		List<Itineraire> itineraires = itineraireDAO.getList(idProfil);
		for (Itineraire itineraire : itineraires) {
			Log.i(LoadDataBruno.class.getSimpleName(), "delete itineraire : " + itineraire.getId());
			itineraireDAO.delete(itineraire);
		}

		Log.i(LoadDataRomain.class.getSimpleName(), "fin suppression des itineraires de test pour le profil : " + idProfil);
	}


	private void clearTrajet(String idProfil) {

		List<Trajet> trajets = trajetDAO.getList(idProfil);

		Log.i(LoadDataRomain.class.getSimpleName(), "debut suppression des trajets de test pour le profil : " + idProfil);
		if(trajets!=null){
			for (Trajet trajet : trajets) {
				clearTrajetLigne(trajet.getId());
				trajetDAO.delete(trajet);
			}
		}

		Log.i(LoadDataRomain.class.getSimpleName(), "fin suppression des trajets de test pour le profil : " + idProfil);
	}


	private void clearTrajetLigne(String idTrajet) {

		List<TrajetLigne> trajetLignes = trajetLigneDAO.getList(idTrajet);

		Log.i(LoadDataRomain.class.getSimpleName(), "debut suppression des lignes de trajets de test pour le trajet : " + idTrajet);

		for (TrajetLigne trajetLigne : trajetLignes) {
			trajetLigneDAO.delete(trajetLigne);
		}

		Log.i(LoadDataRomain.class.getSimpleName(), "fin suppression des lignes de trajets de test pour le trajet : " + idTrajet);
	}	



	private void insertMyProfils() {	

		Profil profil = new Profil();
		profil.setNom("Gconducteur");
		profil.setPrenom("Romain");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setNombreAvis(5);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("01/02/1987");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("rgournay@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("aze");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(10);
		profil.setPseudo("rgournay@gmail.com");
		profil.setSexe("H");
		profil.setTelephone("0203040506");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("106");
		profil = profilDAO.insert(profil);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert profil : " + profil);

		profil = new Profil();
		profil.setNom("Passager");
		profil.setPrenom("Paul");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setNombreAvis(5);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("10/06/1978");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("rg@passager.com");
		profil.setFumeur("N");
		profil.setMotDePasse("aze");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(10);
		profil.setPseudo("rg@passager.com");
		profil.setSexe("H");
		profil.setTelephone("0215469779");
		profil.setTypeProfil("P");
		profil.setTypeProfilHabituel("P");
		profil.setVehicule("207");
		profil = profilDAO.insert(profil);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert profil : " + profil);		

		profil = new Profil();
		profil.setNom("Passager");
		profil.setPrenom("Pierre");
		profil.setAnimaux("N");
		profil.setClassementMoyen(3);
		profil.setNombreAvis(4);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("01/03/1947");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("rg@passager.fr");
		profil.setFumeur("N");
		profil.setMotDePasse("aze");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(15);
		profil.setPseudo("rg@passager.fr");
		profil.setSexe("F");
		profil.setTelephone("0251143407");
		profil.setTypeProfil("P");
		profil.setTypeProfilHabituel("P");
		profil.setVehicule("Polo");	
		profil = profilDAO.insert(profil);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert profil : " + profil);		
	}


	private void insertMyItineraires() {

		Itineraire itineraire = new Itineraire();
		itineraire.setAutoroute(false);
		itineraire.setCommentaire("trajet Bruno via CHOLET");
		itineraire.setFrequenceTrajet("OOOOONN");
		itineraire.setHoraireDepart("07H30");
		itineraire.setHoraireArrivee("08H45");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuPassage1("CHOLET");
		itineraire.setLieuDestination("NANTES");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(profilDAO.login("rgournay@gmail.com", "aze").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);


		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("rg@passager.com", "aze").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);

		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("rg@passager.fr", "aze").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);


		itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("trajet Bruno sans passer par cholet");
		itineraire.setFrequenceTrajet("OOOOONN");
		itineraire.setHoraireArrivee("19H30");
		itineraire.setHoraireDepart("18H10");
		itineraire.setLieuDepart("NANTES");
		itineraire.setLieuDestination("LES HERBIERS");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(profilDAO.login("rgournay@gmail.com", "aze").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);	

		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("rg@passager.com", "aze").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);


		itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("trajet Bruno avec Autoroute");
		itineraire.setFrequenceTrajet("NONONNN");
		itineraire.setHoraireArrivee("08H15");
		itineraire.setHoraireDepart("07H00");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("ANGERS");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(2);
		itineraire.setIdProfil(profilDAO.login("rgournay@gmail.com", "aze").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert itineraire : " + itineraire);

		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("rg@passager.com", "aze").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);


		itineraire = new Itineraire();
		itineraire.setAutoroute(false);
		itineraire.setCommentaire("trajet Bruno sans Autoroute");
		itineraire.setFrequenceTrajet("NONONNN");
		itineraire.setHoraireArrivee("19H30");
		itineraire.setHoraireDepart("18H30");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("ANGERS");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(2);
		itineraire.setIdProfil(profilDAO.login("rgournay@gmail.com", "aze").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire  : " + itineraire);

		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("rg@passager.fr", "aze").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert itineraire : " + itineraire);

	}


	private void generateTrajet() {
		int nb =0;
		String jour="";

		for(int j = 15 ; j< 22;j++){
			if(j<10){
				jour="0"+j;
			}else 
			{
				jour=j+"";
			}

			nb = trajetDAO.generate(jour+"/01/2011");
			Log.i(LoadDataRomain.class.getSimpleName(), "generate trajet "+jour+"/01/2010 : " + nb);
		}


	}


	private void initTrajetLigne() {

		String idConducteur = profilDAO.login("rgournay@gmail.com", "aze").getId();
		List<Trajet> trajets = trajetDAO.getList(idConducteur);



		if (trajets.size() > 0) {
			Trajet trajet1 = trajets.get(0);
			trajet1.setEtat(1);
			trajetDAO.update(trajet1);

			TrajetLigne trajetLigne1 = new TrajetLigne();
			trajetLigne1.setIdProfilPassager(profilDAO.login("rg@passager.com", "aze").getId());
			trajetLigne1.setIdTrajet(trajet1.getId());
			trajetLigne1.setNbrePoint(4);
			trajetLigne1.setPlaceOccupee(1);
			trajetLigneDAO.insert(trajetLigne1);
			Log.i(LoadDataRomain.class.getSimpleName(), "insert trajetLigne : " + trajetLigne1);

			TrajetLigne trajetLigne2 = new TrajetLigne();
			trajetLigne2.setIdProfilPassager(profilDAO.login("rg@passager.fr", "aze").getId());
			trajetLigne2.setIdTrajet(trajet1.getId());
			trajetLigne2.setNbrePoint(3);
			trajetLigne2.setPlaceOccupee(1);
			trajetLigneDAO.insert(trajetLigne2);
			Log.i(LoadDataRomain.class.getSimpleName(), "insert trajetLigne : " + trajetLigne2);


		}
	}

	private void insertLocalisation() {

		LocalDate currentDate = new LocalDate();
		
		

		Localisation localisation1 = new Localisation();
		localisation1.setDateLocalisation(currentDate.getDateFormatCalendar());
		localisation1.setHeureLocalisation(currentDate.getDateFomatHeure());
		localisation1.setIdProfil(profilDAO.login("rg@passager.fr", "aze").getId());
		localisation1.setLatitude(47.092566);
		localisation1.setLongitude(-0.98156);
		localisation1.setPointGPS("47.092566,-0.98156");
		localisationDAO.insert(localisation1);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert localisation : " + localisation1);


		Localisation localisation2 = new Localisation();
		localisation2.setDateLocalisation(currentDate.getDateFormatCalendar());
		localisation2.setHeureLocalisation(currentDate.getDateFomatHeure());
		localisation2.setIdProfil(profilDAO.login("rg@passager.com", "aze").getId());
		localisation2.setLatitude(47.148356);
		localisation2.setLongitude(-1.193669);
		localisation2.setPointGPS("47.148356,-1.193669");
		localisationDAO.insert(localisation2);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert localisation : " + localisation2);
		
		
		Localisation localisation3 = new Localisation();
		localisation3.setDateLocalisation(currentDate.getDateFormatCalendar());
		localisation3.setHeureLocalisation(currentDate.getDateFomatHeure());
		localisation3.setIdProfil(profilDAO.login("rgournay@gmail.com", "aze").getId());
		localisation3.setLatitude(46.962213);
		localisation3.setLongitude(-0.973663);
		localisation3.setPointGPS("46.962213,-0.973663");
		localisationDAO.insert(localisation3);
		Log.i(LoadDataRomain.class.getSimpleName(), "insert localisation : " + localisation3);
	}

}

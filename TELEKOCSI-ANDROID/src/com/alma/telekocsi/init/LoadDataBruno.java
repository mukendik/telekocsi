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


public class LoadDataBruno {

	ProfilDAO profilDAO;
	ItineraireDAO itineraireDAO;
	TrajetDAO trajetDAO;
	TrajetLigneDAO trajetLigneDAO;
	LocalisationDAO localisationDAO;
	
	
	public LoadDataBruno() {
		profilDAO = new ProfilDAO();
		itineraireDAO = new ItineraireDAO();
		trajetDAO = new TrajetDAO();
		trajetLigneDAO = new TrajetLigneDAO();
		localisationDAO = new LocalisationDAO();
	}
	
	
	public void load() {
		
		/*clearMyProfils();
		
		insertMyProfils();
		insertMyItineraires();
		generateTrajet();*/
		
		initTrajetLigne();
		//insertLocalisation();
	}
	
	
	private void clearMyProfils() {
		
		Log.i(LoadDataBruno.class.getSimpleName(), "debut suppression de mes profils de test");
		
		List<Profil> profils = profilDAO.getList();
		for (Profil profil : profils) {
			if (profil.getPseudo() == null)
				continue;
			
			if (profil.getPseudo().equalsIgnoreCase("conducteur1") ||
					profil.getPseudo().equalsIgnoreCase("conducteur2") ||
					profil.getPseudo().equalsIgnoreCase("passager1") ||
					profil.getPseudo().equalsIgnoreCase("passager2")) {
				
				Log.i(LoadDataBruno.class.getSimpleName(), "debut delete profil : " + profil.getPseudo());
				
				clearTrajet(profil.getId());
				clearItineraire(profil.getId());
				profilDAO.delete(profil);
				
				Log.i(LoadDataBruno.class.getSimpleName(), "fin delete profil : " + profil.getPseudo());
			}
		}
		Log.i(LoadDataBruno.class.getSimpleName(), "fin suppression de mes profils de test");
	}
	
	
	private void clearItineraire(String idProfil) {
		
		Log.i(LoadDataBruno.class.getSimpleName(), "debut suppression des itineraires de test pour le profil : " + idProfil);
		
		List<Itineraire> itineraires = itineraireDAO.getList(idProfil);
		for (Itineraire itineraire : itineraires) {
			Log.i(LoadDataBruno.class.getSimpleName(), "delete itineraire : " + itineraire.getId());
			itineraireDAO.delete(itineraire);
		}
		
		Log.i(LoadDataBruno.class.getSimpleName(), "fin suppression des itineraires de test pour le profil : " + idProfil);
	}
	
	
	private void clearTrajet(String idProfil) {
		
		List<Trajet> trajets = trajetDAO.getList(idProfil);
		
		Log.i(LoadDataBruno.class.getSimpleName(), "debut suppression des trajets de test pour le profil : " + idProfil);
		
		for (Trajet trajet : trajets) {
			clearTrajetLigne(trajet.getId());
			trajetDAO.delete(trajet);
		}
		
		Log.i(LoadDataBruno.class.getSimpleName(), "fin suppression des trajets de test pour le profil : " + idProfil);
	}
	
	
	private void clearTrajetLigne(String idTrajet) {
		
		List<TrajetLigne> trajetLignes = trajetLigneDAO.getList(idTrajet);
		
		Log.i(LoadDataBruno.class.getSimpleName(), "debut suppression des lignes de trajets de test pour le trajet : " + idTrajet);
		
		for (TrajetLigne trajetLigne : trajetLignes) {
			trajetLigneDAO.delete(trajetLigne);
		}
		
		Log.i(LoadDataBruno.class.getSimpleName(), "fin suppression des lignes de trajets de test pour le trajet : " + idTrajet);
	}	
	
	
	
	private void insertMyProfils() {	
		
		Profil profil = new Profil();
		profil.setNom("CONDUCTEUR1");
		profil.setPrenom("Bruno");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setNombreAvis(5);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("10/06/1968");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("conducteur1@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(10);
		profil.setPseudo("conducteur1");
		profil.setSexe("H");
		profil.setTelephone("02.51.64.97.97");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("207");
		profil = profilDAO.insert(profil);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert profil : " + profil);

		profil = new Profil();
		profil.setNom("CONDUCTEUR2");
		profil.setPrenom("Didier");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setNombreAvis(5);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("10/09/1968");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("conducteur2@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(10);
		profil.setPseudo("conducteur2");
		profil.setSexe("H");
		profil.setTelephone("02.51.64.97.97");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("207");
		profil = profilDAO.insert(profil);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert profil : " + profil);		
		
		profil = new Profil();
		profil.setNom("PASSAGER1");
		profil.setPrenom("Julien");
		profil.setAnimaux("N");
		profil.setClassementMoyen(3);
		profil.setNombreAvis(4);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("04/06/1967");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("passager1@free.fr");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(15);
		profil.setPseudo("passager1");
		profil.setSexe("F");
		profil.setTelephone("02.51.64.97.07");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("P");
		profil.setVehicule("Polo");	
		profil = profilDAO.insert(profil);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert profil : " + profil);
		
		profil = new Profil();
		profil.setNom("PASSAGER2");
		profil.setPrenom("Romain");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setNombreAvis(3);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("04/04/1987");
		profil.setDetours("O");
		profil.setDiscussion("O");
		profil.setEmail("passager2@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(8);
		profil.setPseudo("passager2");
		profil.setSexe("H");
		profil.setTelephone("02.51.67.87.86");
		profil.setTypeProfil("P");
		profil.setTypeProfilHabituel("P");
		profil.setVehicule("Clio");
		profil = profilDAO.insert(profil);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert profil : " + profil);		
	}
	
	
	private void insertMyItineraires() {
		
		Itineraire itineraire = new Itineraire();
		itineraire.setAutoroute(false);
		itineraire.setCommentaire("Universite de Nantes - Pôle scientifique");
		itineraire.setFrequenceTrajet("OOOOONN");
		itineraire.setHoraireArrivee("09H00");
		itineraire.setHoraireDepart("07H45");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("NANTES");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(profilDAO.login("conducteur1", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire : " + itineraire);

		
		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("passager1", "alma").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire : " + itineraire);
		
		
		itineraire = new Itineraire();
		itineraire.setAutoroute(false);
		itineraire.setCommentaire("Universite de Nantes - Pôle scientifique");
		itineraire.setFrequenceTrajet("OOOOONN");
		itineraire.setHoraireArrivee("19H30");
		itineraire.setHoraireDepart("18H10");
		itineraire.setLieuDepart("NANTES");
		itineraire.setLieuDestination("LES HERBIERS");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(profilDAO.login("conducteur1", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire : " + itineraire);	
		
		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("passager1", "alma").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire : " + itineraire);
		
		
		itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("CAP Emploi");
		itineraire.setFrequenceTrajet("NONONNN");
		itineraire.setHoraireArrivee("08H15");
		itineraire.setHoraireDepart("07H00");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("ANGERS");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(2);
		itineraire.setIdProfil(profilDAO.login("conducteur2", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert itineraire : " + itineraire);

		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("passager2", "alma").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire : " + itineraire);
		
		
		itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("CAP Emploi");
		itineraire.setFrequenceTrajet("NONONNN");
		itineraire.setHoraireArrivee("19H30");
		itineraire.setHoraireDepart("18H30");
		itineraire.setLieuDepart("ANGERS");
		itineraire.setLieuDestination("LES HERBIERS");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(2);
		itineraire.setIdProfil(profilDAO.login("conducteur2", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire  : " + itineraire);
		
		itineraire.setId(null);
		itineraire.setPlaceDispo(0);
		itineraire.setIdProfil(profilDAO.login("passager2", "alma").getId());
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(LoadDataBruno.class.getSimpleName(), "insert itineraire : " + itineraire);
		
	}
	
	
	private void generateTrajet() {
		int nb = trajetDAO.generate("13/01/2011");
		Log.i(LoadDataBruno.class.getSimpleName(), "generate trajet 13/01/2010 : " + nb);
		
		nb = trajetDAO.generate("14/01/2011");
		Log.i(LoadDataBruno.class.getSimpleName(), "generate trajet 14/01/2010 : " + nb);
		
		nb = trajetDAO.generate("17/01/2011");
		Log.i(LoadDataBruno.class.getSimpleName(), "generate trajet 17/01/2010 : " + nb);
		
		nb = trajetDAO.generate("18/01/2011");
		Log.i(LoadDataBruno.class.getSimpleName(), "generate trajet 18/01/2010 : " + nb);
		

	}
	
	
	private void initTrajetLigne() {
		
		String idConducteur = profilDAO.login("rgournay@gmail.com", "azeaze").getId();
		List<Trajet> trajets = trajetDAO.getList(idConducteur);
		
		TrajetLigne trajetLigne1 = new TrajetLigne();
		trajetLigne1.setIdProfilPassager(profilDAO.login("brunobelin@free.fr", "alma").getId());
		trajetLigne1.setIdTrajet(trajets.get(0).getId());
		trajetLigne1.setNbrePoint(4);
		trajetLigne1.setPlaceOccupee(1);
		trajetLigneDAO.insert(trajetLigne1);
		Log.i(LoadData.class.getSimpleName(), "insert trajetLigne : " + trajetLigne1);
		
		TrajetLigne trajetLigne2 = new TrajetLigne();
		trajetLigne2.setIdProfilPassager(profilDAO.login("passager2", "alma").getId());
		trajetLigne2.setIdTrajet(trajets.get(0).getId());
		trajetLigne2.setNbrePoint(3);
		trajetLigne2.setPlaceOccupee(1);
		trajetLigneDAO.insert(trajetLigne2);
		Log.i(LoadData.class.getSimpleName(), "insert trajetLigne : " + trajetLigne2);	
	}
	
	
	private void insertLocalisation() {
		
		LocalDate currentDate = new LocalDate();
		
		Localisation localisation1 = new Localisation();
		localisation1.setDateLocalisation(currentDate.getDateFormatCalendar());
		localisation1.setHeureLocalisation(currentDate.getDateFomatHeure());
		localisation1.setIdProfil(profilDAO.login("brunobelin@free.fr", "alma").getId());
		localisation1.setLatitude(47.092566);
		localisation1.setLongitude(-0.98156);
		localisation1.setPointGPS("47.092566,-0.98156");
		localisationDAO.insert(localisation1);
		Log.i(LoadData.class.getSimpleName(), "insert localisation : " + localisation1);
		
		
		Localisation localisation2 = new Localisation();
		localisation2.setDateLocalisation(currentDate.getDateFormatCalendar());
		localisation2.setHeureLocalisation(currentDate.getDateFomatHeure());
		localisation2.setIdProfil(profilDAO.login("passager2", "alma").getId());
		localisation2.setLatitude(47.148356);
		localisation2.setLongitude(-1.193669);
		localisation2.setPointGPS("47.148356,-1.193669");
		localisationDAO.insert(localisation2);
		Log.i(LoadData.class.getSimpleName(), "insert localisation : " + localisation2);
	}
	
}

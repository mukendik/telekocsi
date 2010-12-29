package com.alma.telekocsi.init;


import android.util.Log;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.dao.trajet.TrajetTestDAO;


public class LoadData {

	ProfilDAO profilDAO;
	ItineraireDAO itineraireDAO;
	TrajetDAO trajetDAO;
	TrajetLigneDAO trajetLigneDAO;

	
	public LoadData() {
		profilDAO = new ProfilDAO();
		itineraireDAO = new ItineraireDAO();
		trajetDAO = new TrajetDAO();
		trajetLigneDAO = new TrajetLigneDAO();
	}
	
	public void load() {
		
		clearTrajet();
		clearItineraire();
		clearProfil();
		
		insertProfil();
		insertItineraire();
		generateTrajet();
	}
	
	
	private void clearProfil() {
		int nb = profilDAO.clear();
		Log.i(LoadData.class.getSimpleName(), "clear profil : " + nb);
	}
	
	
	private void insertProfil() {	
		
		Profil profil = new Profil();
		profil.setNom("BELIN");
		profil.setPrenom("Bruno");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
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
		profil.setPseudo("bbelin");
		profil.setSexe("M");
		profil.setTelephone("02.51.64.97.07");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("207");	
		profil = profilDAO.insert(profil);
		Log.i(LoadData.class.getSimpleName(), "insert profil : " + profil);
		
		profil = new Profil();
		profil.setNom("BELIN");
		profil.setPrenom("Sylvie");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("04/06/1967");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("sbelin@free.fr");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(15);
		profil.setPseudo("sbelin");
		profil.setSexe("F");
		profil.setTelephone("02.51.64.97.08");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("Polo");	
		profil = profilDAO.insert(profil);
		Log.i(LoadData.class.getSimpleName(), "insert profil : " + profil);
		
		profil = new Profil();
		profil.setNom("GOURNAY");
		profil.setPrenom("Romain");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("04/04/1987");
		profil.setDetours("O");
		profil.setDiscussion("O");
		profil.setEmail("rgournay@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(8);
		profil.setPseudo("rgournay");
		profil.setSexe("M");
		profil.setTelephone("02.51.67.87.86");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("Clio");
		profil = profilDAO.insert(profil);
		Log.i(LoadData.class.getSimpleName(), "insert profil : " + profil);		
	}
	
	
	private void clearItineraire() {
		int nb = itineraireDAO.clear();
		Log.i(LoadData.class.getSimpleName(), "clear des itineraires : " + nb);
	}
	
	
	private void insertItineraire() {
		
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
		itineraire.setIdProfil(profilDAO.login("bbelin", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert itineraire : " + itineraire);

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
		itineraire.setIdProfil(profilDAO.login("bbelin", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert itineraire : " + itineraire);		
		
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
		itineraire.setIdProfil(profilDAO.login("sbelin", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert itineraire : " + itineraire);
		
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
		itineraire.setIdProfil(profilDAO.login("sbelin", "alma").getId());
		itineraire.setVariableDepart("5");
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert itineraire  : " + itineraire);
	}
	
	
	private void clearTrajet() {
		int nb = trajetDAO.clear();
		Log.i(TrajetTestDAO.class.getSimpleName(), "clear trajet : " + nb);
	}
	
	private void generateTrajet() {
		int nb = trajetDAO.generate("03/01/2011");
		Log.i(TrajetTestDAO.class.getSimpleName(), "generate trajet 03/01/2010 : " + nb);
		
		nb = trajetDAO.generate("04/01/2011");
		Log.i(TrajetTestDAO.class.getSimpleName(), "generate trajet 04/01/2010 : " + nb);
	}
	
	
}

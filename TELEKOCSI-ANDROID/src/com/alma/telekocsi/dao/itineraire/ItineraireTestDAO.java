package com.alma.telekocsi.dao.itineraire;

import java.util.List;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

import android.util.Log;

public class ItineraireTestDAO {

	ProfilDAO profilDAO;
	ItineraireDAO itineraireDAO;
	Profil profil;
	
	public ItineraireTestDAO() {
		profilDAO = new ProfilDAO();
		itineraireDAO = new ItineraireDAO();
		
		profil = new Profil();
		profil.setNom("BELIN");
		profil.setPrenom("");
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
		profil.setPseudo("pepito");
		profil.setSexe("M");
		profil.setTelephone("02.51.64.97.07");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("207");		
		profil = profilDAO.insert(profil);
		
		Log.i(ItineraireTestDAO.class.getSimpleName(), "insert profil : " + profil);		
	}
	
	
	public void insert() {	
		
		Itineraire itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("Universite de Nantes - Pôle scientifique");
		itineraire.setFrequenceTrajet("LMMJV");
		itineraire.setHoraireArrivee("09H00");
		itineraire.setHoraireDepart("07H45");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("NANTES");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(profil.getId());
		itineraire.setVariableDepart("5");
		
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(ItineraireTestDAO.class.getSimpleName(), "insert : " + itineraire);
	}
	
	
	public void getList() {
		List<Itineraire> list = itineraireDAO.getList(profil.getId());
		int cpt = 0;
		for (Itineraire itineraire : list)
			Log.i(ItineraireTestDAO.class.getSimpleName(), "list(" + (++cpt) + ") : " + itineraire.toString());
	}

	
	public void clear() {
		Integer nb = itineraireDAO.clear();
		Log.i(ItineraireTestDAO.class.getSimpleName(), "clear : " + nb);
	}
	
}

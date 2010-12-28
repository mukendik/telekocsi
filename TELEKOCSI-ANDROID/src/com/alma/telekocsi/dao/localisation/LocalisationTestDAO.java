package com.alma.telekocsi.dao.localisation;

import java.util.List;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

import android.util.Log;


public class LocalisationTestDAO {

	ProfilDAO profilDAO;
	LocalisationDAO localisationDAO;
	
	Profil profil;	
	
	public LocalisationTestDAO() {
		profilDAO = new ProfilDAO();
		localisationDAO = new LocalisationDAO();
		
		profil = new Profil();
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
		profil.setPseudo("pepito");
		profil.setSexe("M");
		profil.setTelephone("02.51.64.97.07");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("207");		
		profil = profilDAO.insert(profil);
		
		Log.i(LocalisationTestDAO.class.getSimpleName(), "insert profil conducteur : " + profil);					
	}
	
	
	public void insert() {	
		
		Localisation localisation = new Localisation();
		localisation.setIdProfil(profil.getId());
		localisation.setPointGPS("458-5587");
		localisation.setDateLocalisation("26/12/2010");
		localisation.setHeureLocalisation("08:00");
		localisationDAO.insert(localisation);
	}
	
	
	public void getList() {
		
		List<Localisation> list = localisationDAO.getList(profil.getId());
		int cpt = 0;
		Log.i(LocalisationTestDAO.class.getSimpleName(), "list des localisations ");
		for (Localisation localisation : list) {
			Log.i(LocalisationTestDAO.class.getSimpleName(), "Localisation (" + (++cpt) + ") : " + localisation.toString());
		}
	}

	
	public void clear() {
		Integer nb = localisationDAO.clear(profil.getId());
		Log.i(LocalisationTestDAO.class.getSimpleName(), "clear localisations : " + nb);
	}
	
}

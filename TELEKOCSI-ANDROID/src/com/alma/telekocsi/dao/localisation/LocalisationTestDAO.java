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
		profil.setNom("Guilles");
		profil.setPrenom("Mathieu");
		profil.setAnimaux("N");
		profil.setClassementMoyen(4);
		profil.setClasseVehicule(3);
		profil.setConnecte(false);
		profil.setDateNaissance("10/06/1975");
		profil.setDetours("N");
		profil.setDiscussion("O");
		profil.setEmail("mguilles@gmail.com");
		profil.setFumeur("N");
		profil.setMotDePasse("alma");
		profil.setMusique("O");
		profil.setPathPhoto("");
		profil.setPointsDispo(10);
		profil.setPseudo("mguilles");
		profil.setSexe("M");
		profil.setTelephone("02.51.64.97.07");
		profil.setTypeProfil("C");
		profil.setTypeProfilHabituel("C");
		profil.setVehicule("307");		
		profil = profilDAO.insert(profil);
		
		Log.i(LocalisationTestDAO.class.getSimpleName(), "insert profil conducteur : " + profil);					
	}
	
	
	public void insert() {	
		
		Localisation localisation = new Localisation();
		localisation.setIdProfil(profil.getId());
		localisation.setPointGPS("458-5587");
		localisation.setLongitude(-47.23454);
		localisation.setLatitude(-48.55555);
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

	
	/*
	 * Doit etre appele pendant la session courante sinon le profil sera different
	 * et il n'y aura pas de suppression
	 */
	public void clear() {
		Integer nb = localisationDAO.clear(profil.getId());
		Log.i(LocalisationTestDAO.class.getSimpleName(), "clear localisations : " + nb);
	}
	
	
	public void clearAll() {
		Integer nb = localisationDAO.clear();
		Log.i(LocalisationTestDAO.class.getSimpleName(), "clear localisations : " + nb);
	}
	
}

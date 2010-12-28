package com.alma.telekocsi.dao.avis;

import java.util.List;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

import android.util.Log;


public class AvisTestDAO {

	ProfilDAO profilDAO;
	AvisDAO avisDAO;
	
	Profil profilFrom;
	Profil profilTo;	
	
	public AvisTestDAO() {
		profilDAO = new ProfilDAO();
		avisDAO = new AvisDAO();
		
		profilFrom = new Profil();
		profilFrom.setNom("BELIN");
		profilFrom.setPrenom("Bruno");
		profilFrom.setAnimaux("N");
		profilFrom.setClassementMoyen(4);
		profilFrom.setClasseVehicule(3);
		profilFrom.setConnecte(false);
		profilFrom.setDateNaissance("10/06/1965");
		profilFrom.setDetours("N");
		profilFrom.setDiscussion("O");
		profilFrom.setEmail("bbelin.sigal@gmail.com");
		profilFrom.setFumeur("N");
		profilFrom.setMotDePasse("alma");
		profilFrom.setMusique("O");
		profilFrom.setPathPhoto("");
		profilFrom.setPointsDispo(10);
		profilFrom.setPseudo("pepito");
		profilFrom.setSexe("M");
		profilFrom.setTelephone("02.51.64.97.07");
		profilFrom.setTypeProfil("C");
		profilFrom.setTypeProfilHabituel("C");
		profilFrom.setVehicule("207");		
		profilFrom = profilDAO.insert(profilFrom);
		
		Log.i(AvisTestDAO.class.getSimpleName(), "insert profil conducteur : " + profilFrom);		

		profilTo = new Profil();
		profilTo.setNom("TUPRIN");
		profilTo.setPrenom("Sylvie");
		profilTo.setAnimaux("N");
		profilTo.setClassementMoyen(4);
		profilTo.setClasseVehicule(3);
		profilTo.setConnecte(false);
		profilTo.setDateNaissance("10/06/1965");
		profilTo.setDetours("N");
		profilTo.setDiscussion("O");
		profilTo.setEmail("sylvie.belin@gmail.com");
		profilTo.setFumeur("N");
		profilTo.setMotDePasse("alma");
		profilTo.setMusique("O");
		profilTo.setPathPhoto("");
		profilTo.setPointsDispo(10);
		profilTo.setPseudo("turpin");
		profilTo.setSexe("M");
		profilTo.setTelephone("02.51.64.87.07");
		profilTo.setTypeProfil("C");
		profilTo.setTypeProfilHabituel("C");
		profilTo.setVehicule("POLO");		
		profilTo = profilDAO.insert(profilTo);
		
		Log.i(AvisTestDAO.class.getSimpleName(), "insert profil passager : " + profilTo);			
		
	}
	
	
	public void insert() {	
		
		Avis avis = new Avis();
		avis.setIdProfilFrom(profilFrom.getId());
		avis.setIdProfilTo(profilTo.getId());
		avis.setClassement(3);
		avis.setCommentaire("poncutel et agréable");
		avis.setChecked(false);
		avis.setEtat("A"); // pour attente
		avis.setDateAvis("26/12/2010");
		avis.setHeureAvis("08:00");

		avisDAO.insert(avis);
	}
	
	
	public void getList() {
		List<Avis> list = avisDAO.getListFrom(profilFrom.getId());
		int cpt1 = 0;
		Log.i(AvisTestDAO.class.getSimpleName(), "list des avis from ");
		for (Avis avis : list) {
			Log.i(AvisTestDAO.class.getSimpleName(), "Avis (" + (++cpt1) + ") : " + avis.toString());
		}
		
		list = avisDAO.getListTo(profilTo.getId());
		cpt1 = 0;
		Log.i(AvisTestDAO.class.getSimpleName(), "list des avis to ");
		for (Avis avis : list) {
			Log.i(AvisTestDAO.class.getSimpleName(), "Avis (" + (++cpt1) + ") : " + avis.toString());
		}
	}

	
	public void clear() {
		Integer nb = avisDAO.clear();
		Log.i(AvisTestDAO.class.getSimpleName(), "clear avis : " + nb);
	}
	
}

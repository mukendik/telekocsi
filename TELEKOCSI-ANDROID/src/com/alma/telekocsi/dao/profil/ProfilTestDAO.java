package com.alma.telekocsi.dao.profil;

import java.util.List;

import android.util.Log;

public class ProfilTestDAO {

	ProfilDAO consumer;
	
	public ProfilTestDAO() {
		consumer = new ProfilDAO();
	}
	
	
	public void insert() {	
		
		Profil profil = new Profil();
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
		profil = consumer.insert(profil);
		
		Log.i(ProfilTestDAO.class.getSimpleName(), "insert : " + profil);
	}
	
	
	public void getList() {
		List<Profil> list = consumer.getList();
		for (Profil profil : list)
			Log.i(ProfilTestDAO.class.getSimpleName(), "list : " + profil.toString());
	}
	
	
	public void login() {
		Profil profil = consumer.login("pepito", "alma");
		Log.i(ProfilTestDAO.class.getSimpleName(), "login : " + profil);
	}

	
	public void clear() {
		Integer nb = consumer.clear();
		Log.i(ProfilTestDAO.class.getSimpleName(), "clear : " + nb);
	}
	
}

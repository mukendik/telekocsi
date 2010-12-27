package com.alma.telekocsi.dao.profil;

public class ProfilTest {

	ProfilConsumer consumer;
	
	public ProfilTest() {
		consumer = new ProfilConsumer(new ProfilTestAdapter());
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
		consumer.insert(profil);
	}
	
	
	public void getList() {
		consumer.getList();
	}
	
	
	public void login() {
		consumer.login("pepito", "alma");
	}

	
	public void clear() {
		consumer.clear();
	}
	
}

package com.alma.telekocsi.dao.event;

import java.util.List;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;
import android.util.Log;

public class EventTestDAO {

	ProfilDAO profilDAO;
	EventDAO eventDAO;
	
	Profil profilFrom;
	Profil profilTo;	
	
	public EventTestDAO() {
		profilDAO = new ProfilDAO();
		eventDAO = new EventDAO();
		
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
		
		Log.i(EventTestDAO.class.getSimpleName(), "insert profil conducteur : " + profilFrom);		

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
		
		Log.i(EventTestDAO.class.getSimpleName(), "insert profil passager : " + profilTo);			
		
	}
	
	
	public void insert() {	
		
		Event event = new Event();
		event.setIdProfilFrom(profilFrom.getId());
		event.setIdProfilTo(profilTo.getId());
		event.setTypeEvent(1);
		event.setDescription("Appel en absence");
		event.setEtat("NP"); // pour non présenté
		event.setDateEvent("26/12/2010");
		event.setHeureEvent("08:00");

		eventDAO.insert(event);
	}
	
	
	public void getList() {
		List<Event> list = eventDAO.getListFrom(profilFrom.getId());
		int cpt1 = 0;
		Log.i(EventTestDAO.class.getSimpleName(), "list des events from ");
		for (Event event : list) {
			Log.i(EventTestDAO.class.getSimpleName(), "Event (" + (++cpt1) + ") : " + event.toString());
		}
		
		list = eventDAO.getListTo(profilTo.getId());
		cpt1 = 0;
		Log.i(EventTestDAO.class.getSimpleName(), "list des events to ");
		for (Event event : list) {
			Log.i(EventTestDAO.class.getSimpleName(), "Event (" + (++cpt1) + ") : " + event.toString());
		}
	}

	
	public void clear() {
		Integer nb = eventDAO.clear();
		Log.i(EventTestDAO.class.getSimpleName(), "clear events : " + nb);
	}
	
}

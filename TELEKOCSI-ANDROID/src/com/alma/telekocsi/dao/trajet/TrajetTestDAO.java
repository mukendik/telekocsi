package com.alma.telekocsi.dao.trajet;

import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

import android.util.Log;


public class TrajetTestDAO {

	ProfilDAO profilDAO;
	ItineraireDAO itineraireDAO;
	TrajetDAO trajetDAO;
	TrajetLigneDAO trajetLigneDAO;
	
	Profil profilConducteur;
	Profil profilPassager;
	Itineraire itineraire;
	Trajet trajet;
	
	
	public TrajetTestDAO() {
		profilDAO = new ProfilDAO();
		itineraireDAO = new ItineraireDAO();
		trajetDAO = new TrajetDAO();
		trajetLigneDAO = new TrajetLigneDAO();
		
		profilConducteur = new Profil();
		profilConducteur.setNom("BELIN");
		profilConducteur.setPrenom("Bruno");
		profilConducteur.setAnimaux("N");
		profilConducteur.setClassementMoyen(4);
		profilConducteur.setClasseVehicule(3);
		profilConducteur.setConnecte(false);
		profilConducteur.setDateNaissance("10/06/1965");
		profilConducteur.setDetours("N");
		profilConducteur.setDiscussion("O");
		profilConducteur.setEmail("bbelin.sigal@gmail.com");
		profilConducteur.setFumeur("N");
		profilConducteur.setMotDePasse("alma");
		profilConducteur.setMusique("O");
		profilConducteur.setPathPhoto("");
		profilConducteur.setPointsDispo(10);
		profilConducteur.setPseudo("pepito");
		profilConducteur.setSexe("M");
		profilConducteur.setTelephone("02.51.64.97.07");
		profilConducteur.setTypeProfil("C");
		profilConducteur.setTypeProfilHabituel("C");
		profilConducteur.setVehicule("207");		
		profilConducteur = profilDAO.insert(profilConducteur);
		
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert profil conducteur : " + profilConducteur);		

		profilPassager = new Profil();
		profilPassager.setNom("TUPRIN");
		profilPassager.setPrenom("Sylvie");
		profilPassager.setAnimaux("N");
		profilPassager.setClassementMoyen(4);
		profilPassager.setClasseVehicule(3);
		profilPassager.setConnecte(false);
		profilPassager.setDateNaissance("10/06/1965");
		profilPassager.setDetours("N");
		profilPassager.setDiscussion("O");
		profilPassager.setEmail("sylvie.belin@gmail.com");
		profilPassager.setFumeur("N");
		profilPassager.setMotDePasse("alma");
		profilPassager.setMusique("O");
		profilPassager.setPathPhoto("");
		profilPassager.setPointsDispo(10);
		profilPassager.setPseudo("turpin");
		profilPassager.setSexe("M");
		profilPassager.setTelephone("02.51.64.87.07");
		profilPassager.setTypeProfil("C");
		profilPassager.setTypeProfilHabituel("C");
		profilPassager.setVehicule("POLO");		
		profilPassager = profilDAO.insert(profilPassager);
		
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert profil passager : " + profilPassager);			
		
		itineraire = new Itineraire();
		itineraire.setAutoroute(true);
		itineraire.setCommentaire("Universite de Nantes - Pôle scientifique");
		itineraire.setFrequenceTrajet("LMMJV");
		itineraire.setHoraireArrivee("09H00");
		itineraire.setHoraireDepart("07H45");
		itineraire.setLieuDepart("LES HERBIERS");
		itineraire.setLieuDestination("NANTES");
		itineraire.setNbrePoint(3);
		itineraire.setPlaceDispo(3);
		itineraire.setIdProfil(profilConducteur.getId());
		itineraire.setVariableDepart("5");
		
		itineraire = itineraireDAO.insert(itineraire);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert : " + itineraire);
	}
	
	
	public void insert() {	
		
		trajet = new Trajet();
		trajet.setAutoroute(itineraire.isAutoroute());
		trajet.setCommentaire(itineraire.getCommentaire());
		trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
		trajet.setHoraireArrivee(itineraire.getHoraireArrivee());
		trajet.setHoraireDepart(itineraire.getHoraireDepart());
		trajet.setLieuDepart(itineraire.getLieuDepart());
		trajet.setLieuDestination(itineraire.getLieuDestination());
		trajet.setNbrePoint(itineraire.getNbrePoint());
		trajet.setPlaceDispo(itineraire.getPlaceDispo());
		trajet.setIdProfilConducteur(itineraire.getIdProfil());
		trajet.setIdItineraire(itineraire.getId());
		trajet.setVariableDepart(itineraire.getVariableDepart());
		trajet.setDateTrajet("26/12/2010");
		trajet.setSoldePlaceDispo(trajet.getPlaceDispo());
		trajet = trajetDAO.insert(trajet);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert trajet : " + trajet);
		
		TrajetLigne trajetLigne = new TrajetLigne();
		trajetLigne.setIdProfilPassager(profilPassager.getId());
		trajetLigne.setIdTrajet(trajet.getId());
		trajetLigne.setNbrePoint(2);
		trajetLigne.setPlaceOccupee(1);
		trajetLigne = trajetLigneDAO.insert(trajetLigne);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert ligne trajet : " + trajetLigne);
		
		trajetLigne = new TrajetLigne();
		trajetLigne.setIdProfilPassager(profilPassager.getId());
		trajetLigne.setIdTrajet(trajet.getId());
		trajetLigne.setNbrePoint(3);
		trajetLigne.setPlaceOccupee(1);
		trajetLigne = trajetLigneDAO.insert(trajetLigne);
		Log.i(TrajetTestDAO.class.getSimpleName(), "insert ligne trajet : " + trajetLigne);
	}

	
	public void getListIdPassagers() {
		List<String> listPassagers = trajetLigneDAO.getListPassagers(trajet.getId());
		int cpt1 = 0;
		Log.i(TrajetTestDAO.class.getSimpleName(), "id des passagers : ");
		for (String id : listPassagers) {
			Log.i(TrajetTestDAO.class.getSimpleName(), "Id passager (" + (++cpt1) + ") : " + id);
		}
	}	
	
	
	public void getList() {
		List<Trajet> list = trajetDAO.getList(profilConducteur.getId());
		int cpt1 = 0;
		Log.i(TrajetTestDAO.class.getSimpleName(), "list des trajets");
		for (Trajet trajet : list) {
			Log.i(TrajetTestDAO.class.getSimpleName(), "Trajet (" + (++cpt1) + ") : " + trajet.toString());

			int cpt2 = 0;
			List<TrajetLigne> listLignes = trajetLigneDAO.getList(trajet.getId());
			for (TrajetLigne trajetLigne : listLignes) {
				Log.i(TrajetTestDAO.class.getSimpleName(), "ligne trajet (" + (++cpt2) + ") : " + trajetLigne.toString());
			}
		}
	}

	
	public void clear() {
		Integer nb = trajetLigneDAO.clear();
		Log.i(TrajetTestDAO.class.getSimpleName(), "clear lignes trajet : " + nb);
		
		nb = trajetDAO.clear();
		Log.i(TrajetTestDAO.class.getSimpleName(), "clear trajet : " + nb);
	}
	
}

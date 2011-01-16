package com.telekocsi.server.trajet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.api.NotFoundException;
import com.telekocsi.server.itineraire.Itineraire;
import com.telekocsi.server.profil.Profil;
import com.telekocsi.server.util.Tools;

@Path("/trajet")
@Produces("application/json")
@Consumes("application/json")
public class TrajetService {
	
	private static final Logger log = Logger.getLogger(TrajetService.class.getName());

	/**
	 * Mise a jour d'un trajet par son id
	 * @param id
	 * @param trajet
	 * @return Trajet actualise
	 */
	@POST
	@Path("{id}")
	public Trajet update(
			@PathParam("id") String id,
			Trajet trajet) {
		
		log.info("Mise a jour du trajet d'id : " + id);
		
		if (trajet == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Trajet persistedTrajet = em.getReference(Trajet.class, id);
		
		if (persistedTrajet == null) {
			throw new NotFoundException();
		}
		
		persistedTrajet.setAutoroute(trajet.isAutoroute());
		persistedTrajet.setCommentaire(trajet.getCommentaire());
		persistedTrajet.setFrequenceTrajet(trajet.getFrequenceTrajet());
		persistedTrajet.setHoraireArrivee(trajet.getHoraireArrivee());
		persistedTrajet.setHoraireDepart(trajet.getHoraireDepart());
		persistedTrajet.setVariableDepart(trajet.getVariableDepart());
		persistedTrajet.setLieuDepart(trajet.getLieuDepart());
		persistedTrajet.setLieuPassage1(trajet.getLieuPassage1());
		persistedTrajet.setLieuPassage2(trajet.getLieuPassage2());
		persistedTrajet.setLieuDestination(trajet.getLieuDestination());
		persistedTrajet.setNbrePoint(trajet.getNbrePoint());
		persistedTrajet.setPlaceDispo(trajet.getNbrePoint());
		persistedTrajet.setIdItineraire(trajet.getIdItineraire());
		persistedTrajet.setDateTrajet(trajet.getDateTrajet());
		persistedTrajet.setIdProfilConducteur(trajet.getIdProfilConducteur());
		persistedTrajet.setSoldePlaceDispo(trajet.getSoldePlaceDispo());
		persistedTrajet.setEtat(trajet.getEtat());
		
		em.getTransaction().begin();
		em.merge(persistedTrajet);
		em.getTransaction().commit();
		
		return persistedTrajet;
	}

	/**
	 * Recupere un trajet par son id
	 * @param Id du trajet
	 * @return Trajet
	 */
	@GET
	@Path("{id}")
	public Trajet get(@PathParam("id") String id) {
		
		log.info("Recuperation du trajet d'id : " + id);
		
		Trajet persistedTrajet = Tools.getEntityManager().getReference(Trajet.class, id);
		
		if (persistedTrajet == null) {
			throw new NotFoundException();
		}
		
		return persistedTrajet;
	}

	/**
	 * Recuperation de la liste des trajets
	 * @return Liste des trajets
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Trajet> list() {
		
		log.info("Recuperation de tous les trajets");
		
		EntityManager em = Tools.getEntityManager();
		List<Trajet> trajets = em.createQuery("SELECT i FROM Trajet i").getResultList();
		
		return trajets;
	}
	
	/**
	 * Recuperation de la liste de tous les trajets pour un profil
	 * @return liste des itineraires
	 */
	@GET
	@Path("/profil/{idProfil}")
	@SuppressWarnings("unchecked")
	public List<Trajet> list(@PathParam("idProfil") String idProfil) {
		
		log.info("Recuperation des trajets pour le profil conducteur : " + idProfil);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT t FROM Trajet t where t.idProfilConducteur=:param1 and ((t.etat=:param2) or (t.etat=:param3))");
		query.setParameter("param1", idProfil);
		query.setParameter("param2", Trajet.ETAT_DISPO);
		query.setParameter("param3", Trajet.ETAT_ACTIF);
		List<Trajet> trajets = query.getResultList();
		return trajets;
	}

	/**
	 * Recuperation de la liste des Trajets pour un profil et pour un etat particulier
	 * @return liste des itineraires
	 */
	@GET
	@Path("/trajetsForEtat/{idProfil}/{etat}")
	@SuppressWarnings("unchecked")
	public List<Trajet> list(@PathParam("idProfil") String idProfil, @PathParam("etat") int etat) {
		
		log.info("Recuperation des trajets pour le profil conducteur : " + idProfil + " avec etat : " + etat);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT t FROM Trajet t where t.idProfilConducteur=:param1 and t.etat=:param2");
		query.setParameter("param1", idProfil);
		query.setParameter("param2", etat);
		List<Trajet> trajets = query.getResultList();
		return trajets;
	}
	
	
	/**
	 * Recuperation de la liste des Trajets disponibles
	 * Préférable d'utiliser la methode de recherche a partir d'un modele
	 * @param Lieu de depart
	 * @param Lieu d'arrivee
	 * @param Date depart
	 * @return liste des itineraires
	 * @deprecated
	 */
	@GET
	@Path("/trajetDispo/{lieuDepart}/{lieuArrivee}/{date}")
	@SuppressWarnings("unchecked")
	public List<Trajet> getTrajetDispo(@PathParam("lieuDepart") String lieuDepart, 
			@PathParam("lieuArrivee") String lieuArrivee,
			@PathParam("date") String date) {
		
		String dateRef = date.replaceAll("-", "/");
		
		log.info("Recuperation des trajets disponibles pour le " + dateRef);
		
		
		EntityManager em = Tools.getEntityManager();
		
		StringBuilder sb = new StringBuilder("SELECT t FROM Trajet t");
		sb.append(" where t.lieuDepart=:param1");
		sb.append(" and t.lieuDestination=:param2");
		sb.append(" and t.dateTrajet=:param3");
		sb.append(" and t.soldePlaceDispo > 0");
		sb.append(" and t.etat=" + Trajet.ETAT_DISPO);
		
		Query query = em.createQuery(sb.toString());
		query.setParameter("param1", lieuDepart);
		query.setParameter("param2", lieuArrivee);
		query.setParameter("param3", dateRef);
		
		List<Trajet> trajets = query.getResultList();
		
		return trajets;
	}

	
	/**
	 * Recuperation de la liste des Trajets disponibles ou actifs
	 * ayant encore des places disponibles
	 * @param Trajet Model
	 * @return liste des itineraires correspondant au modele avec places disponibles
	 */
	@PUT
	@Path("/trajetDispo")
	@SuppressWarnings("unchecked")
	public List<Trajet> getTrajets(Trajet trajetModel) {
		
		log.info("Recuperation des trajets correspondants au model ");
		
		EntityManager em = Tools.getEntityManager();
		
		StringBuilder sb = new StringBuilder("SELECT t FROM Trajet t");
		sb.append(" where t.lieuDepart=:param1");
		sb.append(" and t.lieuDestination=:param2");
		sb.append(" and t.dateTrajet=:param3");
		sb.append(" and ((t.etat=" + Trajet.ETAT_DISPO + ") OR (t.etat=" + Trajet.ETAT_ACTIF + "))");
		sb.append(" and t.soldePlaceDispo > 0");	
		
		Query query = em.createQuery(sb.toString());
		query.setParameter("param1", trajetModel.getLieuDepart());
		query.setParameter("param2", trajetModel.getLieuDestination());
		query.setParameter("param3", trajetModel.getDateTrajet());
		
		List<Trajet> trajets = query.getResultList();
		
		return trajets;
	}

	/**
	 * Supprime un trajet par son id
	 * @param id du trajet a supprimer
	 * @return id trajet supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression du trajet d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Trajet persistedTrajet = em.getReference(Trajet.class, id);
		
		if (persistedTrajet == null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedTrajet);
		em.getTransaction().commit();
		
		return id;
	}
	
	/**
	 * Supprime tous les trajets
	 * @return nbre de trajets supprimés
	 */
	@DELETE
	@Path("/clear")
	public int clear() {
		
		log.info("Suppression de tous les trajets");
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("DELETE FROM Trajet");
		int result = query.executeUpdate();
		return result;
	}

	/**
	 * Ajoute un trajet
	 * @param Transaction a ajouter
	 * @return Trajet venant d'etre cree
	 */
	@PUT
	public Trajet add(Trajet trajet) {
		log.info("Ajout d'un trajet");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(trajet);
		em.getTransaction().commit();
		
		return trajet;
	}

	
	/**
	 * Activation d'un trajet
	 * @return true si operation reussie
	 */
	@GET
	@Path("/activate/{idTrajet}")
	public Integer activate(@PathParam("idTrajet") String idTrajet) {
		
		log.info("Activation du trajet d'id : " + idTrajet);
		
		if (idTrajet == null) {
			return 0;
		}
		
		EntityManager em = Tools.getEntityManager();
		Trajet persistedTrajet = em.getReference(Trajet.class, idTrajet);
		
		if (persistedTrajet == null) {
			return 0;
		}
		
		if (persistedTrajet.getEtat() != Trajet.ETAT_DISPO) {
			return 0;
		}
		
		persistedTrajet.setEtat(Trajet.ETAT_ACTIF);
		
		em.getTransaction().begin();
		em.merge(persistedTrajet);
		em.getTransaction().commit();
		
		return 1;
	}
	
	
	/**
	 * Desactivation d'un trajet (actif ou disponible)
	 * @return true si operation reussie
	 */
	@GET
	@Path("/desactivate/{idTrajet}")
	public Integer desactivate(@PathParam("idTrajet") String idTrajet) {
		
		log.info("Desactivation du trajet d'id : " + idTrajet);
		
		if (idTrajet == null) {
			return 0;
		}
		
		EntityManager em = Tools.getEntityManager();
		Trajet persistedTrajet = em.getReference(Trajet.class, idTrajet);
		
		if (persistedTrajet == null) {
			return 0;
		}
		
		if (persistedTrajet.getEtat() == Trajet.ETAT_FIN) {
			return 0;
		}
		
		persistedTrajet.setEtat(Trajet.ETAT_FIN);
		
		em.getTransaction().begin();
		em.merge(persistedTrajet);
		em.getTransaction().commit();
		
		return 1;
	}
	
	/**
	 * Generation automatique des trajets habituels des conducteurs pour une date
	 * Attention : date au format dd-MM-yyyy
	 * @return Nombre de trajets generes
	 */
	@GET
	@Path("/generate/{date}")
	public Integer generate(@PathParam("date") String date) {

		log.info("Generation des trajets habituels pour le : " + date);

		int cpt = 0;
		String dateTrav = date.replaceAll("-", "/");
		SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy");
		Date dateRef = null;
		try {
			dateRef = s1.parse(dateTrav);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dateRef);
		int jour = calendar.get(calendar.DAY_OF_WEEK) - 2;
		if (jour < 0) 
			jour = 6;
		
		// LMMJVSD OOOOOOO

		EntityManager em = Tools.getEntityManager();
		List<Itineraire> itineraires = em.createQuery("SELECT i FROM Itineraire i where i.placeDispo > 0").getResultList();

		for (Itineraire itineraire : itineraires) {
			if (itineraire.getPlaceDispo() >  0) {
				String chaineJour = itineraire.getFrequenceTrajet() + "NNNNNNN";
				if (chaineJour.charAt(jour) == 'O') {
					
					Query query  = em.createQuery("SELECT p FROM Profil p where p.id=:param");
					query.setParameter("param", itineraire.getIdProfil());
					
					Profil profil = null;
					try {
						profil = (Profil)query.getSingleResult();
					} catch (Exception e) {
					}
					
					if ((profil != null) && (profil.getTypeProfil() != null) && profil.getTypeProfil().equals("C")) {
					
						Trajet trajet = new Trajet();
						trajet.setAutoroute(itineraire.isAutoroute());
						trajet.setCommentaire(itineraire.getCommentaire());
						trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
						trajet.setHoraireArrivee(itineraire.getHoraireArrivee());
						trajet.setHoraireDepart(itineraire.getHoraireDepart());
						trajet.setLieuDepart(itineraire.getLieuDepart());
						trajet.setLieuPassage1(itineraire.getLieuPassage1());
						trajet.setLieuPassage2(itineraire.getLieuPassage2());
						trajet.setLieuDestination(itineraire.getLieuDestination());
						trajet.setNbrePoint(itineraire.getNbrePoint());
						trajet.setPlaceDispo(itineraire.getPlaceDispo());
						trajet.setIdProfilConducteur(itineraire.getIdProfil());
						trajet.setIdItineraire(itineraire.getId());
						trajet.setVariableDepart(itineraire.getVariableDepart());
						trajet.setDateTrajet(dateTrav);
						trajet.setSoldePlaceDispo(trajet.getPlaceDispo());
						trajet.setEtat(Trajet.ETAT_DISPO);
						add(trajet);
						cpt++;
					}
				}
			}
		}		
		return cpt;
	}	
	
}

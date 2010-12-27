package com.telekocsi.server.trajet;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.api.NotFoundException;
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
		persistedTrajet.setLieuDestination(trajet.getLieuDestination());
		persistedTrajet.setNbrePoint(trajet.getNbrePoint());
		persistedTrajet.setPlaceDispo(trajet.getNbrePoint());
		persistedTrajet.setIdItineraire(trajet.getIdItineraire());
		persistedTrajet.setDateTrajet(trajet.getDateTrajet());
		persistedTrajet.setIdProfilConducteur(trajet.getIdProfilConducteur());
		persistedTrajet.setSoldePlaceDispo(trajet.getSoldePlaceDispo());
		
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
		
		log.info("Recuperation des trajets");
		
		EntityManager em = Tools.getEntityManager();
		List<Trajet> trajets = em.createQuery("SELECT i FROM Trajet i").getResultList();
		
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
}

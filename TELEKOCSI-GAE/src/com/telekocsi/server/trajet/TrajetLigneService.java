package com.telekocsi.server.trajet;

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
import com.telekocsi.server.util.Tools;

@Path("/trajet/ligne")
@Produces("application/json")
@Consumes("application/json")
public class TrajetLigneService {
	
	private static final Logger log = Logger.getLogger(TrajetLigneService.class.getName());
	

	/**
	 * Mise a jour d'un trajet par son id
	 * @param id
	 * @param trajetLigne
	 * @return TrajetLigne actualise
	 */
	@POST
	@Path("{id}")
	public TrajetLigne update(
			@PathParam("id") String id,
			TrajetLigne trajetLigne) {
		
		log.info("Mise a jour du trajet d'id : " + id);
		
		if (trajetLigne == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		TrajetLigne persistedTrajetLigne = em.getReference(TrajetLigne.class, id);
		
		if (persistedTrajetLigne == null) {
			throw new NotFoundException();
		}
		
		persistedTrajetLigne.setIdProfilPassager(trajetLigne.getIdProfilPassager());
		persistedTrajetLigne.setIdTrajet(trajetLigne.getIdTrajet());
		persistedTrajetLigne.setNbrePoint(trajetLigne.getNbrePoint());
		persistedTrajetLigne.setPlaceOccupee(trajetLigne.getPlaceOccupee());
		
		em.getTransaction().begin();
		em.merge(persistedTrajetLigne);
		em.getTransaction().commit();
		
		return persistedTrajetLigne;
	}

	/**
	 * Recupere un trajetLigne par son id
	 * @param Id du trajetLigne
	 * @return TrajetLigne
	 */
	@GET
	@Path("{id}")
	public TrajetLigne get(@PathParam("id") String id) {
		
		log.info("Recuperation du trajetLigne d'id : " + id);
		
		TrajetLigne persistedTrajetLigne = Tools.getEntityManager().getReference(TrajetLigne.class, id);
		
		if (persistedTrajetLigne == null) {
			throw new NotFoundException();
		}
		
		return persistedTrajetLigne;
	}

	/**
	 * Recuperation de la liste des trajetLignes
	 * @return Liste des trajetLignes
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<TrajetLigne> list() {
		
		log.info("Recuperation des trajetLignes");
		
		EntityManager em = Tools.getEntityManager();
		List<TrajetLigne> trajetLignes = em.createQuery("SELECT i FROM TrajetLigne i").getResultList();
		
		return trajetLignes;
	}
	
	
	/**
	 * Recuperation des lignes pour un trajet
	 * @return liste des lignes associees au trajet
	 */
	@GET
	@Path("/trajet/{idTrajet}")
	@SuppressWarnings("unchecked")
	public List<TrajetLigne> list(@PathParam("idTrajet") String idTrajet) {
		
		log.info("Recuperation des lignes pour le trajet : " + idTrajet);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT tl FROM TrajetLigne tl where tl.idTrajet=:param");
		query.setParameter("param", idTrajet);
		List<TrajetLigne> trajetLignes = query.getResultList();
		return trajetLignes;
	}

	
	/**
	 * Supprime un trajetLigne par son id
	 * @param id du trajetLigne a supprimer
	 * @return id trajetLigne supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression du trajetLigne d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		TrajetLigne persistedTrajetLigne = em.getReference(TrajetLigne.class, id);
		
		if (persistedTrajetLigne == null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedTrajetLigne);
		em.getTransaction().commit();
		
		return id;
	}
	
	
	/**
	 * Supprime toutes les lignes trajets
	 * @return nbre de lignes trajets supprimées
	 */
	@DELETE
	@Path("/clear")
	public int clear() {
		
		log.info("Suppression de toutes les lignes trajets");
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("DELETE FROM TrajetLigne");
		int result = query.executeUpdate();
		return result;
	}
	

	/**
	 * Ajoute un trajetLigne
	 * @param TrajetLigne a ajouter
	 * @return TrajetLigne venant d'etre cree
	 */
	@PUT
	public TrajetLigne add(TrajetLigne trajetLigne) {
		log.info("Ajout d'un trajetLigne");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(trajetLigne);
		em.getTransaction().commit();
		
		return trajetLigne;
	}
	
	/**
	 * Recuperation de la liste des id passagers pour un itineraire
	 * @return liste des passager integres dans un trajet
	 */
	@GET
	@Path("/passagers/{idTrajet}")
	public List<String> getPassagers(@PathParam("idTrajet") String idTrajet) {
		
		log.info("Recuperation des id des passagers pour le trajet : " + idTrajet);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT tl.idProfilPassager FROM TrajetLigne tl where tl.idTrajet=:param");
		query.setParameter("param", idTrajet);
		List<String> idProfils = query.getResultList();
		return idProfils;
	}
	
	
	/**
	 * Recuperation d'un TrajetLigne pour un trajet donné et un passagé donné
	 */
	@GET
	@Path("/passagers/{idTrajet}/{idPassager}")
	public TrajetLigne getTrajetLigne(@PathParam("idTrajet") String idTrajet, @PathParam("idPassager") String idPassager) {
		
		TrajetLigne tl = null;
		log.info("Recuperation d'un trajet ligne pour le trajet : " + idTrajet + " et le passager " + idPassager);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT tl FROM TrajetLigne tl where tl.idTrajet=:param1 and idProfilPassager=:param2");
		query.setParameter("param1", idTrajet);
		query.setParameter("param2", idPassager);
		tl = (TrajetLigne) query.getSingleResult();

		return tl;
	}

	
	/**
	 * Recuperation liste des TrajetLignes pour un passagé donné
	 */
	@GET
	@Path("/passager/{idPassager}")
	public List<TrajetLigne> getTrajetLigneFromPassager(@PathParam("idPassager") String idPassager) {
		
		log.info("Recuperation des lignes de trajet actives pour le passager : " + idPassager);
		
		EntityManager em = Tools.getEntityManager();
		
		StringBuilder sql = new StringBuilder("SELECT tl from TrajetLigne tl");
		sql.append(" where tl.idProfilPassager = :param1");
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("param1", idPassager);
		List<TrajetLigne> tl = query.getResultList();

		return tl;
	}
}

package com.telekocsi.server.avis;

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

@Path("/avis")
@Produces("application/json")
@Consumes("application/json")
public class AvisService {
	
	private static final Logger log = Logger.getLogger(AvisService.class.getName());
	

	/**
	 * Mise a jour d'un avis par son id
	 * @param id
	 * @param avis
	 * @return Avis actualise
	 */
	@POST
	@Path("{id}")
	public Avis update(
			@PathParam("id") String id,
			Avis avis) {
		
		log.info("Mise a jour de la avis d'id : " + id);
		
		if (avis == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Avis persistedAvis= em.getReference(Avis.class, id);
		
		if (persistedAvis == null) {
			throw new NotFoundException();
		}
		
		persistedAvis.setIdProfilFrom(avis.getIdProfilFrom());
		persistedAvis.setIdProfilTo(avis.getIdProfilTo());
		persistedAvis.setClassement(avis.getClassement());
		persistedAvis.setCommentaire(avis.getCommentaire());
		persistedAvis.setChecked(avis.isChecked());
		persistedAvis.setEtat(avis.getEtat());
		persistedAvis.setDateAvis(avis.getDateAvis());
		persistedAvis.setHeureAvis(avis.getHeureAvis());
		
		em.getTransaction().begin();
		em.merge(persistedAvis);
		em.getTransaction().commit();
		
		return persistedAvis;
	}

	/**
	 * Recupere un avis par son id
	 * @param Id du avis
	 * @return Avis
	 */
	@GET
	@Path("{id}")
	public Avis get(@PathParam("id") String id) {
		
		log.info("Recuperation du avis d'id : " + id);
		
		Avis persistedAvis= Tools.getEntityManager().getReference(Avis.class, id);
		
		if (persistedAvis == null) {
			throw new NotFoundException();
		}
		
		return persistedAvis;
	}

	/**
	 * Recuperation de la liste des avis
	 * @return Liste des avis
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Avis> list() {
		
		log.info("Recuperation des avis");
		
		EntityManager em = Tools.getEntityManager();
		List<Avis> listAvis = em.createQuery("SELECT a FROM Avis a").getResultList();
		
		return listAvis;
	}

	/**
	 * Supprime un avis par son id
	 * @param id du avis a supprimer
	 * @return id avis supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression de l'avis d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Avis persistedAvis = em.getReference(Avis.class, id);
		
		if (persistedAvis == null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedAvis);
		em.getTransaction().commit();
		
		return id;
	}

	/**
	 * Ajoute un avis
	 * @param Event a ajouter
	 * @return Avisvenant d'etre cree
	 */
	@PUT
	public Avis add(Avis avis) {
		log.info("Ajout d'un avis");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(avis);
		em.getTransaction().commit();
		
		return avis;
	}
}

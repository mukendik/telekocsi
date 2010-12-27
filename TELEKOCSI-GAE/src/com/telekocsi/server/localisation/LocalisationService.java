package com.telekocsi.server.localisation;

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

@Path("/localisation")
@Produces("application/json")
@Consumes("application/json")
public class LocalisationService {
	
	private static final Logger log = Logger.getLogger(LocalisationService.class.getName());
	

	/**
	 * Mise a jour d'un localisation par son id
	 * @param id
	 * @param localisation
	 * @return Localisation actualise
	 */
	@POST
	@Path("{id}")
	public Localisation update(
			@PathParam("id") String id,
			Localisation localisation) {
		
		log.info("Mise a jour de la localisation d'id : " + id);
		
		if (localisation == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Localisation persistedLocalisation= em.getReference(Localisation.class, id);
		
		if (persistedLocalisation== null) {
			throw new NotFoundException();
		}
		
		persistedLocalisation.setIdProfil(localisation.getIdProfil());
		persistedLocalisation.setPointGPS(localisation.getPointGPS());
		persistedLocalisation.setDateLocalisation(localisation.getDateLocalisation());
		persistedLocalisation.setHeureLocalisation(localisation.getHeureLocalisation());
		
		em.getTransaction().begin();
		em.merge(persistedLocalisation);
		em.getTransaction().commit();
		
		return persistedLocalisation;
	}

	/**
	 * Recupere un localisation par son id
	 * @param Id du localisation
	 * @return Localisation
	 */
	@GET
	@Path("{id}")
	public Localisation get(@PathParam("id") String id) {
		
		log.info("Recuperation du localisation d'id : " + id);
		
		Localisation persistedLocalisation= Tools.getEntityManager().getReference(Localisation.class, id);
		
		if (persistedLocalisation== null) {
			throw new NotFoundException();
		}
		
		return persistedLocalisation;
	}

	/**
	 * Recuperation de la liste des localisations
	 * @return Liste des localisations
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Localisation> list() {
		
		log.info("Recuperation des localisations");
		
		EntityManager em = Tools.getEntityManager();
		List<Localisation> localisations = em.createQuery("SELECT l FROM Localisation l").getResultList();
		
		return localisations;
	}

	/**
	 * Supprime un localisation par son id
	 * @param id du localisation a supprimer
	 * @return id localisation supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression du localisation d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Localisation persistedLocalisation = em.getReference(Localisation.class, id);
		
		if (persistedLocalisation== null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedLocalisation);
		em.getTransaction().commit();
		
		return id;
	}
	
	/**
	 * Supprime tous les localisations
	 * @return nbre de localisations supprimées
	 */
	@DELETE
	@Path("/clear")
	public int clear() {
		
		log.info("Suppression de toutes les localisations");
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("DELETE FROM Localisation");
		int result = query.executeUpdate();
		return result;
	}

	/**
	 * Ajoute un localisation
	 * @param Avis a ajouter
	 * @return Localisationvenant d'etre cree
	 */
	@PUT
	public Localisation add(Localisation localisation) {
		log.info("Ajout d'un localisation");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(localisation);
		em.getTransaction().commit();
		
		return localisation;
	}
}

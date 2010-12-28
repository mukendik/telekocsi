package com.telekocsi.server.event;

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


@Path("/event")
@Produces("application/json")
@Consumes("application/json")
public class EventService {
	
	private static final Logger log = Logger.getLogger(EventService.class.getName());
	

	/**
	 * Mise a jour d'un event par son id
	 * @param id
	 * @param event
	 * @return Event actualise
	 */
	@POST
	@Path("{id}")
	public Event update(
			@PathParam("id") String id,
			Event event) {
		
		log.info("Mise a jour de l'event d'id : " + id);
		
		if (event == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Event persistedEvent= em.getReference(Event.class, id);
		
		if (persistedEvent == null) {
			throw new NotFoundException();
		}
		
		persistedEvent.setIdProfilFrom(event.getIdProfilFrom());
		persistedEvent.setIdProfilTo(event.getIdProfilTo());
		persistedEvent.setTypeEvent(event.getTypeEvent());
		persistedEvent.setDescription(event.getDescription());
		persistedEvent.setEtat(event.getEtat());
		persistedEvent.setDateEvent(event.getDateEvent());
		persistedEvent.setHeureEvent(event.getHeureEvent());
		
		em.getTransaction().begin();
		em.merge(persistedEvent);
		em.getTransaction().commit();
		
		return persistedEvent;
	}

	/**
	 * Recupere un event par son id
	 * @param Id du event
	 * @return Event
	 */
	@GET
	@Path("{id}")
	public Event get(@PathParam("id") String id) {
		
		log.info("Recuperation de l'event d'id : " + id);
		
		Event persistedEvent= Tools.getEntityManager().getReference(Event.class, id);
		
		if (persistedEvent == null) {
			throw new NotFoundException();
		}
		
		return persistedEvent;
	}

	/**
	 * Recuperation de la liste des events
	 * @return Liste des events
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Event> list() {
		
		log.info("Recuperation des events");
		
		EntityManager em = Tools.getEntityManager();
		List<Event> listEvents = em.createQuery("SELECT e FROM Event e").getResultList();
		
		return listEvents;
	}
	
	/**
	 * Recuperation de la liste des events pour un profil (from)
	 * @return liste des events
	 */
	@GET
	@Path("/profil/from/{idProfil}")
	@SuppressWarnings("unchecked")
	public List<Event> listFrom(@PathParam("idProfil") String idProfil) {
		
		log.info("Recuperation des events pour le profil from : " + idProfil);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT e FROM Event e where e.idProfilFrom=:param");
		query.setParameter("param", idProfil);
		List<Event> listEvents = query.getResultList();
		return listEvents;
	}

	
	/**
	 * Recuperation de la liste des events pour un profil (to)
	 * @return liste des events
	 */
	@GET
	@Path("/profil/to/{idProfil}")
	@SuppressWarnings("unchecked")
	public List<Event> listTo(@PathParam("idProfil") String idProfil) {
		
		log.info("Recuperation des events pour le profil to : " + idProfil);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT e FROM Event e where e.idProfilTo=:param");
		query.setParameter("param", idProfil);
		List<Event> listEvents = query.getResultList();
		return listEvents;
	}
	

	/**
	 * Supprime un event par son id
	 * @param id du event a supprimer
	 * @return id event supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression de l'event d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Event persistedEvent = em.getReference(Event.class, id);
		
		if (persistedEvent == null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedEvent);
		em.getTransaction().commit();
		
		return id;
	}
	
	/**
	 * Supprime tous les events
	 * @return nbre d'events supprimés
	 */
	@DELETE
	@Path("/clear")
	public int clear() {
		
		log.info("Suppression de tous les events");
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("DELETE FROM Event");
		int result = query.executeUpdate();
		return result;
	}

	/**
	 * Ajoute un event
	 * @param Event a ajouter
	 * @return Eventvenant d'etre cree
	 */
	@PUT
	public Event add(Event event) {
		log.info("Ajout d'un event");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(event);
		em.getTransaction().commit();
		
		return event;
	}
}

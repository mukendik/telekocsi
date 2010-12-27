package com.telekocsi.server.itineraire;

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

@Path("/itineraire")
@Produces("application/json")
@Consumes("application/json")
public class ItineraireService {
	
	private static final Logger log = Logger.getLogger(ItineraireService.class.getName());
	

	/**
	 * Mise a jour d'un itineraire par son id
	 * @param id
	 * @param itineraire
	 * @return
	 */
	@POST
	@Path("{id}")
	public Itineraire update(
			@PathParam("id") String id,
			Itineraire itineraire) {
		
		log.info("Mise a jour du itineraire d'id : " + id);
		
		if (itineraire == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Itineraire persistedItineraire = em.getReference(Itineraire.class, id);
		
		if (persistedItineraire == null) {
			throw new NotFoundException();
		}
		
		persistedItineraire.setAutoroute(itineraire.isAutoroute());
		persistedItineraire.setCommentaire(itineraire.getCommentaire());
		persistedItineraire.setFrequenceTrajet(itineraire.getFrequenceTrajet());
		persistedItineraire.setHoraireArrivee(itineraire.getHoraireArrivee());
		persistedItineraire.setHoraireDepart(itineraire.getHoraireDepart());
		persistedItineraire.setVariableDepart(itineraire.getVariableDepart());
		persistedItineraire.setLieuDepart(itineraire.getLieuDepart());
		persistedItineraire.setLieuDestination(itineraire.getLieuDestination());
		persistedItineraire.setNbrePoint(itineraire.getNbrePoint());
		persistedItineraire.setPlaceDispo(itineraire.getNbrePoint());
		persistedItineraire.setIdProfil(itineraire.getIdProfil());
		
		em.getTransaction().begin();
		em.merge(persistedItineraire);
		em.getTransaction().commit();
		
		return persistedItineraire;
	}

	/**
	 * Recupere un Itineraire par son id
	 * @param Id de l'itineraire
	 * @return Itineraire trouve
	 */
	@GET
	@Path("{id}")
	public Itineraire get(@PathParam("id") String id) {
		
		log.info("Recuperation du itineraire d'id : " + id);
		
		Itineraire persistedItineraire = Tools.getEntityManager().getReference(Itineraire.class, id);
		
		if (persistedItineraire == null) {
			throw new NotFoundException();
		}
		
		return persistedItineraire;
	}

	/**
	 * Recuperation de la liste des Itineraires
	 * @return liste des itineraires
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Itineraire> list() {
		
		log.info("Recuperation des itineraires");
		
		EntityManager em = Tools.getEntityManager();
		List<Itineraire> itineraires = em.createQuery("SELECT i FROM Itineraire i").getResultList();
		
		return itineraires;
	}

	
	/**
	 * Recuperation de la liste des Itineraires pour un profil
	 * @return liste des itineraires
	 */
	@GET
	@Path("/profil/{idProfil}")
	@SuppressWarnings("unchecked")
	public List<Itineraire> list(@PathParam("idProfil") String idProfil) {
		
		log.info("Recuperation des itineraires pour le profil : " + idProfil);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT i FROM Itineraire i where i.idProfil=:param");
		query.setParameter("param", idProfil);
		List<Itineraire> itineraires = query.getResultList();
		return itineraires;
	}
	
	/**
	 * Supprime un itineraire par son id
	 * @param Id de l'itineraire a suprimer
	 * @return id de l'itineraire supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression du itineraire d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Itineraire persistedItineraire = em.getReference(Itineraire.class, id);
		
		if (persistedItineraire == null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedItineraire);
		em.getTransaction().commit();
		
		return id;
	}
	
	/**
	 * Supprime tous les itineraires
	 * @return nbre d'itineraires supprimés
	 */
	@DELETE
	@Path("/clear")
	public int clear() {
		
		log.info("Suppression de tous les itineraires");
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("DELETE FROM Itineraire");
		int result = query.executeUpdate();
		return result;
	}	

	/**
	 * Ajoute un itineraire
	 * @param Itineraire a ajouter
	 * @return Itineraire cree
	 */
	@PUT
	public Itineraire add(Itineraire itineraire) {
		log.info("Ajout d'un itineraire");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(itineraire);
		em.getTransaction().commit();
		
		return itineraire;
	}
}

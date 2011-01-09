package com.telekocsi.server.profil;

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

@Path("/profil")
@Produces("application/json")
@Consumes("application/json")
public class ProfilService {
	
	private static final Logger log = Logger.getLogger(ProfilService.class.getName());
	

	/**
	 * Mise a jour d'un profil par son id
	 * @param id
	 * @param profil
	 * @return
	 */
	@POST
	@Path("{id}")
	public Profil update(
			@PathParam("id") String id,
			Profil profil) {
		
		log.info("Mise a jour du profil d'id : " + id);
		
		if (profil == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Profil persistedProfil = em.getReference(Profil.class, id);
		
		if (persistedProfil == null) {
			throw new NotFoundException();
		}
		
		persistedProfil.setNom(profil.getNom());
		persistedProfil.setPrenom(profil.getPrenom());
		persistedProfil.setAnimaux(profil.getAnimaux());
		persistedProfil.setClassementMoyen(profil.getClassementMoyen());
		persistedProfil.setClasseVehicule(profil.getClasseVehicule());
		persistedProfil.setConnecte(profil.isConnecte());
		persistedProfil.setDateNaissance(profil.getDateNaissance());
		persistedProfil.setDetours(profil.getDetours());
		persistedProfil.setDiscussion(profil.getDiscussion());
		persistedProfil.setEmail(profil.getEmail());
		persistedProfil.setFumeur(profil.getFumeur());
		persistedProfil.setMotDePasse(profil.getMotDePasse());
		persistedProfil.setMusique(profil.getMusique());
		persistedProfil.setPathPhoto(profil.getPathPhoto());
		persistedProfil.setPointsDispo(profil.getPointsDispo());
		persistedProfil.setPseudo(profil.getPseudo());
		persistedProfil.setSexe(profil.getSexe());
		persistedProfil.setTelephone(profil.getTelephone());
		persistedProfil.setTypeProfil(profil.getTypeProfil());
		persistedProfil.setTypeProfilHabituel(profil.getTypeProfilHabituel());
		persistedProfil.setVehicule(profil.getVehicule());
		
		em.getTransaction().begin();
		em.merge(persistedProfil);
		em.getTransaction().commit();
		
		return persistedProfil;
	}

	/**
	 * Recupere un profil par son id
	 * @param Id du profil
	 * @return Profil trouve
	 */
	@GET
	@Path("{id}")
	public Profil get(@PathParam("id") String id) {
		
		log.info("Recuperation du profil d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Profil persistedProfil = em.getReference(Profil.class, id);
		
		if (persistedProfil == null) {
			throw new NotFoundException();
		}
		
		return persistedProfil;
	}
	
	
	/**
	 * Recupere un profil par le pseudo
	 * @param Id du profil
	 * @return Profil trouve
	 */
	@GET
	@Path("/login/{pseudo}/{password}")
	public Profil login(@PathParam("pseudo") String pseudo, @PathParam("password") String password) {
		
		log.info("Login a partir du pseudo : " + pseudo);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT p FROM Profil p where p.pseudo=:param1 and p.motDePasse=:param2");
		query.setParameter("param1", pseudo);
		query.setParameter("param2", password);
		
		Profil profil = null;
		try {
			profil = (Profil)query.getSingleResult();	
		} catch (Exception e) {
			log.info("Profil inconnu  : " + pseudo + "/" + password);
		}
		
		return profil;
	}

	/**
	 * Recupere un profil par son email (on considere que l'email est unique)
	 * @param Id du profil
	 * @return Profil trouve
	 */
	@GET
	@Path("/email/{email}")
	public Profil getProfilByEmail(@PathParam("email") String email) {
		
		log.info("Login a partir de l'email : " + email);
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("SELECT p FROM Profil p where p.email=:param1");
		query.setParameter("param1", email);
		
		Profil profil = null;
		
		try {
			profil = (Profil)query.getSingleResult();	
		} catch (Exception e) {
			log.info("Profil inconnu pour l'email : " + email);
		}
		
		return profil;
	}	
	
	
	/**
	 * Recuperation de la liste des profils
	 * @return liste des profils
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Profil> list() {
		
		log.info("Recuperation des profils");
		
		EntityManager em = Tools.getEntityManager();
		List<Profil> profils = em.createQuery("SELECT p FROM Profil p").getResultList();
		
		return profils;
	}

	/**
	 * Supprime un profil par son id
	 * @param Id du profil a supprimer
	 * @return id du profil supprimé
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression du profil d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Profil persistedProfil = em.getReference(Profil.class, id);
		
		if (persistedProfil == null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedProfil);
		em.getTransaction().commit();
		
		return id;
	}

	/**
	 * Supprime tous les profils
	 * @return nbre de profils supprimés
	 */
	@DELETE
	@Path("/clear")
	public int clear() {
		
		log.info("Suppression de tous les profils");
		
		EntityManager em = Tools.getEntityManager();
		Query query = em.createQuery("DELETE FROM Profil");
		int result = query.executeUpdate();
		return result;
	}
	
	/**
	 * Ajoute un profil
	 * @param Profil a ajouter
	 * @return profil cree
	 */
	@PUT
	public Profil add(Profil profil) {
		log.info("Ajout d'un profil");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(profil);
		em.getTransaction().commit();
		
		return profil;
	}
}

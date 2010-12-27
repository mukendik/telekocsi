package com.telekocsi.server.user;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.api.NotFoundException;

@Path("/user")
@Produces("application/json")
@Consumes("application/json")
public class UserService {
    
    private static final Logger log = Logger.getLogger(UserService.class.getName());
    
    private static final EntityManagerFactory ENTITY_MANAGER = 
        Persistence.createEntityManagerFactory("transactions-optional");
    
    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER.createEntityManager();
    }

    /**
     * Mise a jour d'un utilisateur par son id
     * @param id
     * @param user
     * @return
     */
    @POST
    @Path("{id}")
    public User update(
            @PathParam("id") String id, 
            User user) {
        log.info("Mise a jour du user d'id : " + id);
        
        if (user == null) {
            throw new IllegalArgumentException();
        }
        
        EntityManager em = getEntityManager();
        User persistedUser = em.getReference(User.class, id);
        
        if (persistedUser == null) {
            throw new NotFoundException();
        }
        
        persistedUser.setNom(user.getNom());
        persistedUser.setPrenom(user.getPrenom());

        em.getTransaction().begin();
        em.merge(persistedUser);
        em.getTransaction().commit();
        
        return persistedUser;
    }

    /**
     * Recupere un utilisateur par son id
     * @param deviceId
     * @return
     */
    @GET
    @Path("{id}")
    public User get(@PathParam("id") String id) {
        log.info("Recuperation du user d'id : " + id);
        
        EntityManager em = getEntityManager();
        User persistedUser = em.getReference(User.class, id);
        
        if (persistedUser == null) {
            throw new NotFoundException();
        }
        
        return persistedUser;
    }

    /**
     * Recuperation de la liste des utilisateurs
     * @param deviceId
     * @return
     */
    @GET
    @SuppressWarnings("unchecked")
    public List<User> list() {
        log.info("Recuperation des utilisateurs");
        
        EntityManager em = getEntityManager();
        List<User> users = em.createQuery("SELECT u FROM User u").getResultList();
        
        return users;
    }

    /**
     * Supprime un utilisateur par son id
     * @param deviceId
     * @return
     */
    @DELETE
    @Path("{id}")
    public String delete(@PathParam("id") String id) {
        log.info("Suppression du user d'id : " + id);
        
        EntityManager em = getEntityManager();
        User persistedUser = em.getReference(User.class, id);
        
        if (persistedUser == null) {
            throw new NotFoundException();
        }

        em.getTransaction().begin();
        em.remove(persistedUser);
        em.getTransaction().commit();
        
        return id;
    }

    /**
     * Ajoute un utilisateur
     * @param deviceId
     * @return
     */
    @PUT
    public User add(User user) {
        log.info("Ajout d'un utilisateur");
        
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        
        return user;
    }
    
}

package com.telekocsi.server.transaction;

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

@Path("/transaction")
@Produces("application/json")
@Consumes("application/json")
public class TransactionService {
	
	private static final Logger log = Logger.getLogger(TransactionService.class.getName());
	

	/**
	 * Mise a jour d'un transaction par son id
	 * @param id
	 * @param transaction
	 * @return Transaction actualise
	 */
	@POST
	@Path("{id}")
	public Transaction update(
			@PathParam("id") String id,
			Transaction transaction) {
		
		log.info("Mise a jour du transaction d'id : " + id);
		
		if (transaction == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = Tools.getEntityManager();
		Transaction persistedTransaction= em.getReference(Transaction.class, id);
		
		if (persistedTransaction== null) {
			throw new NotFoundException();
		}
		
		persistedTransaction.setIdTrajetLigne(transaction.getIdTrajetLigne());
		persistedTransaction.setIdProfilConducteur(transaction.getIdProfilConducteur());
		persistedTransaction.setIdProfilPassager(transaction.getIdProfilPassager());
		persistedTransaction.setDateTransaction(transaction.getDateTransaction());
		persistedTransaction.setHeureTransaction(transaction.getHeureTransaction());
		persistedTransaction.setPointEchange(transaction.getPointEchange());
		
		em.getTransaction().begin();
		em.merge(persistedTransaction);
		em.getTransaction().commit();
		
		return persistedTransaction;
	}

	/**
	 * Recupere un transaction par son id
	 * @param Id du transaction
	 * @return Transaction
	 */
	@GET
	@Path("{id}")
	public Transaction get(@PathParam("id") String id) {
		
		log.info("Recuperation du transaction d'id : " + id);
		
		Transaction persistedTransaction= Tools.getEntityManager().getReference(Transaction.class, id);
		
		if (persistedTransaction== null) {
			throw new NotFoundException();
		}
		
		return persistedTransaction;
	}

	/**
	 * Recuperation de la liste des transactions
	 * @return Liste des transactions
	 */
	@GET
	@SuppressWarnings("unchecked")
	public List<Transaction> list() {
		
		log.info("Recuperation des transactions");
		
		EntityManager em = Tools.getEntityManager();
		List<Transaction> transactions = em.createQuery("SELECT t FROM Transaction t").getResultList();
		
		return transactions;
	}

	/**
	 * Supprime un transaction par son id
	 * @param id du transaction a supprimer
	 * @return id transaction supprime
	 */
	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") String id) {
		
		log.info("Suppression du transaction d'id : " + id);
		
		EntityManager em = Tools.getEntityManager();
		Transaction persistedTransaction = em.getReference(Transaction.class, id);
		
		if (persistedTransaction== null) {
			throw new NotFoundException();
		}

		em.getTransaction().begin();
		em.remove(persistedTransaction);
		em.getTransaction().commit();
		
		return id;
	}

	/**
	 * Ajoute un transaction
	 * @param Localisation a ajouter
	 * @return Transactionvenant d'etre cree
	 */
	@PUT
	public Transaction add(Transaction transaction) {
		log.info("Ajout d'un transaction");
		
		EntityManager em = Tools.getEntityManager();
		em.getTransaction().begin();
		em.persist(transaction);
		em.getTransaction().commit();
		
		return transaction;
	}
}

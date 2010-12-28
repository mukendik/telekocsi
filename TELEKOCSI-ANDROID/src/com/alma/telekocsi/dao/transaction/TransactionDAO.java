package com.alma.telekocsi.dao.transaction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import com.alma.telekocsi.dao.AbstractTask;
import com.alma.telekocsi.dao.GAE;
import com.google.gson.reflect.TypeToken;


public class TransactionDAO {
	
	
	public TransactionDAO() {
	}
	
	
	/**
	 * Recuperation d'une liste de Transactions
	 */
	private class GetListTask extends AbstractTask<List<Transaction>>  {

		protected List<Transaction> handleJson(final InputStream in) {
			
			final Type collectionType = new TypeToken<List<Transaction>>(){}.getType();
			List<Transaction> transactions = null;
			synchronized (lock) {
				transactions = GAE.getGson().fromJson(new InputStreamReader(in), collectionType);
			}
			return transactions;
		}
	};
	
	
	/**
	 * Recuperation d'une transaction
	 * deja reference localement
	 */
	private class GetFicheTask extends AbstractTask<Transaction> {

		protected Transaction handleJson(final InputStream in) {
			Transaction transaction = null;
			synchronized (lock) {
				transaction = GAE.getGson().fromJson(new InputStreamReader(in), Transaction.class);
			}
			return transaction;
		}
	};
	
	
	/**
	 * Suppression d'une transaction
	 * reference localement
	 */
	private class GetIdTask extends AbstractTask<String> {

		protected String handleJson(final InputStream in) {
			String idTransaction = "";
			try {
				idTransaction =  new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
			} catch (Exception e) {}
			return idTransaction;
		}
	};

	
	/**
	 * Suppression de toutes les transactions
	 * reference localement
	 */
	private class GetNbFicheTask extends AbstractTask<Integer> {

		protected Integer handleJson(final InputStream in) {
			Integer nb = 0;
			try {
				String res = new BufferedReader(new InputStreamReader(in, GAE.getEncoding())).readLine();
				nb = Integer.parseInt(res);
			} catch (Exception e) {}
			
			return nb;
		}
	};
	
	
    /**
     * Creation d'un transaction
     * @param Transaction
     */
    public Transaction insert(Transaction transaction) {

    	// creation d'une requete de type POST
    	HttpPut request = new HttpPut(GAE.getTransactionEndPoint());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type User serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(transaction), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }

    
    public Transaction update(Transaction transaction) { 	

    	// creation d'une requete de type POST
    	// l'URL contient l'ID du Transaction a mettre a jour
    	HttpPost request = new HttpPost(
    			GAE.getTransactionEndPoint() + "/" + transaction.getId());
    	// precision du Content-Type
    	request.setHeader("Content-Type", GAE.getContentType());
    	synchronized (AbstractTask.lock) {
    		try {
    			// l'objet de type Transaction serialise est envoye dans le corps
    			// de la requete HTTP et encode en UTF-8
    			request.setEntity(new StringEntity(
    					GAE.getGson().toJson(transaction), GAE.getEncoding()));
    		} catch (UnsupportedEncodingException e) {}
    	}
    	return (new GetFicheTask()).execute(request);
    }
    
    
    /**
     * Action Supprimer
     * @param Transaction
     */
    public String delete(Transaction transaction) {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL correspondant au Transaction a supprimer
		return (new GetIdTask()).execute(new HttpDelete(
				GAE.getTransactionEndPoint() + "/" + transaction.getId()));
    }

    
    /**
     * Action Supprimer
     * @param Transaction
     */
    public Integer clear() {
    	// envoi d'une requete DELETE au serveur
    	// sur l'URL pour supprimer tous les Transactions
		return (new GetNbFicheTask()).execute(new HttpDelete(
				GAE.getTransactionEndPoint() + "/clear"));
    }    
    
    
    public List<Transaction> getListPassager(String idProfil) {
    	// recuperation des transactions from
		return (new GetListTask()).execute(new HttpGet(GAE.getTransactionEndPoint() + "/profil/passager/" + idProfil));
    }

    
    public List<Transaction> getListConducteur(String idProfil) {
    	// recuperation des transactions to
		return (new GetListTask()).execute(new HttpGet(GAE.getTransactionEndPoint() + "/profil/conducteur/" + idProfil));
    }    
}
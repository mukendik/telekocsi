/**
 * 
 */
package com.alma.telekocsi.session;

import com.alma.telekocsi.dao.profil.Profil;

/**
 * @author Rv
 * Contenir les informations relatifs a une session
 * d'utilisation et fournir un point d'entrer pour la notification.
 */
public interface Session {
	public int TRAJET = 0;
	public int EVENT = 1;
	public int LOCALISATION = 2;
	public int TRANSACTION = 3;
	public int AVIS = 4;
	public int PROFIL = 5;
	
	/**
	 * Accéder au profil courant.
	 * On vérifie que le profil n'a pas encore été crée, si oui on le charge
	 * @return Le profil actif et <code>null</code> si non existant
	 */
	public abstract Profil getActiveProfile();
	
	/**
	 * Remplacer ou créer le profil actif
	 * @param profile Le nouveau profil
	 */
	public abstract void saveProfile(Profil profile);

	/**
	 * 
	 * @return <code>false</code> Si l'utilisateur c'est déconnecté
	 */
	public abstract boolean isConnected();
	
	/**
	 * Se connecter à un profil
	 * Si les identifiants sont valide, le profil devient le profil actif
	 * @return <code>true</code> Si succès
	 */
	public abstract boolean login(String name,String password);
	
	/**
	 * 
	 * @return Deconnecter le profil actif
	 */
	public abstract boolean logout();
	
	/**
	 * Ecouter les l'évènement sur la session
	 * @param listener
	 */
	public abstract void addSessionListener(SessionListener listener);
	
	/**
	 *  Arrêter l'écoute
	 * @param listener
	 */
	public abstract void removeSessionListener(SessionListener listener);
	
   
    /**
     * Correspond a l'appel de TDAO.insert(object)
     * @param <T> Avis,Itineraire, Event,Transaction,Trajet ou Localisation
     * @param object L'instance
     */
    public abstract<T> void save(T object); 
    
    /**
     * 
     * @param <T> Avis,Itineraire, Transaction,Trajet, Event ou Localisation
     * @param object L'instance
     */
    public abstract<T> void update(T object);

    /**
     * 
     * @param <T> Avis,Itineraire, Transaction,Trajet Event ou Localisation
     * @param object L'instance
     */
    public abstract<T> void delete(T object);
    
    /**
     * Effacer toutes les données d'un type donné
     * @param type Session.AVIS, Session.TRANSACTION, Session.PROFIL, Session.EVENT, Session.LOCALISATION
     */
    public abstract void clear(int type);

}

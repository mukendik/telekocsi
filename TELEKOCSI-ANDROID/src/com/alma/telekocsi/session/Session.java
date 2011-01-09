/**
 * 
 */
package com.alma.telekocsi.session;

import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;

/**
 * @author Rv
 * Contenir les informations relatifs a une session
 * d'utilisation et fournir un point d'entrer pour la notification.
 */
public interface Session {
	/**
	 * Acceder au profil courant.
	 * On verifie que le profil n'a pas encore ete cree, si oui on le charge
	 * @return Le profil actif et <code>null</code> si non existant
	 */
	public abstract Profil getActiveProfile();
	
	/**
	 * Remplacer ou creer le profil actif
	 * @param profile Le nouveau profil
	 */
	public abstract void saveProfile(Profil profile);

	/**
	 * 
	 * @return Le traject actif ou null si pas de trajet
	 */
	public abstract Trajet getActiveRoute();
	
	/**
	 * Active un trajet
	 * @param route
	 */
	public abstract void activateRoute(Trajet route);
	
	/**
	 * Désactive la route actite
	 * Une fois appeler la méthode <code>getActiveRoute()</code> retourne <code>null</code>
	 */
	public abstract void deactivateRoute();
	
	/**
	 * Change le type du profil courant comme passager.
	 * A noter que le type du profil n'est pas sauvé en base.
	 */
	public abstract void switchToPassengerType();
	
	/**
	 * Change le type du profil courant comme conducteur.
	 * A noter que le type du profil n'est pas sauvé en base.
	 */
	public abstract void switchToDriverType();
	
	/**
	 * 
	 * @return <code>false</code> Si l'utilisateur c'est deconnecte
	 */
	public abstract boolean isConnected();
	
	/**
	 * Se connecter e un profil
	 * Si les identifiants sont valide, le profil devient le profil actif
	 * @return <code>true</code> Si succes
	 */
	public abstract boolean login(String name,String password);
	
	/**
	 * 
	 * @return Deconnecter le profil actif
	 */
	public abstract boolean logout();
	
	/**
	 * Ecouter les l'evenement sur la session
	 * @param listener
	 */
	public abstract void addSessionListener(SessionListener listener);
	
	/**
	 *  Arreter l'ecoute
	 * @param listener
	 */
	public abstract void removeSessionListener(SessionListener listener);
	
   
    /**
     * Correspond a l'appel de TDAO.insert(object)
     * @param <T> Avis,Itineraire, Event,Transaction,Trajet ou Localisation
     * @param object L'instance
     */
    public abstract<T> T save(T object); 
    
    /**
     * 
     * @param <T>
     * @param type
     * @param id
     * @return
     */
    public abstract<T> T find(Class<T> type,String id);
    
    /**
     * 
     * @param <T> Avis,Itineraire, Transaction,Trajet, Event ou Localisation
     * @param object L'instance
     */
    public abstract<T> T update(T object);

    /**
     * 
     * @param <T> Avis,Itineraire, Transaction,Trajet Event ou Localisation
     * @param object L'instance
     */
    public abstract<T> void delete(T object);
    
    /**
     * Effacer toutes les donnees d'un type donne
     * @param type Un des types de la DAO
     */
    public abstract<T> void clear(Class<T> type);

    /**
     * De meme que clear mais pour un profil particulier
     * @param <T>
     * @param type
     * @param profilId
     */
    public abstract<T> void clear(Class<T> type,String profilId);
    
    /**
     * 
     * @return Les trajets du profil actif
     */
    public abstract List<Trajet> getRoutes();
    
    /**
     * 
     * @return Les itineraires du profil actif
     */
    public abstract List<Itineraire> getItineraires();
    
    
    /**
     * Recherche les trajets actifs correspondants au modele transmis.
     * Les trajets selectionnes doivent avoir des places disponibles.
     * 
     * Elements devant etre renseignes dans le modele :
     * - lieuDepart
     * - lieuDestination
     * - dateTrajet
     * 
     * @param le modele permettant d'identifier les criteres de recherche
     * @return Les trajets actifs sur la base du modele transmis
     */
    public abstract List<Trajet> getTrajets(Trajet trajetModel);
	
}

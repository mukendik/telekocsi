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
	 * @return <code>true</code> Si l'utilisateur c'est déconnecte
	 */
	public abstract boolean isConnected();
	
	/**
	 * Se connecté à un profil
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
}

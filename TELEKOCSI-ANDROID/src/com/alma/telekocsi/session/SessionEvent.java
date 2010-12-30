package com.alma.telekocsi.session;

/**
 * 
 * @author Rv
 * Base des evenements pour ceux qui veulent être notifié
 *
 */
public interface SessionEvent {
	/**
	 * Type d'évenement
	 */
	public final int TRAJET_ACTIVATED = 0;
	
	/**
	 * Le type d'évènement
	 * @return
	 */
	public abstract int getEventType();
	
	/**
	 * L' origine de l'evenement
	 * @return
	 */
	public abstract Object getSource();
	
	/**
	 *  Les données
	 * @return
	 */
	public abstract Object getUserData();
	
}

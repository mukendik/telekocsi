package com.alma.telekocsi.session;

/**
 * 
 * @author Rv
 * Base des evenements pour ceux qui veulent atre notifia
 *
 */
public interface SessionEvent {
	/**
	 * Type d'evenement
	 */
	public final int ROUTE_ACTIVATED = 0;
	
	/**
	 * 
	 */
	public final int ROUTE_DISACTIVATED = 1;
	
	/**
	 * Evenement emis a la deconnection
	 */
	public final int LOGOUT = 1;
	
	/**
	 * Le type d'avanement
	 * @return
	 */
	public abstract int getEventType();
	
	/**
	 * L' origine de l'evenement
	 * @return
	 */
	public abstract Object getSource();
	
	/**
	 *  Les donnees
	 * @return
	 */
	public abstract Object getUserData();
	
}

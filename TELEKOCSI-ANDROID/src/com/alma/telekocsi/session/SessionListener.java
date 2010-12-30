package com.alma.telekocsi.session;

/**
 * 
 * @author Fatima
 *
 */
public interface SessionListener {

	/**
	 * 
	 * @param event
	 */
	public abstract void onEvent(SessionEvent event);
	
}

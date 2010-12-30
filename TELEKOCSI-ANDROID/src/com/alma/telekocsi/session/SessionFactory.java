package com.alma.telekocsi.session;

public class SessionFactory {
	private static Session theSession;
	
	/**
	 *  Point d'accès à la session courante
	 *  Si elle n'existe pas encore elle est crée
	 * @return La session courante
	 */
	public static Session getCurrentSession(){
		if(theSession==null){
			theSession = new SessionImpl();
		}
		return theSession;
	}
	
}

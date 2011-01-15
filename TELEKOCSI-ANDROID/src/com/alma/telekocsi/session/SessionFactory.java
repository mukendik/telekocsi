package com.alma.telekocsi.session;

import android.content.Context;

/**
 * 
 * @author Rv
 *
 */
public class SessionFactory {

	private static Session theSession;

	/**
	 *  Point d'acces a la session courante
	 *  Si elle n'existe pas encore elle est cree
	 * @return La session courante
	 */
	public static Session getCurrentSession(Context context) {
		if ((theSession == null) && (context != null)) {
			theSession = new SessionImpl(context.getApplicationContext());
		}
		return theSession;
	}
	
}

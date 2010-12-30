/**
 * 
 */
package com.alma.telekocsi.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

/**
 * @author Fatima
 *
 */
public class SessionImpl implements Session {

	/**
	 * Liste avec gestion des accès concurrents
	 */
	private final List<SessionListener> listeners 
		= Collections.synchronizedList(new ArrayList<SessionListener>());
	private final ProfilDAO profileDAO = new ProfilDAO();
	private Profil profile = null;
	
	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#getActiveProfile()
	 */
	@Override
	public Profil getActiveProfile(){
		return profile;
	}

	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#saveProfile(com.alma.telekocsi.dao.profil.Profil)
	 */
	@Override
	public void saveProfile(Profil profile) {
		if(profile!=null){
			profileDAO.insert(profile);
			this.profile = profile;
		}
	}

	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#addSessionListener(com.alma.telekocsi.session.SessionListener)
	 */
	@Override
	public void addSessionListener(SessionListener listener) {
		if(listener!=null){
			listeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#removeSessionListener(com.alma.telekocsi.session.SessionListener)
	 */
	@Override
	public void removeSessionListener(SessionListener listener) {
		if(listener!=null){
			listeners.add(listener);
		}
	}

	/**
	 * Notifie tous les listeners
	 * @param event
	 */
	public synchronized void dispatchEvent(SessionEvent event){
		synchronized(listeners){
			for(SessionListener l : listeners){
				try{
					l.onEvent(event);
				}catch(Throwable e){
					Log.e(getClass().getName(),e.getMessage());
				}
			}
		}
	}
	
}

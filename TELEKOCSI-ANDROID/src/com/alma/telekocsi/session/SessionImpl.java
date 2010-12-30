/**
 * 
 */
package com.alma.telekocsi.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

/**
 * @author Rv
 *
 */
public class SessionImpl implements Session {
	
	public static final String KEY_PROFILE_ID = "profile_id";
	public static final String KEY_PROFILE_CONNECTED = "profile_connected";
	private static final String PREFERENCES_STORE = "TELEKOCSI_PREFERENCES";
	
	/**
	 * 
	 */
	private Context context;
	/**
	 * settings
	 */
	private SharedPreferences settings;
	/**
	 * The notification manager
	 */
	protected NotificationManager notificationMgr;
	/**
	 * Liste avec gestion des accès concurrents
	 */
	private final List<SessionListener> listeners 
		= Collections.synchronizedList(new ArrayList<SessionListener>());
	
	/**
	 * The active profile
	 */
	private Profil profile = null;

	//DAO
	private final ProfilDAO profileDAO = new ProfilDAO();

	/**
	 * 
	 * @param context
	 */
	public SessionImpl(Context context){
		init(context);
	}
	
	/**
	 * 
	 * @param context
	 */
	private void init(Context context) {
		this.context = context;
		notificationMgr = (NotificationManager)this.context.getSystemService(Context.NOTIFICATION_SERVICE);		
		settings = context.getSharedPreferences(PREFERENCES_STORE, Context.MODE_PRIVATE);
	}

	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#getActiveProfile()
	 */
	@Override
	public synchronized Profil getActiveProfile(){
		if(profile==null){
			//On charge le profil
			String id = settings.getString(KEY_PROFILE_ID, null);
			if(id!=null){
				profile = profileDAO.getProfil(id);
			}
		}
		return profile;
	}

	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#saveProfile(com.alma.telekocsi.dao.profil.Profil)
	 */
	@Override
	public synchronized void saveProfile(Profil profile) {
		if(profile!=null){
			profileDAO.insert(profile);
			this.profile = profile;
			Editor editor = settings.edit();
			editor.putString(KEY_PROFILE_ID, profile.getId());
			editor.commit();
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
			listeners.remove(listener);
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

	@Override
	public boolean isConnected() {
		return settings.getBoolean(KEY_PROFILE_CONNECTED,false);
	}

	@Override
	public boolean login(String name, String password) {
		Profil profile = profileDAO.login(name, password);
		if(profile!=null){
			Editor edit = settings.edit();
			edit.putString(KEY_PROFILE_ID, profile.getId());
			edit.putBoolean(KEY_PROFILE_CONNECTED, true);
			edit.commit();
			return true;
		}
		return false;
	}

	@Override
	public boolean logout() {
		Editor edit = settings.edit();
		edit.putBoolean(KEY_PROFILE_CONNECTED, false);
		edit.commit();
		dispatchEvent(makeEvent(SessionEvent.LOGOUT, this, null));
		return true;
	}

	private SessionEvent makeEvent(final int type,final Object source,final Object data){
		return new SessionEvent() {
			
			@Override
			public Object getUserData() {
				return data;
			}
			
			@Override
			public Object getSource() {
				return source;
			}
			
			@Override
			public int getEventType() {
				return type;
			}
		};
	}
}

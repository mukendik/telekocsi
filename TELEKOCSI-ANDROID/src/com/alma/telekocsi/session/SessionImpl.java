/**
 * 
 */
package com.alma.telekocsi.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alma.telekocsi.R;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

/**
 * @author Rv
 *
 */
public class SessionImpl extends IntentService implements Session {
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        SessionImpl getService() {
            return SessionImpl.this;
        }
    }
	
	
	public static final String KEY_PROFILE_ID = "profile_id";
	private static final String PREFERENCES_STORE = "TELEKOCSI_PREFERENCES";
	
	/**
	 * settings
	 */
	private SharedPreferences settings;
	/**
	 * The notification manager
	 */
	private NotificationManager notificationMgr;
	/**
	 * Liste avec gestion des accès concurrents
	 */
	private final List<SessionListener> listeners 
		= Collections.synchronizedList(new ArrayList<SessionListener>());
	
	/**
	 * The active profile
	 */
	private Profil profile = null;

	/**
	 * The binder
	 */
	private LocalBinder mBinder = new LocalBinder();
	
	//DAO
	private final ProfilDAO profileDAO = new ProfilDAO();

	/**
	 * 
	 * @param name
	 */
    public SessionImpl(String name) {
		super(name);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Session", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        notificationMgr.cancel(R.string.session_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.session_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

	@Override
	public void onCreate() {
		super.onCreate();
		notificationMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);		
		settings = getSharedPreferences(PREFERENCES_STORE, Context.MODE_PRIVATE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
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

	
}

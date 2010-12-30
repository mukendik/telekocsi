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

import com.alma.telekocsi.dao.avis.Avis;
import com.alma.telekocsi.dao.avis.AvisDAO;
import com.alma.telekocsi.dao.event.Event;
import com.alma.telekocsi.dao.event.EventDAO;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.localisation.Localisation;
import com.alma.telekocsi.dao.localisation.LocalisationDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.dao.transaction.Transaction;
import com.alma.telekocsi.dao.transaction.TransactionDAO;

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
	private final AvisDAO avisDAO = new AvisDAO();
	private final ItineraireDAO itineraireDAO = new ItineraireDAO();
    private final EventDAO eventDAO = new EventDAO();
    private final TrajetDAO trajetDAO = new TrajetDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final LocalisationDAO localisationDAO = new LocalisationDAO();
    
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

	@Override
	public <T> void save(T object) {
		if(object instanceof Trajet) trajetDAO.insert((Trajet)object);
		else if(object instanceof Itineraire) itineraireDAO.insert((Itineraire)object);
		else if(object instanceof Event) eventDAO.insert((Event)object);
		else if(object instanceof Profil) profileDAO.insert((Profil)object);
		else if(object instanceof Avis) avisDAO.insert((Avis)object);
		else if(object instanceof Localisation) localisationDAO.insert((Localisation)object);
		else if(object instanceof Transaction) transactionDAO.insert((Transaction)object);
		throw new IllegalArgumentException("Unsupported parameter type : "+object.getClass().getName());
    }

	@Override
	public <T> void update(T object) {
		if(object instanceof Trajet) trajetDAO.update((Trajet)object);
		else if(object instanceof Itineraire) itineraireDAO.update((Itineraire)object);
		else if(object instanceof Event) eventDAO.update((Event)object);
		else if(object instanceof Profil) profileDAO.update((Profil)object);
		else if(object instanceof Avis) avisDAO.update((Avis)object);
		else if(object instanceof Localisation) localisationDAO.update((Localisation)object);
		else if(object instanceof Transaction) transactionDAO.update((Transaction)object);
		throw new IllegalArgumentException("Unsupported parameter type : "+object.getClass().getName());
	}
	
	@Override
	public <T> void delete(T object) {
		if(object instanceof Trajet) trajetDAO.delete((Trajet)object);
		else if(object instanceof Itineraire) itineraireDAO.delete((Itineraire)object);
		else if(object instanceof Event) eventDAO.delete((Event)object);
		else if(object instanceof Profil) profileDAO.delete((Profil)object);
		else if(object instanceof Avis) avisDAO.delete((Avis)object);
		else if(object instanceof Localisation) localisationDAO.delete((Localisation)object);
		else if(object instanceof Transaction) transactionDAO.delete((Transaction)object);
		throw new IllegalArgumentException("Unsupported parameter type : "+object.getClass().getName());
	}

	@Override
	public void clear(int type) {
		switch(type){
		case PROFIL: profileDAO.clear(); break;
		case TRANSACTION: transactionDAO.clear(); break;
		case TRAJET: trajetDAO.clear(); break;
		case EVENT: eventDAO.clear(); break;
		case AVIS: avisDAO.clear(); break;
		case LOCALISATION: localisationDAO.clear(); break;
		}
	}

}

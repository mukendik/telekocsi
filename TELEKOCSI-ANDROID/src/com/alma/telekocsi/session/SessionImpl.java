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
import com.alma.telekocsi.dao.trajet.TrajetLigne;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.dao.transaction.Transaction;
import com.alma.telekocsi.dao.transaction.TransactionDAO;


/**
 * @author Rv
 *
 */
public class SessionImpl implements Session {
	
	public static final String KEY_PROFILE_ID = "profile_id";
	public static final String KEY_PROFILE_CONNECTED = "profile_connected";
	public static final String KEY_PROFILE_PASS = "profile_secret";
	public static final String KEY_PROFILE_PSEUDO = "profile_pseudo";
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
	 * Liste avec gestion des acces concurrents
	 */
	private final List<SessionListener> listeners 
		= Collections.synchronizedList(new ArrayList<SessionListener>());
	
	/**
	 * The active profile
	 */
	private Profil profile = null;
	
	/**
	 * The active route
	 */
	private Trajet activeRoute = null;

	//DAO
	private final ProfilDAO profileDAO = new ProfilDAO();
	private final AvisDAO avisDAO = new AvisDAO();
	private final ItineraireDAO itineraireDAO = new ItineraireDAO();
    private final EventDAO eventDAO = new EventDAO();
    private final TrajetDAO trajetDAO = new TrajetDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final LocalisationDAO localisationDAO = new LocalisationDAO();
    private final TrajetLigneDAO trajetLigneDAO = new TrajetLigneDAO();
    
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
		
		Log.i(getClass().getName(), "Current Profile = "+settings.getString(KEY_PROFILE_ID, "null"));
	}

	/* (non-Javadoc)
	 * @see com.alma.telekocsi.session.Session#getActiveProfile()
	 */
	@Override
	public synchronized Profil getActiveProfile(){
		if(!isConnected()) return null;
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
			Log.i(getClass().getName(),"Saving profile '"+profile.getEmail()+"'");
			String id = profile.getId();
			if(id==null){				
				this.profile = profileDAO.insert(profile);
			}
			else{
				this.profile = profileDAO.update(profile);
			}
			Editor editor = settings.edit();
			editor.putString(KEY_PROFILE_ID, this.profile.getId());
			editor.putBoolean(KEY_PROFILE_CONNECTED, true);
			editor.commit();
			Log.i(getClass().getName(),this.profile.toString());
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
			this.profile = profile;
			this.profile.setConnecte(true);
			saveProfile(profile);
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
		this.profile.setConnecte(false);
		this.saveProfile(profile);
		Editor edit = settings.edit();
		edit.putBoolean(KEY_PROFILE_CONNECTED, false);
		edit.commit();
		dispatchEvent(makeEvent(SessionEvent.LOGOUT, this, null));
		this.profile = null;
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> T save(T object) {
		if(object instanceof Trajet) return (T)trajetDAO.insert((Trajet)object);
		else if(object instanceof Itineraire) return (T)itineraireDAO.insert((Itineraire)object);
		else if(object instanceof Event) return (T)eventDAO.insert((Event)object);
		else if(object instanceof Profil) return (T)profileDAO.insert((Profil)object);
		else if(object instanceof Avis) return (T)avisDAO.insert((Avis)object);
		else if(object instanceof Localisation) return (T)localisationDAO.insert((Localisation)object);
		else if(object instanceof Transaction) return (T)transactionDAO.insert((Transaction)object);
		else if(object instanceof TrajetLigne) return (T)trajetLigneDAO.insert((TrajetLigne)object);
		throw new IllegalArgumentException("Unsupported parameter type : "+object.getClass().getName());
    }

	@SuppressWarnings("unchecked")
	@Override
	public <T> T update(T object) {
		if(object instanceof Trajet) return (T)trajetDAO.update((Trajet)object);
		else if(object instanceof Itineraire) return (T)itineraireDAO.update((Itineraire)object);
		else if(object instanceof Event) return (T)eventDAO.update((Event)object);
		else if(object instanceof Profil) return (T)profileDAO.update((Profil)object);
		else if(object instanceof Avis) return (T)avisDAO.update((Avis)object);
		else if(object instanceof Localisation) return (T)localisationDAO.update((Localisation)object);
		else if(object instanceof Transaction) return (T)transactionDAO.update((Transaction)object);
		else if(object instanceof TrajetLigne) return (T)trajetLigneDAO.update((TrajetLigne)object);
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
		else if(object instanceof TrajetLigne) trajetLigneDAO.delete((TrajetLigne)object);
		throw new IllegalArgumentException("Unsupported parameter type : "+object.getClass().getName());
	}

	@Override
	public<T> void clear(Class<T> type) {
		if(Profil.class==type) profileDAO.clear();
		else if(Trajet.class==type) trajetDAO.clear();
		else if(Itineraire.class==type) itineraireDAO.clear();
		else if(Avis.class==type) avisDAO.clear();
		else if(Event.class==type) eventDAO.clear();
		else if(Localisation.class==type) localisationDAO.clear();
		else if(TrajetLigne.class==type) trajetLigneDAO.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T find(Class<T> type, String id) {
		if(Profil.class==type) return (T)profileDAO.getProfil(id);
		else if(Trajet.class==type) return (T)trajetDAO.getTrajet(id);
		else if(Itineraire.class==type) return (T)itineraireDAO.getItineraire(id);
		else if(Avis.class==type) return (T)avisDAO.getAvis(id);
		else if(Event.class==type) return (T)eventDAO.getEvent(id);
		else if(Localisation.class==type) return (T)localisationDAO.getLocalisation(id);
		else if(TrajetLigne.class==type) return (T)trajetLigneDAO.getTrajetLigne(id);
		return null;
	}
	
	@Override
	public<T> void clear(Class<T> type,String id) {
		if(Localisation.class==type) localisationDAO.clear(id);
	}
	
	@Override
	public List<Trajet> getRoutes() {
		if(profile!=null){
			return trajetDAO.getList(profile.getId());
		}
		return new ArrayList<Trajet>(0);
	}

	@Override
	public Trajet getActiveRoute() {
		return activeRoute;
	}

	@Override
	public void activateRoute(Trajet route) {
		this.activeRoute = route;
	}

	@Override
	public void deactivateRoute() {
		activeRoute = null;
	}

	@Override
	public void switchToPassengerType() {
		if(profile!=null){
			profile.setTypeProfil("P");
		}
	}

	@Override
	public void switchToDriverType() {
		if(profile!=null){
			profile.setTypeProfil("C");
		}
	}
	

	@Override
	public List<Itineraire> getItineraires() {
		Profil profil = getActiveProfile();
		if(profil!=null){
			List<Itineraire> itineraires = itineraireDAO.getList(profil.getId());
			return itineraires;
		}
		return new ArrayList<Itineraire>(0);
	}
	
	
	@Override
	public List<Trajet> getTrajets(Trajet trajetModel) {
		List<Trajet> trajets = trajetDAO.getTrajetDispo(trajetModel);		
		return trajets;
	}
	
}

package com.alma.telekocsi.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alma.telekocsi.GoogleMapActivity;
import com.alma.telekocsi.R;
import com.alma.telekocsi.dao.localisation.Localisation;
import com.alma.telekocsi.dao.localisation.LocalisationDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.util.LocalDate;
import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;


public class MapUserLocalization {

	private GeoPoint pointConducteur;
	private List<GeoPoint> pointsPassager;

	private static final String CONDUCTEUR = "C";        
	
	private Timer timer;
	private Profil profil;
	private Trajet trajet;
	private LocationManager locationManager;
	private GoogleMapActivity context;
	private List<MapOverlay> overlays;

	public MapUserLocalization(GoogleMapActivity context, Profil profil, Trajet trajet) {
		this.context = context;
		this.profil = profil;
		this.trajet = trajet;
		timer = new Timer();
		pointsPassager = new ArrayList<GeoPoint>();
		overlays = new ArrayList<MapOverlay>();
	}


	public void start() {
		Log.d(this.getClass().getName(), "start MapUserLocalization"); 
		timer.scheduleAtFixedRate(new TimerTask() { 
			public void run() { 
				Log.i(MapUserLocalization.class.getSimpleName(), "Scheduler MapUserLocalization");
				new CalculUserPosition().execute(null, null, null);
			} 
		}, 0, 20000); 
		
		
	}


	
	public void stop(){ 
		timer.cancel();
	}


	/**
	 * Récupère la position du mobile
	 * @param context
	 * @return un GeoPoint
	 */
	private GeoPoint getPositionMobile(Context context) {
		Log.i(MapUserLocalization.class.getSimpleName(),"---> Debut getPositionMobile() <---");
		
		String locationContext = Context.LOCATION_SERVICE;
		Log.i(MapUserLocalization.class.getSimpleName(), "LOCATION_SERVICE : " + locationContext);
		locationManager = (LocationManager) context.getSystemService(locationContext);
		Log.i(MapUserLocalization.class.getSimpleName(), "locationManager : " + locationManager.toString());
		Location location;
		location = getLastKnownLocation();

		GeoPoint positionMobile = null;
		int latitudeE6;
		int longitudeE6;

		if(location!=null) {
			Log.i(MapUserLocalization.class.getSimpleName(),"LocationManager -> provider : " + location.getProvider() +
					" | Lat: " +location.getLatitude() +
					" | Longitude : "+location.getLongitude());

			//construction du GeoPoint 
			latitudeE6 = (int)(location.getLatitude()*1000000);
			longitudeE6 = (int)(location.getLongitude()*1000000);
			positionMobile = new GeoPoint(latitudeE6, longitudeE6);

			//insert localisation en bdd
			LocalisationDAO localisationDAO = new LocalisationDAO();
			Localisation localisation;
			localisation = new Localisation();

			LocalDate dateCourante = new LocalDate();
			localisation.setDateLocalisation(dateCourante.getDateFormatCalendar());
			localisation.setHeureLocalisation(dateCourante.getDateFomatHeure());
			localisation.setIdProfil(profil.getId());
			localisation.setLatitude(location.getLatitude());
			localisation.setLongitude(location.getLongitude());
			localisation.setPointGPS(location.getLatitude() + " , "+location.getLongitude());

			localisationDAO.insert(localisation);
		}


		Log.i(MapUserLocalization.class.getSimpleName(),"---> Fin getPositionMobile() <---");
		return positionMobile;
	}

	/**
	 * Derniere location connu par le provider
	 * @return Location
	 */
	private Location getLastKnownLocation() {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
		if(location==null){
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) ;
		}
		Log.i(MapUserLocalization.class.getSimpleName(), "location : "+location);
		return location;
	}

	private void calculUsersPosition() {
		
		LocalisationDAO localisationDAO = new LocalisationDAO();
		if(profil.getTypeProfil().equals(CONDUCTEUR)){
			Log.i(MapUserLocalization.class.getSimpleName(), "Type du Profil : CONDUCTEUR");

			//position conducteur = celle du mobile calcule GPS
			pointConducteur = getPositionMobile(context);
			if(pointConducteur!=null){
				overlays.add(new MapOverlay(pointConducteur, R.drawable.pin_conducteur));
			}
			//coordonnée des passagers = derniere localisation GAE 
			TrajetLigneDAO trajetLigneDAO = new TrajetLigneDAO();
		
			Localisation localisation = null;

			for(String idPassager : trajetLigneDAO.getListPassagers(trajet.getId())) {
				localisation = localisationDAO.getList(idPassager).size() > 0 ? localisationDAO.getList(idPassager).get(0) : null;
				if(localisation!=null) {
					GeoPoint pointPassager = new GeoPoint((int)(localisation.getLatitude()*1000000),(int)(localisation.getLongitude()*1000000));
					pointsPassager.add(pointPassager);
					overlays.add(new MapOverlay(pointPassager, R.drawable.pin_conducteur));
					
					Log.i(MapUserLocalization.class.getSimpleName(), "Passager position : " + localisation.getLatitude() +" , " +localisation.getLongitude());
				}
			}

		}else {
			Log.i(MapUserLocalization.class.getSimpleName(), "Type du Profil : PASSAGER");
			
			GeoPoint passager = getPositionMobile(context);
			
			if(passager!=null){
				pointsPassager.add(passager);
			}
			
			
			//coordonnée du conducteur
			// recherche sur GAE
			List<Localisation> localisationsConducteur = localisationDAO.getList(trajet.getIdProfilConducteur());
			if(localisationsConducteur!=null && localisationsConducteur.size()>0) {
				pointConducteur = new GeoPoint((int)(localisationsConducteur.get(0).getLatitude()*1000000), (int)(localisationsConducteur.get(0).getLongitude()*1000000));
			}

		}
		
	}
	
	private class CalculUserPosition extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... urls) {
			calculUsersPosition();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			context.showUserLocalizationOverlays();
		}		
	}
	
	
	
	
	/* ***************************
	 * Getter & Setter
	 * ***************************/
	public GeoPoint getPointConducteur() {
		return pointConducteur;
	}

	public void setPointConducteur(GeoPoint pointConducteur) {
		this.pointConducteur = pointConducteur;
	}

	public List<GeoPoint> getPointsPassager() {
		return pointsPassager;
	}

	public void setPointsPassager(List<GeoPoint> pointsPassager) {
		this.pointsPassager = pointsPassager;
	}
	
	public List<MapOverlay> getOverlays(){
		return overlays;
	}
}
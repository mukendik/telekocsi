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
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.util.LocalDate;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class MapUserLocalization {

	private GeoPoint pointConducteur;
	private List<GeoPoint> pointsPassager;


	public static final String RESULT_USERLOCALIZATION = "result";
	private static final String CONDUCTEUR = "C";        
	
	private Timer timer;
	private Profil profil;
	private LocationManager locationManager;
	private MapView mapView;


	public MapUserLocalization() {
	
	
		timer = new Timer();
		pointsPassager = new ArrayList<GeoPoint>();
		//mapView = (MapView) findViewById(R.id.mapView);
/*
		if(profil.getTypeProfil().equals(CONDUCTEUR)){
			Log.i(GoogleMapActivity.class.getSimpleName(), "Type du Profil : CONDUCTEUR");

			//position conducteur = celle du mobile calcule GPS
			pointConducteur = getPositionMobile(context);

			//coordonnée des passagers = derniere localisation GAE 
			TrajetLigneDAO trajetLigneDAO = new TrajetLigneDAO();
			LocalisationDAO localisationDAO = new LocalisationDAO();
			Localisation localisation = null;

			for(String idPassager : trajetLigneDAO.getListPassagers(trajetID)) {
				localisation = localisationDAO.getList(idPassager).size() > 0 ? localisationDAO.getList(idPassager).get(0) : null;
				if(localisation!=null) {
					pointsPassager.add(new GeoPoint((int)(localisation.getLatitude()*1000000),(int)(localisation.getLongitude()*1000000)));

					Log.i(GoogleMapActivity.class.getSimpleName(), "Passager position : " + localisation.getLatitude() +" , " +localisation.getLongitude());
				}
			}

		}else {
			GeoPoint p = getPositionMobile(context);
			if(p!=null){
				pointsPassager.add(p);
			}

			int latitudeE6 = (int)(46.814081*1000000);
			int longitudeE6 = (int)(-1.349166*1000000);

			pointConducteur = new GeoPoint(latitudeE6, longitudeE6);
			Log.i(GoogleMapActivity.class.getSimpleName(), "Type Profil : PASSAGER");
			//coordonnée du conducteur
			// recherche sur GAE
		}
*/
	}


	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(this.getClass().getName(), "onStart"); 
		timer.scheduleAtFixedRate(new TimerTask() { 
			public void run() { 
				int n = 3, result = 2;
				Intent resultIntent = new Intent(RESULT_USERLOCALIZATION);
				resultIntent.putExtra("fibo", n);
				resultIntent.putExtra("result", result);
				//sendBroadcast(resultIntent);
			} 
		}, 0, 20000); 

		return 1;
	}


	
	public void onDestroy(){ 
		
		timer.cancel();
	}





	/* Methods du service */
	public String test() {
		return "Test appel method sur service";
	}


	/**
	 * Récupère la position du mobile
	 * @param context
	 * @return un GeoPoint
	 */
	private GeoPoint getPositionMobile(Context context) {
		Log.i(GoogleMapActivity.class.getSimpleName(),"---> Debut getPositionMobile() <---");

		String locationContext = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) context.getSystemService(locationContext);

		Location location;
		location = getLastKnownLocation();

		GeoPoint positionMobile = null;
		int latitudeE6;
		int longitudeE6;

		if(location!=null) {
			Log.i(GoogleMapActivity.class.getSimpleName(),"LocationManager -> provider : " + location.getProvider() +
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


		Log.i(GoogleMapActivity.class.getSimpleName(),"---> Fin getPositionMobile() <---");
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
		return location;
	}


	/**
	 * Positionnement des markers sur la map
	 */
	private void makeOverlays() {
		//Ajout de markers
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();

		//conducteur
		if(getPointConducteur()!=null){
			listOfOverlays.add(new MapOverlay(getPointConducteur(), R.drawable.pin_conducteur));  
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointConducteur()-->null Pointer  donc non affiche");}


		//liste passagers
		if(getPointsPassager()!=null){
			for(GeoPoint pointPassager : getPointsPassager()){
				listOfOverlays.add(new MapOverlay(pointPassager, R.drawable.pin_passager));
			}
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointsPassager()-->null Pointer  donc non affiche");}

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
}
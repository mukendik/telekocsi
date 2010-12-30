package com.alma.telekocsi;


import java.util.List;



import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.init.DataContext;
import com.alma.telekocsi.map.MapInfo;
import com.alma.telekocsi.map.MapOverlay;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

//import android.content.Intent;
//import android.net.Uri;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;


/**
 * Objectif du jour :
 * Trouver sa position et calculer la distance par rapport à un autre point
 * @author Romain
 *
 */

public class GoogleMapActivity extends MapActivity {

	private MapView mapView;
	private MapController mapController;
	private GeoPoint location;
	private Profil currentProfil;
	private MapInfo mapInfo;
	private Context context;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		//Récupération du profil courant
		Session session = SessionFactory.getCurrentSession(this);
		currentProfil = session.getActiveProfile();
		if(currentProfil==null) {
			Log.i(GoogleMapActivity.class.getSimpleName(), "currentProfil == null");
			currentProfil = DataContext.getCurrentProfil();//Créér profile
			session.saveProfile(currentProfil);
		}
		String sessionID = currentProfil.getId();


		//		Profil currentProfil = DataContext.getCurrentProfil();
		//		String sessionID = currentProfil.getId();

		Log.i(GoogleMapActivity.class.getSimpleName(), "Session profilID : "+sessionID);



		setContentView(R.layout.map);

		this.mapView = (MapView) findViewById(R.id.mapView);
		this.mapController = this.mapView.getController();
		this.mapController.setZoom(10); //taille du zoom

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				//générer les geopoints [conducteur, passager, depart etc.]
				mapInfo = new MapInfo(context, currentProfil);
				if(mapInfo.getPointDepart()!=null){
					// positionne googlemap sur le point de depart
					mapController.setCenter(mapInfo.getPointDepart());

				}
				makeOverlays();
			}
		});

		thread.start();

		
		mapView.setSatellite(false);
		mapView.setBuiltInZoomControls(true);
	}


	protected void onStart() {
		super.onStart();



	}


	private void makeOverlays() {
		//Ajout de markers

		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		//depart
		if(mapInfo.getPointDepart()!=null){
			listOfOverlays.add(new MapOverlay(this, mapInfo.getPointDepart(), R.drawable.pin_depart));
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointDepart()-->null Pointer  donc non affiche");}

		//arrivee
		if(mapInfo.getPointArrivee()!=null){
			listOfOverlays.add(new MapOverlay(this, mapInfo.getPointArrivee(), R.drawable.pin_arrivee));
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointArrivee()-->null Pointer  donc non affiche");}

		//conducteur
		if(mapInfo.getPointConducteur()!=null){
			listOfOverlays.add(new MapOverlay(this, mapInfo.getPointConducteur(), R.drawable.pin_conducteur));  
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointConducteur()-->null Pointer  donc non affiche");}


		//liste passagers
		if(mapInfo.getPointsPassager()!=null){
			for(GeoPoint pointPassager : mapInfo.getPointsPassager()){
				listOfOverlays.add(new MapOverlay(this, pointPassager, R.drawable.pin_passager));
			}
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointsPassager()-->null Pointer  donc non affiche");}

	}




	/* Itinéraire avec google maps
	private void tracer(GeoPoint p, GeoPoint p2) {

		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse("http://maps.google.com/maps?saddr="+(p.getLatitudeE6()/1000000.0)+","+(p.getLongitudeE6()/1000000.0) + 
						"&daddr="+(p2.getLatitudeE6()/1000000.0)+","+(p2.getLongitudeE6()/1000000.0)));
		startActivity(intent);

	}*/


	public GeoPoint getLocation() {
		return location; 
	}

	public void setLocation(GeoPoint location) {
		if(location!=null){
			this.location = location;
			this.mapController.setCenter(this.location);
			this.mapView.invalidate();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}



}
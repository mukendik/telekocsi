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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


/**
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
	private boolean trajetActive = false;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		//Récupération du profil courant
		Session session = SessionFactory.getCurrentSession(context);
		currentProfil = session.getActiveProfile();
		if(currentProfil==null) {
			Log.i(GoogleMapActivity.class.getSimpleName(), "currentProfil == null");
			currentProfil = DataContext.getCurrentProfil();//Créér profile
			
		}else {
			Log.i(GoogleMapActivity.class.getSimpleName(), "currentProfil == session");
		}
		


		//		Profil currentProfil = DataContext.getCurrentProfil();
		//		String sessionID = currentProfil.getId();

		Log.i(GoogleMapActivity.class.getSimpleName(), "profil : "+currentProfil);



		setContentView(R.layout.map);

		this.mapView = (MapView) findViewById(R.id.mapView);
		this.mapController = this.mapView.getController();
		this.mapController.setZoom(10); //taille du zoom
//
//		Thread thread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
				//générer les geopoints [conducteur, passager, depart etc.]
				mapInfo = new MapInfo(context, currentProfil, mapView);
				trajetActive = mapInfo.load();
				if(mapInfo.getPointDepart()!=null) {
					// positionne googlemap sur le point de depart
					mapController.setCenter(mapInfo.getPointDepart());
					
				}
				Log.i(GoogleMapActivity.class.getSimpleName(), "Distance = " + mapInfo.getDistanceTotal());
			
//			}
//		});

//		thread.start();

		

		mapView.setSatellite(false);
		mapView.setBuiltInZoomControls(true);
		
		if(!trajetActive){
			Toast.makeText(context, "Pas de Trajet Activé", Toast.LENGTH_LONG).show();
		}
	}


	protected void onStart() {
		super.onStart();
	
	}


	public GeoPoint getLocation() {
		return location; 
	}

	public void setLocation(GeoPoint location) {
		if(location!=null) {
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
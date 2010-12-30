package com.alma.telekocsi;

import java.util.List;

import com.alma.telekocsi.map.MapOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

//import android.content.Intent;
//import android.net.Uri;
import android.os.Bundle;

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


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	
		this.mapView = (MapView) findViewById(R.id.mapView);

		this.mapController = this.mapView.getController();

		/*
		 * Définition d'une position 
		 * coordonnées prise sur google maps
		 */
		double latitudeP = 47.842838;
		double longitudeP = -0.820595;
		GeoPoint p = new GeoPoint((int) (latitudeP * 1000000.0),(int) (longitudeP * 1000000.0));
		this.setLocation(p);

		double latitudeP2 = 47.214081;
		double longitudeP2 = -1.549166;
		GeoPoint p2 = new GeoPoint((int) (latitudeP2 * 1000000.0),(int) (longitudeP2 * 1000000.0));


		this.mapController.setZoom(19);
		this.mapView.setSatellite(false);


		//Ajout de markers
		MapOverlay mapOverlay = new MapOverlay(this, p);
		MapOverlay mapOverlay2 = new MapOverlay(this, p2);
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		listOfOverlays.add(mapOverlay2);   


		this.mapView.invalidate();

		//Ajout des zooms 
		this.mapView.setBuiltInZoomControls(true);

		
		//this.setContentView(this.mapView);
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
		this.location = location;
		this.mapController.setCenter(this.location);
		this.mapView.invalidate();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}



}
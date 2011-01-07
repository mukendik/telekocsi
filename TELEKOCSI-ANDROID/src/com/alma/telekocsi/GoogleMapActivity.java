package com.alma.telekocsi;


import java.util.List;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.map.IMapInfoItiniraire;
import com.alma.telekocsi.map.MapInfoItiniraire;
import com.alma.telekocsi.map.MapOverlay;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Romain
 *
 */

public class GoogleMapActivity extends MapActivity implements IMapInfoItiniraire {

	private MapView mapView;
	private TextView tvDistance;
	private TextView tvDepart;
	private TextView tvArrivee;
	private MapController mapController;
	private GeoPoint location;
	private Profil currentProfil;
	private Context context;
	private Trajet trajetActif;
	private MapInfoItiniraire mapInfoItiniraire;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		context = this;

		//Récupération du profil courant
		Session session = SessionFactory.getCurrentSession(context);
		if(session!=null) {

			currentProfil = session.getActiveProfile(); // n'est jamais null normalement
			Log.i(GoogleMapActivity.class.getSimpleName(), "profil de la session courante \n==> "+currentProfil);

			trajetActif = session.getActiveRoute();
			//Pour l'instant trajetActif est toujours null 
			if(trajetActif==null) {	

				/* ********** *
				 * TEMPORAIRE *
				 * ********** */

				//Trajet à afficher
				TrajetDAO trajetDAO = new TrajetDAO();
				List<Trajet> trajets = trajetDAO.getList(currentProfil.getId());
				if(trajets.size()>0){
					trajetActif = trajets.get(0);
					Log.i(GoogleMapActivity.class.getSimpleName(), "Trajet : "+trajetActif);
				}else {

					/* ********** *
					 * FIN TEMP.. *
					 * ********** */
					Log.i(GoogleMapActivity.class.getSimpleName(), "Aucun Trajet Activé");
					Toast.makeText(context, "Pas de Trajet Activé", Toast.LENGTH_SHORT).show();
					finish();
				}
			}


			mapView = (MapView) findViewById(R.id.mapView);
			//mapTableInfo = (TableLayout) findViewById(R.id.mapTableInfo);
			tvArrivee = (TextView) findViewById(R.id.arriveeName);
			tvDepart = (TextView) findViewById(R.id.departName);
			tvDistance = (TextView) findViewById(R.id.distance);

		}

	}

	/** Called after the activity is first created. */
	@Override
	protected void onStart() {
		super.onStart();
		mapController = this.mapView.getController();
		mapController.setZoom(10); 
		mapView.setSatellite(false);
		mapView.setBuiltInZoomControls(true);
		
		
		mapInfoItiniraire = new MapInfoItiniraire(this, trajetActif);
		mapInfoItiniraire.start();
		Toast.makeText(context, "Veuillez patienter...", Toast.LENGTH_LONG).show();

	}

	

	


	@Override
	public void onDestroy(){
		super.onDestroy();
		mapInfoItiniraire.stop();
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

	@Override
	public void finish() {
		super.finish();
	}
	
	

	@Override
	public void showItiniraireOverlays() {
		//Ajout de markers
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		//depart
		if(mapInfoItiniraire.getPointDepart()!=null){
			listOfOverlays.add(new MapOverlay(mapInfoItiniraire.getPointDepart(), R.drawable.pin_depart));
			mapController.animateTo(mapInfoItiniraire.getPointDepart());
			tvDepart.setText(mapInfoItiniraire.getLieuDepart());
		}
		

		//arrivee
		if(mapInfoItiniraire.getPointArrivee()!=null){
			listOfOverlays.add(new MapOverlay(mapInfoItiniraire.getPointArrivee(), R.drawable.pin_arrivee));
			tvArrivee.setText(mapInfoItiniraire.getLieuArrivee());
		}
		
		
		listOfOverlays.addAll(mapInfoItiniraire.getOverlays());

		tvDistance.setText( mapInfoItiniraire.getDistanceTotalToString()+" / " + mapInfoItiniraire.getDistanceTotalToString()+" Km");
		
	}

	
	
}
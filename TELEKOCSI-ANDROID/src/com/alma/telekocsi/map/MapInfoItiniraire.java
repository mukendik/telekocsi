package com.alma.telekocsi.map;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.alma.telekocsi.GoogleMapActivity;
import com.alma.telekocsi.R;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;

import com.alma.telekocsi.dao.trajet.Trajet;
import com.google.android.maps.GeoPoint;

public class MapInfoItiniraire {
	private GeoPoint pointDepart;
	private String lieuDepart;
	private GeoPoint pointArrivee;
	private String lieuArrivee;
	private List<GeoPoint> pointsVia;
	private double distanceTotale;
	private List<MapOverlay> overlays;


	private NumberFormat doubleFormat;

	//	private Timer timer;
	private Trajet trajet;
	private GoogleMapActivity context;


	public MapInfoItiniraire(GoogleMapActivity context, Trajet trajet) {
		Log.i(MapInfoItiniraire.class.getSimpleName(), "Constructor MapInfoItiniraire");
		this.distanceTotale = 0;
		this.doubleFormat = new DecimalFormat("0.0");
		this.lieuDepart = "";
		this.lieuArrivee = "";
		//		this.timer = new Timer();
		overlays = new ArrayList<MapOverlay>();
		this.context = context;
		this.trajet = trajet;
		Log.i(MapInfoItiniraire.class.getSimpleName(), "Trajet : "+trajet);


	}


	public void start() {
		Log.i(this.getClass().getName(), "start MapInfoItiniraire");
		new CalculItiniraire().execute(null, null, null);

		//		timer.scheduleAtFixedRate(new TimerTask() { 
		//			public void run() { 
		//				Log.i(MapInfoItiniraire.class.getSimpleName(), "Scheduler MapInfoItiniraire");
		//			} 
		//		}, 5000, 10000); 
	}



	public void stop(){ 
		Log.i(this.getClass().getName(), "stop MapInfoItiniraire");
		//		timer.cancel();
	}


	private class CalculItiniraire extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... urls) {
			calculInfoItiniraire();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			context.showItiniraireOverlays();
		}		
	}


	/**
	 * Chargement, calule informations de l'itineraire
	 * @return
	 */
	public boolean calculInfoItiniraire(){

		if(trajet!=null){
			ItineraireDAO itineraireDAO = new ItineraireDAO();
			Itineraire iti = itineraireDAO.getItineraire(trajet.getIdItineraire());
			Log.i(getClass().getSimpleName(), "isAutoroute : " + trajet.isAutoroute());
			// Lieu de départ et Lieu d'arrivée
			lieuDepart = iti.getLieuDepart();
			lieuArrivee = iti.getLieuDestination();			
			Log.i(MapInfoItiniraire.class.getSimpleName(), "Itineraire :\n => Depart : " + iti.getLieuDepart()+" \n => Arrivee : "+iti.getLieuDestination());

			//Calcul des GeoPoints  Depart & Arrivee
			pointDepart = calculGeoPoint(lieuDepart);
			overlays.add(new MapOverlay(pointDepart, R.drawable.pin_depart));
			pointArrivee = calculGeoPoint(lieuArrivee);
			overlays.add(new MapOverlay(pointArrivee, R.drawable.pin_arrivee));

			//Calcul des GeoPoints Via
			/*
			List<String> lieux = iti.getLieuxVia();
			pointsVia = new ArrayList<GeoPoint>();
			if(lieux.size()>0) {
				for(String lieuVia : lieux){
					pointsVia.add(calculGeoPoint(lieuVia));
				}
			}
			 */

			/*Test via Cholet   TEMPORAIRE *
			Geocoder geoCoder = new Geocoder(context);
			List<Address> adrs;
			try {
				adrs = geoCoder.getFromLocationName("CHOLET", 1);

				if(adrs!=null && adrs.size()>0){
					Address addressVia = adrs.get(0);
					pointsVia = new ArrayList<GeoPoint>();
					pointsVia.add(new GeoPoint((int) (addressVia.getLatitude() * 1000000.0),(int) (addressVia.getLongitude() * 1000000.0)));

					Log.i(MapInfoItiniraire.class.getSimpleName(), 
							" Lieu Via" +
							" |Pays : " + addressVia.getCountryName() + 
							" |feature : "+ addressVia.getFeatureName() +
							" |Latitude : "+ addressVia.getLatitude() +
							" |Longitude : "+ addressVia.getLongitude() 
					);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			* FIN TEMPORAIRE */


			if(pointDepart!=null && pointArrivee!=null){
				drawPath(pointDepart, pointArrivee, pointsVia, Color.BLUE);
			}


			return true;
		}

		return false;
	}




	//Calcul le GeoPoint d'un lieu :  exemple  de lieu :  "Chateau Gontier, Mayenne, France"
	private GeoPoint calculGeoPoint(String lieu) {
		Geocoder geoCoder = new Geocoder(context);
		List<Address> adrs;
		//Récupération coordonnées de l'adresse du lieu
		Address address=null;
		GeoPoint geoPoint = null;

		try {
			adrs = geoCoder.getFromLocationName(lieu, 1);

			if(adrs!=null && adrs.size()>0){
				address = adrs.get(0);
				geoPoint = new GeoPoint((int) (address.getLatitude() * 1000000.0),(int) (address.getLongitude() * 1000000.0));

				Log.i(MapInfoItiniraire.class.getSimpleName(), 
						" Lieu " +
						" |Pays : " + address.getCountryName() + 
						" |feature : "+ address.getFeatureName() +
						" |Latitude : "+ address.getLatitude() +
						" |Longitude : "+ address.getLongitude() 
				);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return geoPoint;
	}


	/**
	 * Traces les différentes portion d'itiniraires en prenant en compte les intermédiaires
	 * src-via  et via-dest
	 * @param src  point de départ
	 * @param dest point d'arrivée
	 * @param via  Liste de points intermédiaire
	 * @param color couleur de l'itiniraire
	 */
	private void drawPath(GeoPoint src,GeoPoint dest,List<GeoPoint> via, int color){
		if(via!=null && via.size()>0){
			Log.i(getClass().getSimpleName(), "Tracage itiniraire intermediaire");
			//trace la portion (depart - 1er point intermédiaire)
			drawPath(src, via.get(0), color);	
			//trace les différentes portions de points intermédiaire de l'itiniraire
			for(int i = 1; i<via.size(); i++) {
				drawPath(via.get(i-1), via.get(i), color);	
			}
			//trace la portion (dernier point intermédiaire - point d'arrivee)
			drawPath(via.get(via.size()-1), dest, color); 
		}else{
			Log.i(getClass().getSimpleName(), "Tracage itiniraire sans intermediaire");
			drawPath(src, dest, color);
		}
	}

	/**
	 * Calcul de l'itiniraire entre 2 GeoPoints
	 * @param src
	 * @param dest
	 * @param color
	 */
	private void drawPath(GeoPoint src,GeoPoint dest, int color){
		// connect to map web service
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.google.com/maps?f=d&hl=fr");
		if(!trajet.isAutoroute())urlString.append("&dirflg=t"); // evitement section à peage
		urlString.append("&saddr=");//from
		urlString.append( Double.toString((double)src.getLatitudeE6()/1.0E6 ));
		urlString.append(",");
		urlString.append( Double.toString((double)src.getLongitudeE6()/1.0E6 ));
		urlString.append("&daddr=");//to
		urlString.append( Double.toString((double)dest.getLatitudeE6()/1.0E6 ));
		urlString.append(",");
		urlString.append( Double.toString((double)dest.getLongitudeE6()/1.0E6 ));
		urlString.append("&ie=UTF8&0&om=0&output=kml");
		Log.d("xxx","URL="+urlString.toString());
		// get the kml (XML) doc. And parse it to get the coordinates(direction route).
		Document doc = null;
		HttpURLConnection urlConnection= null;
		URL url = null;
		try
		{ 
			url = new URL(urlString.toString());
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect(); 

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlConnection.getInputStream()); 

			if(doc.getElementsByTagName("GeometryCollection").getLength()>0) {
				String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue() ;
				Log.d("xxx","path="+ path);
				String [] pairs = path.split(" "); 
				String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height
				// src
				GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
				//overlays.add(new MapOverlay(startGP, R.drawable.pin_depart));
				GeoPoint gp1;
				GeoPoint gp2 = startGP; 
				double distanceIntermediaire;
				for(int i=1;i<pairs.length;i++) // the last one would be crash
				{
					lngLat = pairs[i].split(",");
					gp1 = gp2;
					// watch out! For GeoPoint, first:latitude, second:longitude
					gp2 = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
					overlays.add(new MapOverlay(gp1,gp2,MapOverlay.PATH,color));
					Log.d("xxx","pair:" + pairs[i]);
					distanceIntermediaire = Distance.calculateDistance(gp1, gp2, Distance.KILOMETERS);
					distanceTotale += Double.isNaN(distanceIntermediaire) ? 0 : distanceIntermediaire;
				}
				//overlays.add(new MapOverlay(dest, R.drawable.pin_arrivee)); // use the default color
			} 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


	}









	/* ******************************
	 * 		Getters & Setters		*
	 * ******************************/
	public GeoPoint getPointDepart() {
		return pointDepart;
	}

	public void setPointDepart(GeoPoint pointDepart) {
		this.pointDepart = pointDepart;
	}

	public GeoPoint getPointArrivee() {
		return pointArrivee;
	}

	public void setPointArrivee(GeoPoint pointArrivee) {
		this.pointArrivee = pointArrivee;
	}

	public List<GeoPoint> getPointsVia() {
		return pointsVia;
	}


	public double getDistanceTotal(){
		return distanceTotale;
	}

	public String getDistanceTotalToString(){
		return doubleFormat.format(distanceTotale);		
	}

	public String getLieuDepart() {
		return lieuDepart;
	}

	public String getLieuArrivee() {
		return lieuArrivee;
	}

	public List<MapOverlay> getOverlays(){
		return overlays;
	}



}

package com.alma.telekocsi.map;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.alma.telekocsi.GoogleMapActivity;
import com.alma.telekocsi.R;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.localisation.Localisation;
import com.alma.telekocsi.dao.localisation.LocalisationDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.util.LocalDate;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapInfo {
	private static final String CONDUCTEUR = "C";

	private GeoPoint pointConducteur;
	private List<GeoPoint> pointsPassager;
	private GeoPoint pointDepart;
	private GeoPoint pointArrivee;
	private LocationManager locationManager;
	private Profil profil;

	private double distanceTotale;

	private Context context;

	private MapView mapView;

	private GeoPoint pointVia;

	public MapInfo(Context context, Profil currentProfil,MapView mapView) {
		this.context = context;
		this.profil = currentProfil;
		this.pointsPassager = new ArrayList<GeoPoint>();
		this.mapView = mapView;
		this.distanceTotale = 0;

	}

	public boolean load(){
		TrajetDAO trajetDAO = new TrajetDAO();

		List<com.alma.telekocsi.dao.trajet.Trajet> trajets = trajetDAO.getList(profil.getId());
		Log.i(GoogleMapActivity.class.getSimpleName(), "Recherche trajet du profil "+profil.getId());
		if(trajets!=null && trajets.size()>0){
			com.alma.telekocsi.dao.trajet.Trajet trajet = trajets.get(0);

			String trajetID = trajet!=null ? trajet.getId() : "";
			String itiniraireID = trajet!=null ? trajet.getIdItineraire() : "";

			ItineraireDAO itineraireDAO = new ItineraireDAO();


			if(profil.getTypeProfil().equals(CONDUCTEUR)){	
				Log.i(GoogleMapActivity.class.getSimpleName(), "Type Profil : CONDUCTEUR");

				pointConducteur = getPositionMobile(context); //fixe en dur
				//coordonnée des passagers
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

				/*temporaire
			double getLatitude = 47.014081; //temp
			double getLongitude = -1.249166;//temp
			for(int idPassager = 0; idPassager<3;idPassager++) {
				getLatitude -= 0.1;//temp
				getLongitude += 0.1;//temp

				//listPointPassager.add(new GeoPoint((int)(localisation.getLatitude()*1000000),(int)(localisation.getLongitude()*1000000)));
				pointsPassager.add(new GeoPoint((int)(getLatitude*1000000),(int)(getLongitude*1000000)));
			}
			fintemporaire*/	

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

			}



			Itineraire iti = itineraireDAO.getItineraire(itiniraireID);
			Log.i(GoogleMapActivity.class.getSimpleName(), "Itineraire : Depart : " + iti.getLieuDepart()+" | Arrivee : "+iti.getLieuDestination());

			Geocoder geoCoder = new Geocoder(context);
			List<Address> adrs;
			try {
				//Récupération coordonnées de l'adresse de départ
				Address AddressDepart=null;
				Address AddressArrivee =null;

				/*Test via Cholet*/
				adrs = geoCoder.getFromLocationName("CHOLET", 1);
				if(adrs.size()>0){
					Address AddressVia = adrs.get(0);
					pointVia = new GeoPoint((int) (AddressVia.getLatitude() * 1000000.0),(int) (AddressVia.getLongitude() * 1000000.0));
				}
				/**/

				adrs = geoCoder.getFromLocationName(iti.getLieuDepart(), 1);
				if(adrs!=null && adrs.size()>0){
					AddressDepart = adrs.get(0);
					pointDepart = new GeoPoint((int) (AddressDepart.getLatitude() * 1000000.0),(int) (AddressDepart.getLongitude() * 1000000.0));

					Log.i(GoogleMapActivity.class.getSimpleName(), 
							" Lieu Depart " +
							" |Pays : " + AddressDepart.getCountryName() + 
							" |feature : "+ AddressDepart.getFeatureName() +
							" |Latitude : "+ AddressDepart.getLatitude() +
							" |Longitude : "+ AddressDepart.getLongitude() 
					);
				}

				//Récupération coordonnées de l'adresse d'arrivée
				adrs = geoCoder.getFromLocationName(iti.getLieuDestination(), 1);
				if(adrs!=null  && adrs.size()>0){
					AddressArrivee = adrs.get(0);
					pointArrivee = new GeoPoint((int) (AddressArrivee.getLatitude() * 1000000.0),(int) (AddressArrivee.getLongitude() * 1000000.0));

					Log.i(GoogleMapActivity.class.getSimpleName(), 
							" Lieu Arrivee " +
							" |Pays : " + AddressArrivee.getCountryName() + 
							" |feature : "+ AddressArrivee.getFeatureName() +
							" |Latitude : "+ AddressArrivee.getLatitude() +
							" |Longitude : "+ AddressArrivee.getLongitude() 
					);
				}

				if(pointDepart!=null && pointArrivee!=null){
					makeOverlays();
					drawPath(pointDepart, pointArrivee,pointVia, Color.BLUE, mapView);
				}

			} catch (IOException e) {
				Log.e(GoogleMapActivity.class.getSimpleName(),"Erreur MapInfo : " + e.toString());
			}

		}else {return false;}

		return true;
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
		//depart
		if(getPointDepart()!=null){
			listOfOverlays.add(new MapOverlay(getPointDepart(), R.drawable.pin_depart));
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointDepart()-->null Pointer  donc non affiche");}

		//arrivee
		if(getPointArrivee()!=null){
			listOfOverlays.add(new MapOverlay(getPointArrivee(), R.drawable.pin_arrivee));
		}else {Log.i(GoogleMapActivity.class.getSimpleName(),"makeOverlays : mapInfo.getPointArrivee()-->null Pointer  donc non affiche");}

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

	/**
	 * Traces les différents itiniraires 
	 * src-via  et via-dest
	 * @param src
	 * @param dest
	 * @param via
	 * @param color
	 * @param mMapView01
	 */
	private void drawPath(GeoPoint src,GeoPoint dest,GeoPoint via, int color, MapView mMapView01){
		if(via!=null){
			drawPath(src, via, color, mMapView01);
			drawPath(via, dest, color, mMapView01);
		}else{
			drawPath(src, dest, color, mMapView01);
		}
	}

	/**
	 * Trace l'itiniraire entre 2 points
	 * @param src
	 * @param dest
	 * @param color
	 * @param mMapView01
	 */
	private void drawPath(GeoPoint src,GeoPoint dest, int color, MapView mMapView01){
		// connect to map web service
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.google.com/maps?f=d&hl=en");
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

			if(doc.getElementsByTagName("GeometryCollection").getLength()>0)
			{
				//String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
				String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue() ;
				Log.d("xxx","path="+ path);
				String [] pairs = path.split(" "); 
				String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height
				// src
				GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
				mMapView01.getOverlays().add(new MapOverlay(startGP,startGP,MapOverlay.DEPART));
				GeoPoint gp1;
				GeoPoint gp2 = startGP; 
				double distanceIntermediaire;
				for(int i=1;i<pairs.length;i++) // the last one would be crash
				{
					lngLat = pairs[i].split(",");
					gp1 = gp2;
					// watch out! For GeoPoint, first:latitude, second:longitude
					gp2 = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
					mMapView01.getOverlays().add(new MapOverlay(gp1,gp2,MapOverlay.PATH,color));
					Log.d("xxx","pair:" + pairs[i]);
					distanceIntermediaire = Distance.calculateDistance(gp1, gp2, Distance.KILOMETERS);
					distanceTotale += Double.isNaN(distanceIntermediaire) ? 0 : distanceIntermediaire;
					//Log.i(GoogleMapActivity.class.getSimpleName(), "Distance intermediaire : "+ distanceIntermediaire);
				}
				mMapView01.getOverlays().add(new MapOverlay(dest,dest, MapOverlay.ARRIVE)); // use the default color
			} 
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
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

	public double getDistanceTotal(){
		return distanceTotale;
	}



}

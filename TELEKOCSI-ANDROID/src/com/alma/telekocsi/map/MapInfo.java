package com.alma.telekocsi.map;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.alma.telekocsi.GoogleMapActivity;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.dao.localisation.Localisation;
import com.alma.telekocsi.dao.localisation.LocalisationDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.util.LocalDate;
import com.google.android.maps.GeoPoint;

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

	public MapInfo(Context context, Profil currentProfil) {
		this.context = context;
		profil = currentProfil;
		pointsPassager = new ArrayList<GeoPoint>();
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
				Address AddressDepart=null,AddressArrivee =null;
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

				if(AddressDepart!=null && AddressArrivee!=null){
					distanceTotale = Distance.calculateDistance(AddressDepart, AddressArrivee, Distance.KILOMETERS);
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
	 * @return
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

	private Location getLastKnownLocation() {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
		if(location==null){
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) ;
		}
		return location;
	}
	

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

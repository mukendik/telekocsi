package com.alma.telekocsi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.*;

import com.alma.telekocsi.dao.profil.ProfilTest;


public class Covoiturage extends Activity implements AdapterView.OnItemSelectedListener {
	
	String[] items = {"Cholet -> Nantes",
			"Les Herbiers -> Nantes",
			"Les Herbiers -> Angers"};
	
	Trajet[] trajets = new Trajet[10];

	String[] dates = {"Lundi 15/11/2010",
			"Mardi 16/11/2010",
			"Mercredi 17/11/2010"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        /*
         * Partie transports trouvés
         */
        //setContentView(R.layout.transports);
        
        ProfilTest profilTest = new ProfilTest();
        profilTest.clear();
        profilTest.insert();
        profilTest.insert();
        profilTest.getList();
        profilTest.login();
        
        String result = resultatGAPJSON();
        
        TextView monTextView = new TextView(this);
        setContentView(monTextView);
        monTextView.setText(result);
        
        monTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}	
        });
        
        
        
        /*
         * Partie paiement et evaluation
         * ----------------------------- 
         setContentView(R.layout.main);
         */
        
        
        /*
         * Partie recherche de transports
         * ------------------------------
        setContentView(R.layout.recherche);
        Trajet trajet = new Trajet();
        trajet.setVilleDepart("Les Herbiers");
        trajet.setVilleArrivee("Nantes");
        trajet.setHeure("7h30");
        trajet.setHeureRange("5 mn");
        trajet.setJours("L M M J V S D");
        trajet.setAutoroute(false);
        trajet.setCommentaire("Pendant les périodes universitaires");
        
        trajets[0] = trajet;
        
        Spinner spinTrajet = (Spinner)findViewById(R.id.spinTrajet);
        spinTrajet.setOnItemSelectedListener(this);
        
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTrajet.setAdapter(aa);
        
        Spinner spinDateTrajet = (Spinner)findViewById(R.id.spinDateTrajet);
        
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dates);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDateTrajet.setAdapter(aa);
        */
    }


	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
	/**
	 * Necessite de mettre en place les autorisations suivantes 
	 * dans le fichier AndroidManifest.xml
	 * 
	 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
	 * @return
	 */
	private String getNetWorkInfo() {
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		
		int networkType;
		State networkState;
		
		if (networkInfo != null) {
			/* Type de connexion : réseau ou wi-fi */
			networkType = networkInfo.getType();
		
			/* Verification etat de la connexion */
			networkState = networkInfo.getState();
			
			if ((networkState != null) && (networkState.compareTo(State.CONNECTED) == 0)) {
				return "Connected : " + networkType;
			}
		}
		return "not connected";
	}
	
	public String resultatGAPJSON() {
		
		String LOG_TAG = "Client-JSON";
		String NOM_HOTE = "http://alma-telekocsi.appspot.com";
		String PATH_METHOD = "/resources/hr/employee";
		String PARAM_METHOD = "/tug@grallandco.com";
		String url = NOM_HOTE + PATH_METHOD + PARAM_METHOD;
		
		String email = "";
		String firstName = "";
		String lastName = "";
		String erreur = "";
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			
			HttpGet httpGet = new HttpGet(url);
			
			/* Permet de dialoguer en JSON sinon format XML */
			httpGet.addHeader(new BasicHeader("Accept", "application/json"));
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder stringBuilder = new StringBuilder();
				String ligneLue = bufferReader.readLine();
				while (ligneLue != null) {
					stringBuilder.append(ligneLue + "\n");
					ligneLue = bufferReader.readLine();
				}
				bufferReader.close();
				
				JSONObject jsonObject = new JSONObject(stringBuilder.toString());
				email = jsonObject.getString("email");
				firstName = jsonObject.getString("firstName");
				lastName = jsonObject.getString("lastName");
			}
			
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			erreur = e.getMessage();
			e.printStackTrace();
		}
		
		Log.i(LOG_TAG, "Networkinfo : " + getNetWorkInfo());
		Log.i(LOG_TAG, "Email : " + email);
		Log.i(LOG_TAG, "FirstName : " + firstName);
		Log.i(LOG_TAG, "LastName : " + lastName);
		
		return getNetWorkInfo() + "; email : " + email + "; firstName : " + firstName + "; lastName : " + lastName + "; erreur : " + erreur;
	}
}
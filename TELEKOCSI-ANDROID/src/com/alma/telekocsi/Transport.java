package com.alma.telekocsi;

import com.alma.telekocsi.dao.itineraire.ItineraireTestDAO;
import com.alma.telekocsi.dao.profil.ProfilTestDAO;
import com.alma.telekocsi.dao.trajet.TrajetTestDAO;

import android.app.Activity;
import android.os.Bundle;

public class Transport extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        testTrajet();
        
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	
    	testTrajet();
    }

    
    private void testProfil() {
    	
        ProfilTestDAO profilTest = new ProfilTestDAO();
        profilTest.clear();
        profilTest.insert();
        profilTest.insert();
        profilTest.getList();
        profilTest.login();
    }
    
    
    private void testItineraire() {
    	
        ItineraireTestDAO itineraireTest = new ItineraireTestDAO();
        itineraireTest.clear();
        itineraireTest.insert();
        itineraireTest.insert();
        itineraireTest.getList();    	
    }
    
    private void testTrajet() {
    	
        TrajetTestDAO trajetTest = new TrajetTestDAO();
        trajetTest.clear();
        trajetTest.insert();
        trajetTest.insert();
        trajetTest.getList();    	
    }
}
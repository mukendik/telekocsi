package com.alma.telekocsi;

import com.alma.telekocsi.dao.itineraire.ItineraireTestDAO;
import com.alma.telekocsi.dao.profil.ProfilTestDAO;

import android.app.Activity;
import android.os.Bundle;

public class Transport extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        testItineraire();
        
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	
    	testItineraire();
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
}
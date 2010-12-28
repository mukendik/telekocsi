package com.alma.telekocsi.test;

import com.alma.telekocsi.dao.avis.AvisTestDAO;
import com.alma.telekocsi.dao.event.EventTestDAO;
import com.alma.telekocsi.dao.itineraire.ItineraireTestDAO;
import com.alma.telekocsi.dao.localisation.LocalisationTestDAO;
import com.alma.telekocsi.dao.profil.ProfilTestDAO;
import com.alma.telekocsi.dao.trajet.TrajetTestDAO;
import com.alma.telekocsi.dao.transaction.TransactionTestDAO;

import android.app.Activity;
import android.os.Bundle;

public class TestDataAccessUI extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        testLocalisation();
        finish();
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	
    	testLocalisation();
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
    
    private void testTransaction() {
    	
        TransactionTestDAO transactionTest = new TransactionTestDAO();
        transactionTest.clear();
        transactionTest.insert();
        transactionTest.insert();
        transactionTest.getList();    	
    }
    
    private void testAvis() {
    	
        AvisTestDAO avisTest = new AvisTestDAO();
        avisTest.clear();
        avisTest.insert();
        avisTest.insert();
        avisTest.getList();    	
    }
    
    private void testEvent() {
    	
        EventTestDAO eventTest = new EventTestDAO();
        eventTest.clear();
        eventTest.insert();
        eventTest.insert();
        eventTest.getList();    	
    }
    
    private void testLocalisation() {
    	
        LocalisationTestDAO localisationTest = new LocalisationTestDAO();
        localisationTest.clearAll();
        localisationTest.insert();
        localisationTest.insert();
        localisationTest.getList();
        localisationTest.clear();
    }
}
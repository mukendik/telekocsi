package com.alma.telekocsi;

import com.alma.telekocsi.dao.profil.ProfilTest;

import android.app.Activity;
import android.os.Bundle;

public class Transport extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ProfilTest profilTest = new ProfilTest();
        profilTest.clear();
        profilTest.insert();
        profilTest.insert();
        profilTest.getList();
        profilTest.login();        
        
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	
        ProfilTest profilTest = new ProfilTest();
        profilTest.clear();
        profilTest.insert();
        profilTest.insert();
        profilTest.getList();
        profilTest.login();     	
    }
}
package com.alma.telekocsi;

import android.app.Activity;
import android.os.Bundle;


public class Transaction extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        /*
         * Partie paiement et evaluation
         * ----------------------------- 
         */
         setContentView(R.layout.transaction);
    }
	
}
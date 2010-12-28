package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class TrajetRecherche extends Activity implements AdapterView.OnItemSelectedListener{

	private static final int CODE_TRAJETRECHERCHE = 1;
	
	OnClickListener onClickListener = null;
	Button btRecherche;
	Button btAnnuler;
	
	
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
        
        setContentView(R.layout.trajetrecherche);
        
        btRecherche = (Button)findViewById(R.id.btTRRecherche);
        btRecherche.setOnClickListener(getOnClickListener());        

        btAnnuler = (Button)findViewById(R.id.btTRAnnuler);
        btAnnuler.setOnClickListener(getOnClickListener()); 
        
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
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CODE_TRAJETRECHERCHE:
    		switch(resultCode) {
    		case RESULT_OK:
    			// Nouvelle recherche
    			break;
    		case RESULT_CANCELED:
    			finish();
    		}
    	}
    }
    
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btAnnuler) {
						finish();
					} else if (v == btRecherche) {
						goTrajetTrouve();
					}
				}
			};
    	}
    		
    	return onClickListener;
    } 
    
    public void goTrajetTrouve() {
        Intent intent = new Intent(this, TrajetTrouve.class);
        startActivityForResult(intent, CODE_TRAJETRECHERCHE);
    }
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
}

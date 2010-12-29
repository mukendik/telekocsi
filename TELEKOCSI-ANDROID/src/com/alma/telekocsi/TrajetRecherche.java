package com.alma.telekocsi;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.itineraire.ItineraireDAO;
import com.alma.telekocsi.init.DataContext;
import com.alma.telekocsi.util.LocalDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class TrajetRecherche extends Activity implements AdapterView.OnItemSelectedListener {

	private static final int CODE_TRAJETRECHERCHE = 1;
	
	OnClickListener onClickListener = null;
	Button btRecherche;
	Button btAnnuler;

	TextView tvVilleDepart;
	TextView tvVilleArrivee;
	TextView tvHeureDepart;
	TextView tvVariableDepart;
	TextView tvJours;
	TextView tvAutoroute;

	LocalDate[] dates = new LocalDate[10];
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trajetrecherche);
        
        btRecherche = (Button)findViewById(R.id.btTRRecherche);
        btRecherche.setOnClickListener(getOnClickListener());        

        btAnnuler = (Button)findViewById(R.id.btTRAnnuler);
        btAnnuler.setOnClickListener(getOnClickListener()); 
        
        tvVilleDepart = (TextView)findViewById(R.id.tvTRVilleDepart);
        tvVilleArrivee = (TextView)findViewById(R.id.tvTRVilleArrivee);
    	tvHeureDepart = (TextView)findViewById(R.id.tvTRHeureDepart);
    	tvVariableDepart = (TextView)findViewById(R.id.tvTRVariableDepart);
    	tvJours = (TextView)findViewById(R.id.tvTRJours);
    	tvAutoroute = (TextView)findViewById(R.id.tvTRAutoroute);
        
    	initDates();
    	
        Spinner spinTrajet = (Spinner)findViewById(R.id.spinTrajet);
        spinTrajet.setOnItemSelectedListener(this);
        
        ArrayAdapter<Itineraire> aa1 = new ArrayAdapter<Itineraire>(this, android.R.layout.simple_spinner_item, getItineraires());
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTrajet.setAdapter(aa1);
        
        Spinner spinDateTrajet = (Spinner)findViewById(R.id.spinDateTrajet);
        
        ArrayAdapter<LocalDate> aa2 = new ArrayAdapter<LocalDate>(this, android.R.layout.simple_spinner_item, dates);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDateTrajet.setAdapter(aa2);
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
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Itineraire itineraire = (Itineraire)parent.getItemAtPosition(position);
		
		tvVilleDepart.setText(itineraire.getLieuDepart());
		tvVilleArrivee.setText(itineraire.getLieuDestination());
		tvHeureDepart.setText(itineraire.getHoraireDepart());
		tvVariableDepart.setText("+/-" + itineraire.getVariableDepart() + "mn");
		
		String frequence = itineraire.getFrequenceTrajet() + "NNNNNNN";
		String frequenceJour = "LMMJVSD";
		String res = "";
		for (int cpt = 0; cpt < 7; cpt++) {
			if (frequence.charAt(cpt) == 'O')
				res += frequenceJour.charAt(cpt);
			else
				res += "-";
		}
		tvJours.setText(res);
		
		
		tvAutoroute.setText(itineraire.isAutoroute() ? "oui" : "non");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	

	public List<Itineraire> getItineraires() {
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des trajets");
		ItineraireDAO itineraireDAO = new ItineraireDAO();
		List<Itineraire> itineraires = itineraireDAO.getList(DataContext.getCurrentProfil().getId());
		Log.i(TrajetRecherche.class.getSimpleName(), "Fin recherche des trajets");
		return itineraires;
	}
	
	public void initDates() {
		
		Calendar cal = new GregorianCalendar();		
		LocalDate date = new LocalDate();

		for (int cptDate = 0; cptDate < 10; cptDate++) {
			
			dates[cptDate] = date;
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
			date = new LocalDate(cal.getTimeInMillis());
		}
	}

}

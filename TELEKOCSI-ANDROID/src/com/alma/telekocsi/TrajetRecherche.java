package com.alma.telekocsi;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;
import com.alma.telekocsi.util.LocalDate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class TrajetRecherche extends ARunnableActivity {

	private static final int CODE_TRAJETRECHERCHE = 1;
	
	private OnClickListener onClickListener = null;
	private Button btRecherche;
	private Button btAnnuler;

	private TextView tvVilleDepart;
	private TextView tvVilleArrivee;
	private TextView tvHeureDepart;
	private TextView tvVariableDepart;
	private TextView tvJours;
	private TextView tvAutoroute;
	private Spinner spinTrajet;
	private Spinner spinDateTrajet;
	
	private LocalDate[] dates = new LocalDate[10];
	private LocalDate date;
	private Itineraire itineraire;
	private ArrayAdapter<Itineraire> adapterItineraire;
	private ArrayAdapter<LocalDate> adapterDate;
	private TrajetRecherche trajetRecherche = this;
	
	private OnItemSelectedListener onTrajetSelectedListener;
	private OnItemSelectedListener onDateSelectedListener;
	
	private Session session;
	
	
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			spinTrajet.setAdapter(adapterItineraire);
			spinDateTrajet.setAdapter(adapterDate);
			
	        spinTrajet.setEnabled(true);
	        spinDateTrajet.setEnabled(true);
	        
	        spinTrajet.setOnItemSelectedListener(getOnTrajetSelectedListener());
	        spinDateTrajet.setOnItemSelectedListener(getOnDateSelectedListener());
		};
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trajetrecherche);
        
        session = SessionFactory.getCurrentSession(this);
        
        btRecherche = (Button)findViewById(R.id.btTRRecherche);
        btRecherche.setOnClickListener(getOnClickListener());     
        btRecherche.setEnabled(false);

        btAnnuler = (Button)findViewById(R.id.btTRAnnuler);
        btAnnuler.setOnClickListener(getOnClickListener()); 
        
        tvVilleDepart = (TextView)findViewById(R.id.tvTRVilleDepart);
        tvVilleArrivee = (TextView)findViewById(R.id.tvTRVilleArrivee);
    	tvHeureDepart = (TextView)findViewById(R.id.tvTRHeureDepart);
    	tvVariableDepart = (TextView)findViewById(R.id.tvTRVariableDepart);
    	tvJours = (TextView)findViewById(R.id.tvTRJours);
    	tvAutoroute = (TextView)findViewById(R.id.tvTRAutoroute);
    	
    	spinTrajet = (Spinner)findViewById(R.id.spinTrajet);
    	spinDateTrajet = (Spinner)findViewById(R.id.spinDateTrajet);
    }
    
    protected void onStart() {
    	
    	super.onStart();
        
        spinTrajet.setEnabled(false);
        spinDateTrajet.setEnabled(false);
        
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				
				List<Itineraire> itineraires = session.getItineraires();
		        adapterItineraire = new ArrayAdapter<Itineraire>(trajetRecherche, android.R.layout.simple_spinner_item, itineraires);
		        adapterItineraire.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);				

		        initDates();
		        adapterDate = new ArrayAdapter<LocalDate>(trajetRecherche, android.R.layout.simple_spinner_item, dates);
		        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
			}
		});
        
        thread.start();
    }
    
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	stopProgressDialog();
    	
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
    	startProgressDialogInNewThread(this);
    }
	
	
	private OnItemSelectedListener getOnTrajetSelectedListener() {
		
		if (onTrajetSelectedListener == null) {
		
			onTrajetSelectedListener = new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					
					itineraire = (Itineraire)parent.getItemAtPosition(position);
					
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
					
					refreshBtn();
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					itineraire = null;
					refreshBtn();
				}
			};
		}
		
		return onTrajetSelectedListener;
	}

	
	private OnItemSelectedListener getOnDateSelectedListener() {
		
		if (onDateSelectedListener == null) {
		
			onDateSelectedListener = new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					
					date = (LocalDate)parent.getItemAtPosition(position);
					refreshBtn();
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					date = null;
					refreshBtn();
				}
			};
		}
		
		return onDateSelectedListener;
	}
	

	private void refreshBtn() {
		btRecherche.setEnabled((date != null) && (itineraire != null));
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


	@Override
	public void run() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("itineraire", itineraire);
        bundle.putSerializable("date", date);
        
        Intent intent = new Intent(this, TrajetTrouve.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_TRAJETRECHERCHE);
	}

}

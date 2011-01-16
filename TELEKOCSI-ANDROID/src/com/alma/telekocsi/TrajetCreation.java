package com.alma.telekocsi;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;
import com.alma.telekocsi.util.LocalDate;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class TrajetCreation extends ARunnableActivity {
		
	private Button trajetCreationButton;
	private Button cancelButton;
	private OnClickListener onClickListener = null;
	private Spinner itinerairesSpin;
	private Spinner datesSpin;
	private TextView departureText;
	private TextView arrivalText;
	private TextView departureTimeText;
	private TextView variableTimeText;
	private TextView frequencyText;
	private TextView autorouteText;
	
	private LocalDate[] dates = new LocalDate[10];
	private LocalDate date;

	private Session session;
	private Itineraire itineraire;
	private ArrayAdapter<Itineraire> adapterItineraire;
	private ArrayAdapter<LocalDate> adapterDate;
	
	private OnItemSelectedListener onItineraireSelectedListener;
	private OnItemSelectedListener onDateSelectedListener;
	
	private TrajetCreation trajetActivation = this;
	
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			itinerairesSpin.setAdapter(adapterItineraire);
			datesSpin.setAdapter(adapterDate);
			
			itinerairesSpin.setEnabled(true);
			datesSpin.setEnabled(true);
	        
	        itinerairesSpin.setOnItemSelectedListener(getOnTrajetSelectedListener());
	        datesSpin.setOnItemSelectedListener(getOnDateSelectedListener());
		};
	};

	
	final Handler handlerMsgCreate = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(trajetActivation, R.string.trajet_creation_successed, Toast.LENGTH_SHORT).show();
			refreshBtn();
			setResult(RESULT_OK);
			finish();
		};
	};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.trajet_creation);
        
        session = SessionFactory.getCurrentSession(this);
        
        trajetCreationButton = (Button)findViewById(R.id.start_trajet_creation);
        trajetCreationButton.setOnClickListener(getOnClickListener());
        trajetCreationButton.setEnabled(false);
        
        cancelButton = (Button)findViewById(R.id.cancel_trajet_creation);
        cancelButton.setOnClickListener(getOnClickListener());
        
        departureText = (TextView)findViewById(R.id.creation_departure);
        arrivalText = (TextView)findViewById(R.id.creation_arrival);
    	departureTimeText = (TextView)findViewById(R.id.creation_departure_time);
    	variableTimeText = (TextView)findViewById(R.id.creation_variable_time);
    	frequencyText = (TextView)findViewById(R.id.creation_frequency);
    	autorouteText = (TextView)findViewById(R.id.creation_autoroute);
        
        itinerairesSpin = (Spinner)findViewById(R.id.itineraires_spin);
        datesSpin = (Spinner)findViewById(R.id.dates_spin);
        
        trajetCreationButton.setText(getString(R.string.trajet_start_creation));
	}
	
	@Override
	protected void onStart() {
    	
    	super.onStart();
        
    	itinerairesSpin.setEnabled(false);
    	itinerairesSpin.setEnabled(false);
        
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				List<Itineraire> itineraires = session.getItineraires();
		        adapterItineraire = new ArrayAdapter<Itineraire>(trajetActivation, android.R.layout.simple_spinner_item, itineraires);
		        adapterItineraire.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);				

		        initDates();
		        adapterDate = new ArrayAdapter<LocalDate>(trajetActivation, android.R.layout.simple_spinner_item, dates);
		        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
			}
		});
        
        thread.start();
    }
	
	
	private OnItemSelectedListener getOnTrajetSelectedListener() {
		
		if (onItineraireSelectedListener == null) {
		
			onItineraireSelectedListener = new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					
					itineraire = (Itineraire)parent.getItemAtPosition(position);
					
					departureText.setText(itineraire.getLieuDepart());
					arrivalText.setText(itineraire.getLieuDestination());
					departureTimeText.setText(itineraire.getHoraireDepart());
					variableTimeText.setText("+/-" + itineraire.getVariableDepart() + "mn");
					
					String frequence = itineraire.getFrequenceTrajet() + "NNNNNNN";
					String frequenceJour = "LMMJVSD";
					String res = "";
					for (int cpt = 0; cpt < 7; cpt++) {
						if (frequence.charAt(cpt) == 'O')
							res += frequenceJour.charAt(cpt);
						else
							res += "-";
					}
					frequencyText.setText(res);
					autorouteText.setText(itineraire.isAutoroute() ? "oui" : "non");
					
					refreshBtn();
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					itineraire = null;
					refreshBtn();
				}
			};
		}
		
		return onItineraireSelectedListener;
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
		trajetCreationButton.setEnabled((date != null) && (itineraire != null));
	}

	
	private OnClickListener getOnClickListener(){
		if (onClickListener == null) {
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	
	private OnClickListener makeOnClickListener() {
    	
    	return new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if (v == cancelButton) {
					goBack();
				} else if (v == trajetCreationButton) {
					startTrajetCreation();
				}
			}
		};
    } 
	
	
	private void startTrajetCreation() {
		trajetCreationButton.setEnabled(false);
		startProgressDialogInNewThread(this);
	}
	
	
	private void goBack() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	
	private void creationTrajet() {
		Trajet trajet = new Trajet();
		trajet.setAutoroute(itineraire.isAutoroute());
		trajet.setCommentaire(itineraire.getCommentaire());
		trajet.setDateTrajet(date.getDateFormatCalendar());
		trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
		trajet.setHoraireDepart(itineraire.getHoraireDepart());
		trajet.setHoraireArrivee(itineraire.getHoraireArrivee());
		trajet.setIdItineraire(itineraire.getId());
		trajet.setIdProfilConducteur(itineraire.getIdProfil());
		trajet.setLieuDepart(itineraire.getLieuDepart());
		trajet.setLieuPassage1(itineraire.getLieuPassage1());
		trajet.setLieuPassage2(itineraire.getLieuPassage2());
		trajet.setLieuDestination(itineraire.getLieuDestination());
		trajet.setNbrePoint(itineraire.getNbrePoint());
		trajet.setPlaceDispo(itineraire.getPlaceDispo());
		trajet.setSoldePlaceDispo(itineraire.getPlaceDispo());
		trajet.setVariableDepart(itineraire.getVariableDepart());
		trajet.setEtat(Trajet.ETAT_DISPO);
		session.save(trajet);
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
		creationTrajet();    			
		stopProgressDialog();
        Message msg = handlerMsgCreate.obtainMessage();
        handlerMsgCreate.sendMessage(msg);		
	}

}

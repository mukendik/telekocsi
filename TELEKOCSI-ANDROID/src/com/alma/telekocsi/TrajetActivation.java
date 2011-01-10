package com.alma.telekocsi;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.trajet.Trajet;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TrajetActivation extends ARunnableActivity {
	
	private static final int ACTIVATION = 1;
	private static final int DESACTIVATION = 2;
	
	private Button trajetActivationButton;
	private Button cancelButton;
	private OnClickListener onClickListener = null;
	private Spinner routesSpin;
	private Spinner datesSpin;
	private TextView departureText;
	private TextView arrivalText;
	private TextView departureTimeText;
	private TextView variableTimeText;
	private TextView frequencyText;
	private TextView autorouteText;
	
	private String from;
	
	private LocalDate[] dates = new LocalDate[10];
	private LocalDate date;

	private Session session;
	private Itineraire itineraire;
	private ArrayAdapter<Itineraire> adapterItineraire;
	private ArrayAdapter<LocalDate> adapterDate;
	
	private OnItemSelectedListener onRouteSelectedListener;
	private OnItemSelectedListener onDateSelectedListener;
	
	private TrajetActivation routeActivation = this;
	
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			routesSpin.setAdapter(adapterItineraire);
			datesSpin.setAdapter(adapterDate);
			
			routesSpin.setEnabled(true);
			datesSpin.setEnabled(true);
	        
	        routesSpin.setOnItemSelectedListener(getOnTrajetSelectedListener());
	        datesSpin.setOnItemSelectedListener(getOnDateSelectedListener());
		};
	};

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.route_activation);
        
        session = SessionFactory.getCurrentSession(this);
        
        trajetActivationButton = (Button)findViewById(R.id.start_route_activation);
        trajetActivationButton.setOnClickListener(getOnClickListener());
        trajetActivationButton.setEnabled(false);
        
        cancelButton = (Button)findViewById(R.id.cancel_route_activation);
        cancelButton.setOnClickListener(getOnClickListener());
        
        departureText = (TextView)findViewById(R.id.activation_departure);
        arrivalText = (TextView)findViewById(R.id.activation_arrival);
    	departureTimeText = (TextView)findViewById(R.id.activation_departure_time);
    	variableTimeText = (TextView)findViewById(R.id.activation_variable_time);
    	frequencyText = (TextView)findViewById(R.id.activation_frequency);
    	autorouteText = (TextView)findViewById(R.id.activation_autoroute);
        
        routesSpin = (Spinner)findViewById(R.id.routes_spin);
        datesSpin = (Spinner)findViewById(R.id.dates_spin);
        
        from = getIntent().getExtras().getString("from");
        if(from.equals("activation")){
        	trajetActivationButton.setText(getString(R.string.start_route_activation));
        }else{
        	trajetActivationButton.setText(getString(R.string.start_route_desactivation));
        }
	}
	
	@Override
	protected void onStart() {
    	
    	super.onStart();
        
    	routesSpin.setEnabled(false);
    	routesSpin.setEnabled(false);
        
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				List<Itineraire> itineraires = session.getItineraires();
		        adapterItineraire = new ArrayAdapter<Itineraire>(routeActivation, android.R.layout.simple_spinner_item, itineraires);
		        adapterItineraire.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);				

		        initDates();
		        adapterDate = new ArrayAdapter<LocalDate>(routeActivation, android.R.layout.simple_spinner_item, dates);
		        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
			}
		});
        
        thread.start();
    }
	
	private OnItemSelectedListener getOnTrajetSelectedListener() {
		
		if (onRouteSelectedListener == null) {
		
			onRouteSelectedListener = new OnItemSelectedListener() {
				
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
		
		return onRouteSelectedListener;
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
		trajetActivationButton.setEnabled((date != null) && (itineraire != null));
	}

	private OnClickListener getOnClickListener(){
		if(onClickListener==null){
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	private OnClickListener makeOnClickListener() {
    	
    	return new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if (v==cancelButton) {
					goBack();
				}else if (v==trajetActivationButton) {
					startRouteActivation();
				}
			}
		};
    } 
	
	private void startRouteActivation(){
		startProgressDialogInNewThread(this);
	}
	
	private void goBack(){
		finish();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case ACTIVATION:
    		switch(resultCode) {
    		case RESULT_OK:
    			activeTrajet();    			
    			Toast.makeText(this, R.string.activation_successed, Toast.LENGTH_SHORT).show();
    			trajetActivationButton.setText(getText(R.string.route_activation_button_after_activation));
    			break;
    		case RESULT_CANCELED:
    			finish();
    		}
    		break;
    	case DESACTIVATION:
    		switch(resultCode) {
    		case RESULT_OK:
    			desactiveTrajet();
    			Toast.makeText(this, R.string.desactivation_successed, Toast.LENGTH_SHORT).show();
    			trajetActivationButton.setText(getText(R.string.route_activation_button_after_desactivation));
    			break;
    		case RESULT_CANCELED:
    			finish();
    		}
    	}
    	stopProgressDialog();
    }
	
	private void activeTrajet() {
		Trajet trajet = new Trajet();
		trajet.setAutoroute(itineraire.isAutoroute());
		trajet.setCommentaire(itineraire.getCommentaire());
		trajet.setDateTrajet(date.toString());
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
		session.activateRoute(trajet);
	}
	
	private void desactiveTrajet(){
		session.deactivateRoute();
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
		//si c'est le bouton pour voir les resultats
		if(trajetActivationButton.getText().equals(getString(R.string.route_activation_button_after_activation))){
			Intent intent = new Intent(this, DriverResults.class);
			intent.putExtras(bundle);
			startActivity(intent);
			stopProgressDialog();
		}
		//sinon si cest pour reactiver le trajet
		else if(trajetActivationButton.getText().equals(getString(R.string.route_activation_button_after_desactivation))){
			Intent intent = new Intent(this, TrajetActivator.class);
	        intent.putExtras(bundle);
	        startActivityForResult(intent, ACTIVATION);
		}
		else{
			//sinon on active ou on desactive selon l'appel
			if(from.equals("activation")){
		        Intent intent = new Intent(this, TrajetActivator.class);
		        intent = intent.putExtras(bundle);
		        intent = intent.putExtra("from", "activation");
		        startActivityForResult(intent, ACTIVATION);
			}
			else{
		        Intent intent = new Intent(this, TrajetActivator.class);
		        intent = intent.putExtras(bundle);
		        intent = intent.putExtra("from", "desactivation");
		        startActivityForResult(intent, DESACTIVATION);
			}
		}
	}

	
}

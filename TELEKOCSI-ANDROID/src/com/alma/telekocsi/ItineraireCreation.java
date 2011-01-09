package com.alma.telekocsi;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alma.telekocsi.checking.RouteCreationChecking;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class ItineraireCreation extends ARunnableActivity {
	
	private static final int CHECKING = 1;
	public static final String ROUTE_ARG = "routeToEdit";
	
	static final int ROUTE_FREQUENCY_DIALOG = 0;
	static private final int DEPARTURE_TIME = 1;
	static private final int ARRIVAL_TIME = 2;
	
	private Button startRouteCreationButton;
	private Button cancelRouteCreationButton;
	private Button timeDepartureButton;
	private Button timeArrivalButton;
	
	private OnClickListener onClickListener = null;
	
	private Button routeFreq;
	private Spinner placesCount;
	private RadioGroup automaticRoute;
	private EditText departure;
	private EditText intermedA;
	private EditText intermedB;
	private EditText arrival;
	private EditText price;
	private EditText comment;
	private Session session;
	private Profil profile;
	
	private TextView departurePlaceLabel;
	private TextView departureTimeLabel;
	private TextView arrivalPlaceLabel;
	private TextView arrivalTimeLabel;
	private TextView priceLabel;
	private TextView frequencyLabel;
	
	private Itineraire itineraire;
	
	private boolean create = false;
	
	/**
	 * Tableau de frequence de la meme taille que 
	 */
	private boolean[] frequencies = new boolean[]{false,false,false,false,false,false,false};
		                
	private int departureMinute, departureHour, arrivalMinute, arrivalHour;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.route_creation);
        
        startRouteCreationButton = (Button)findViewById(R.id.start_route_creation_button);
        startRouteCreationButton.setOnClickListener(getOnClickListener());

        cancelRouteCreationButton = (Button)findViewById(R.id.cancel_route_creation_button);
        cancelRouteCreationButton.setOnClickListener(getOnClickListener());
        
        placesCount = (Spinner)findViewById(R.id.route_creation_places_count_value);
        automaticRoute = (RadioGroup)findViewById(R.id.automatic_route_radio_group);
        departure = (EditText)findViewById(R.id.route_creation_departure_user);
        intermedA = (EditText)findViewById(R.id.route_creation_intermedA_user);
        intermedB = (EditText)findViewById(R.id.route_creation_intermedB_user);
        arrival = (EditText)findViewById(R.id.route_creation_arrival_user);
        price = (EditText)findViewById(R.id.route_creation_price_user);
        comment = (EditText)findViewById(R.id.route_creation_comment_user);
        routeFreq = (Button)findViewById(R.id.route_creation_frequence_user);
        session = SessionFactory.getCurrentSession(this);
        profile = session.getActiveProfile();
        
        departurePlaceLabel = (TextView)findViewById(R.id.route_creation_departure_label);
        arrivalPlaceLabel = (TextView)findViewById(R.id.route_creation_arrival_label);
        departureTimeLabel = (TextView)findViewById(R.id.route_creation_departure_time_label);
        arrivalTimeLabel = (TextView)findViewById(R.id.route_creation_arrival_time_label);
        frequencyLabel = (TextView)findViewById(R.id.route_creation_frequence_trajet_label);
        priceLabel = (TextView)findViewById(R.id.route_creation_price_label);
        
        timeDepartureButton = (Button)findViewById(R.id.time_departure_button);
        timeDepartureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
			    departureHour = c.get(Calendar.HOUR_OF_DAY);
			    departureMinute = c.get(Calendar.MINUTE);
			    showDialog(DEPARTURE_TIME);
			}
        	
        });

        timeArrivalButton = (Button)findViewById(R.id.time_arrival_button);
        timeArrivalButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
			    arrivalHour = c.get(Calendar.HOUR_OF_DAY);
			    arrivalMinute = c.get(Calendar.MINUTE);
			    showDialog(ARRIVAL_TIME);
			}
        	
        });
        
        routeFreq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(frequencies==null){
					frequencies = new boolean[]{false,false,false,false,false,false,false};
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(ItineraireCreation.this);
				builder.setTitle(R.string.route_creation_frequence);
				builder.setMultiChoiceItems(R.array.weekday_list, frequencies,new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						frequencies[which] = isChecked;
					}
					
				});
				final AlertDialog dialog;
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(dialog!=null) dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
			}
		});
        
        //========================================
        //on cree ou on modifie?
        //Chargement de l'itineraires parametres
        String itineraireId = (String)getIntent().getStringExtra(ROUTE_ARG);
        if(itineraireId==null){
        	create = true;
        }
        else{
            itineraire = session.find(Itineraire.class, itineraireId);
            if(itineraire==null){
            	finish();
            	return;
            }
            //l'utilisateur modifie un trajet donc il faut mettre a jour ses valeurs
            initValues();
        }
        
	}//onCreate()
	
	private void initValues(){
        //Chargement du trajet parametres
		placesCount.setPrompt(""+itineraire.getPlaceDispo());
		timeArrivalButton.setText(itineraire.getHoraireArrivee());
		timeDepartureButton.setText(itineraire.getHoraireDepart());
		arrival.setText(itineraire.getLieuDestination());
		departure.setText(itineraire.getLieuDepart());
		intermedA.setText(itineraire.getLieuPassage1());
		intermedB.setText(itineraire.getLieuPassage2());
		comment.setText(itineraire.getCommentaire());
		frequencies = new boolean[]{false,false,false,false,false,false,false};
		String freq = itineraire.getFrequenceTrajet();
		if(freq!=null){
			for(int i=0;i<freq.length() && i<frequencies.length;++i) frequencies[i] = 'O'==freq.charAt(i);
		}
		price.setText(""+itineraire.getNbrePoint());
		boolean isAutoroute = itineraire.isAutoroute();
		for(int i=0;i<this.automaticRoute.getChildCount();++i){
			RadioButton btn = (RadioButton)this.automaticRoute.getChildAt(i);
			if(isAutoroute){
				if(getString(R.string.yes).equals(btn.getText())){
					btn.setChecked(true);
				}else{
					btn.setChecked(false);
				}
			}else{
				if(getString(R.string.yes).equals(btn.getText())){
					btn.setChecked(false);
				}else{
					btn.setChecked(true);
				}
			}
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	 
		switch(id){
		case DEPARTURE_TIME:
			return new TimePickerDialog(this
										,onTimeDepartureSetListener
										,departureHour
										,departureMinute
										,false
			);
		case ARRIVAL_TIME:
			return new TimePickerDialog(this
										,onTimeArrivalSetListener
										,arrivalHour
										,arrivalMinute
										,false
			);
		default:
			return null;
		}
	 }
	 
	private OnTimeSetListener onTimeDepartureSetListener = new OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String min=String.valueOf(minute);
			if(minute>=0 && minute<=9){
				min = "0"+min;
			}
			String time = String.valueOf(hourOfDay)+":"+min;
			timeDepartureButton.setText(time);
		}
	};
	private OnTimeSetListener onTimeArrivalSetListener = new OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String min=String.valueOf(minute);
			if(minute>=0 && minute<=9){
				min = "0"+min;
			}
			String time = String.valueOf(hourOfDay)+":"+min;
			timeArrivalButton.setText(time);
		}
	};
	    
	
	private OnClickListener getOnClickListener(){
		if(onClickListener==null){
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	private OnClickListener makeOnClickListener(){
		
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==cancelRouteCreationButton){
					goBack();
				}
				else if(v==startRouteCreationButton){
					startRouteCreation();
				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
	private void startRouteCreation(){
		startProgressDialogInNewThread(this);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:{
    			
    			ItineraireCreation self = this;
    			if(doCreateRoute()){
    				goBack();
    				if(create){
        				Toast.makeText(self, getString(R.string.route_creation_successed), Toast.LENGTH_SHORT).show();
    				}else{
        				Toast.makeText(self, getString(R.string.route_modification_successed), Toast.LENGTH_SHORT).show();
    				}
    			}
    			else{
    				final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(self);
    				alertBuilder.setTitle(R.string.app_name);
    				if(create){
        				alertBuilder.setMessage(getString(R.string.route_creation_failed));
    				}else{
        				alertBuilder.setMessage(getString(R.string.route_modification_failed));
    				}
    				alertBuilder.show();
    			}
    		}break;
    		case RouteCreationChecking.INVALID_DEPARTURE_PLACE:
    			Toast.makeText(this, R.string.departure_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.RED);
    			departureTimeLabel.setTextColor(Color.BLACK);
    			arrivalPlaceLabel.setTextColor(Color.BLACK);
    			arrivalTimeLabel.setTextColor(Color.BLACK);
    			frequencyLabel.setTextColor(Color.BLACK);
    			priceLabel.setTextColor(Color.BLACK);
    			break;
    		case RouteCreationChecking.INVALID_ARRIVAL_PLACE:
    			Toast.makeText(this, R.string.arrival_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.BLACK);
    			departureTimeLabel.setTextColor(Color.BLACK);
    			arrivalPlaceLabel.setTextColor(Color.RED);
    			arrivalTimeLabel.setTextColor(Color.BLACK);
    			frequencyLabel.setTextColor(Color.BLACK);
    			priceLabel.setTextColor(Color.BLACK);
    			break;
    		case RouteCreationChecking.INVALID_DEPARTURE_TIME:
    			Toast.makeText(this, R.string.departure_time_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.BLACK);
    			departureTimeLabel.setTextColor(Color.RED);
    			arrivalPlaceLabel.setTextColor(Color.BLACK);
    			arrivalTimeLabel.setTextColor(Color.BLACK);
    			frequencyLabel.setTextColor(Color.BLACK);
    			priceLabel.setTextColor(Color.BLACK);
    			break;
    		case RouteCreationChecking.INVALID_ARRIVAL_TIME:
    			Toast.makeText(this, R.string.arrival_time_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.BLACK);
    			departureTimeLabel.setTextColor(Color.BLACK);
    			arrivalPlaceLabel.setTextColor(Color.BLACK);
    			arrivalTimeLabel.setTextColor(Color.RED);
    			frequencyLabel.setTextColor(Color.BLACK);
    			priceLabel.setTextColor(Color.BLACK);
    			break;
    		case RouteCreationChecking.INVALID_TIMES:
    			Toast.makeText(this, R.string.times_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.BLACK);
    			departureTimeLabel.setTextColor(Color.RED);
    			arrivalPlaceLabel.setTextColor(Color.BLACK);
    			arrivalTimeLabel.setTextColor(Color.BLACK);
    			frequencyLabel.setTextColor(Color.BLACK);
    			priceLabel.setTextColor(Color.BLACK);
    			break;
    		case RouteCreationChecking.INVALID_FREQUENCY:
    			Toast.makeText(this, R.string.frequency_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.BLACK);
    			departureTimeLabel.setTextColor(Color.BLACK);
    			arrivalPlaceLabel.setTextColor(Color.BLACK);
    			arrivalTimeLabel.setTextColor(Color.BLACK);
    			frequencyLabel.setTextColor(Color.RED);
    			priceLabel.setTextColor(Color.BLACK);
    			break;
    		case RouteCreationChecking.INVALID_PRICE:
    			Toast.makeText(this, R.string.price_error, Toast.LENGTH_SHORT).show();
    			departurePlaceLabel.setTextColor(Color.BLACK);
    			departureTimeLabel.setTextColor(Color.BLACK);
    			arrivalPlaceLabel.setTextColor(Color.BLACK);
    			arrivalTimeLabel.setTextColor(Color.BLACK);
    			frequencyLabel.setTextColor(Color.BLACK);
    			priceLabel.setTextColor(Color.RED);
    			break;
    		}
    	}
		stopProgressDialog();
    }
	
	/**
	 * Cr�ation de l'itineraire
	 * @return true en cas de succ�s
	 */
	protected boolean doCreateRoute(){
		
		RadioButton rb = (RadioButton)findViewById(automaticRoute.getCheckedRadioButtonId());
		boolean autoroute = rb!=null && getString(R.string.yes).equals(rb.getText().toString());

		Itineraire iti = null;
		if(create){
			iti = new Itineraire();
		}else{
			iti = session.find(Itineraire.class, itineraire.getId());
		}
		iti.setLieuDepart(departure.getText().toString());
		iti.setLieuPassage1(intermedA.getText().toString());
		iti.setLieuPassage2(intermedB.getText().toString());
		iti.setLieuDestination(arrival.getText().toString());
		iti.setHoraireDepart(timeDepartureButton.getText().toString());
		iti.setHoraireArrivee(timeArrivalButton.getText().toString());
		iti.setCommentaire(comment.getText().toString());
		iti.setIdProfil(profile.getId());
		iti.setPlaceDispo(Integer.valueOf(placesCount.getSelectedItem().toString()));
		iti.setAutoroute(autoroute);
		iti.setNbrePoint(Integer.valueOf(price.getText().toString()));
		iti.setVariableDepart("5");
		
		//La fréquence du trajet
		String freq = "";
		for(boolean freqBool : frequencies) freq += freqBool?"O":"N";
		iti.setFrequenceTrajet(freq);
		
		if(create){
			iti = session.save(iti);
		}else{
			iti = session.update(iti);
		}
		
		return true;
		/*
		if(itineraire!=null){
			if(create){
				trajet = new Trajet();
			}
			trajet.setAutoroute(autoroute);
			trajet.setPlaceDispo(Integer.valueOf(placesCount.getSelectedItem().toString()));
			trajet.setLieuDepart(itineraire.getLieuDepart());
			trajet.setLieuDestination(itineraire.getLieuDestination());
			trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
			trajet.setHoraireDepart(timeDepartureButton.getText().toString());
			trajet.setHoraireArrivee(timeArrivalButton.getText().toString());
			trajet.setCommentaire(comment.getText().toString());
			trajet.setIdProfilConducteur(profile.getId());
			trajet.setIdItineraire(itineraire.getId());
			trajet.setNbrePoint(Integer.valueOf(price.getText().toString()));
			trajet = session.save(trajet);
			
			return trajet!=null;
		}
		
		return false;*/
	}

	@Override
	public void run() {
		Intent intent = new Intent(this, RouteCreationChecking.class);
    	intent = intent.putExtra("departurePlace", departure.getText().toString());
    	intent = intent.putExtra("arrivalPlace", arrival.getText().toString());
    	intent = intent.putExtra("departureTime", timeDepartureButton.getText().toString());
    	intent = intent.putExtra("arrivalTime", timeArrivalButton.getText().toString());
    	intent = intent.putExtra("frequency", hasAtLeastOneFrequency());
    	intent = intent.putExtra("price", price.getText().toString());
    	startActivityForResult(intent, CHECKING);
	}
	
	private boolean hasAtLeastOneFrequency(){
		boolean res = false;
		for(boolean freqBool : frequencies) res |= freqBool;
		return res;
	}
	
}

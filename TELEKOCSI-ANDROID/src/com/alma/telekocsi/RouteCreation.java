package com.alma.telekocsi;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class RouteCreation extends ARunnableActivity {
	static final int ROUTE_FREQUENCY_DIALOG = 0;
	
	private Button startRouteCreationButton;
	private Button cancelRouteCreationButton;
	private Button timeDepartureButton;
	private Button timeArrivalButton;
	private OnClickListener onClickListener = null;
	
	private Button routeFreq;
	private Spinner placesCount;
	private RadioGroup automaticRoute;
	private EditText departure;
	private EditText arrival;
	private EditText departureTime;
	private EditText arrivalTime;
	private EditText price;
	private EditText comment;
	private Session session;
	private Profil profile;
	
	private int departureMinute, departureHour, arrivalMinute, arrivalHour;
	static private final int DEPARTURE_TIME = 1;
	static private final int ARRIVAL_TIME = 2;

	/**
	 * Tableau de frequence de la meme taille que 
	 */
	private boolean[] frequencies = new boolean[]{false,false,false,false,false,false,false,false};;
		                
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
        arrival = (EditText)findViewById(R.id.route_creation_arrival_user);
        departureTime = (EditText)findViewById(R.id.route_creation_departure_time_user);
        arrivalTime = (EditText)findViewById(R.id.route_creation_arrival_time_user);
        price = (EditText)findViewById(R.id.route_creation_price_user);
        comment = (EditText)findViewById(R.id.route_creation_comment_user);
        routeFreq = (Button)findViewById(R.id.route_creation_frequence_user);
        session = SessionFactory.getCurrentSession(this);
        profile = session.getActiveProfile();
        
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
					frequencies = new boolean[]{false,false,false,false,false,false,false,false};
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(RouteCreation.this);
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
        
	}//onCreate()
	
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
			String time = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
			timeDepartureButton.setText(time);
		}
	};
	private OnTimeSetListener onTimeArrivalSetListener = new OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String time = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
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
		startProgressDialog(this);
    	Thread thread = new Thread(this);
    	thread.start();
		stopProgressDialog();
	}
	
	/**
	 * Cr�ation du trajet
	 * @return true en cas de succ�s
	 */
	protected boolean doCreateRoute(){		
		RadioButton rb = (RadioButton)findViewById(automaticRoute.getCheckedRadioButtonId());
		boolean autoroute = rb!=null && getString(R.string.yes).equals(rb.getText().toString());

		Itineraire itineraire = new Itineraire();
		itineraire.setLieuDepart(departure.getText().toString());
		itineraire.setLieuDestination(arrival.getText().toString());
		itineraire.setCommentaire(comment.getText().toString());
		itineraire.setIdProfil(profile.getId());
		itineraire.setPlaceDispo(Integer.valueOf(placesCount.getSelectedItem().toString()));
		itineraire.setAutoroute(autoroute);
		
		//La fréquence du trajet
		String freq = "";
		for(boolean freqBool : frequencies) freq += freqBool?"O":"N";
		itineraire.setFrequenceTrajet(freq);
		
		itineraire = session.save(itineraire);

		if(itineraire!=null){
			Trajet trajet = new Trajet();
			trajet.setAutoroute(autoroute);
			trajet.setPlaceDispo(Integer.valueOf(placesCount.getSelectedItem().toString()));
			trajet.setLieuDepart(itineraire.getLieuDepart());
			trajet.setLieuDestination(itineraire.getLieuDestination());
			trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
			trajet.setHoraireDepart(departureTime.getText().toString());
			trajet.setHoraireArrivee(arrivalTime.getText().toString());
			trajet.setCommentaire(comment.getText().toString());
			trajet.setIdProfilConducteur(profile.getId());
			trajet.setIdItineraire(itineraire.getId());
			trajet.setNbrePoint(Integer.valueOf(price.getText().toString()));
			trajet = session.save(trajet);
			
			return trajet!=null;
		}
		
		return false;
	}

	@Override
	public void run() {
		RouteCreation self = this;
		//FIXME Ajouter la v�rifaction des valeurs
		if(doCreateRoute()){
			goBack();
		}
		else{
			final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(self);
			alertBuilder.setTitle(R.string.app_name);
			alertBuilder.setMessage(getString(R.string.route_creation_failed));
			alertBuilder.show();
		}
	}
	
}

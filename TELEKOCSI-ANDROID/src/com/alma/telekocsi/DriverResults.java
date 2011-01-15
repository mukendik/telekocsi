package com.alma.telekocsi;

import java.util.List;

import com.alma.telekocsi.TrajetTrouve.CheckAdapter;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;
import com.alma.telekocsi.util.LocalDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class DriverResults extends ARunnableActivity {
	
private static final int CODE_TRAJETTROUVE = 20;
	
	private OnClickListener onClickListener = null;
	private Button quit;

	private TextView departureText;
	private TextView arrivalText;
	private TextView dateText;
	private TextView resultsText;
	private TextView pagesText;
	
	private ListView resultsList;
	private ArrayAdapter<Trajet> routesDetailsAdapter;
	private OnItemSelectedListener onTrajetSelectedListener;
	private OnItemClickListener onItemClickListener;
	private DriverResults driverResults = this;
	
	private List<Trajet> routes = null;
	private Itineraire itineraire = null;
	private LocalDate date = null;
	
	private Session session;
		
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
		
			resultsText.setText(routes.size() + " résultats");
			pagesText.setText("Page : 1/1");
			
			resultsList.setAdapter(routesDetailsAdapter);
			resultsList.setOnItemSelectedListener(getOnTrajetSelectedListener());
			resultsList.setOnItemClickListener(getOnItemClickListener());
			
			if (routes.size() == 0) {
				Toast.makeText(driverResults,getString(R.string.no_results_for_driver), Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private OnItemClickListener getOnItemClickListener() {
		
		onItemClickListener = new OnItemClickListener() {
			
		    public void onItemClick(AdapterView parent, View v, int position, long id) {
		        Toast.makeText(driverResults,"Gérer un event vers le conducteur",Toast.LENGTH_SHORT).show();
		        goTrajetDetail();
		    }
		};
		
		return onItemClickListener;
	}
	
	private OnItemSelectedListener getOnTrajetSelectedListener() {
		
		if (onTrajetSelectedListener == null) {
		
			onTrajetSelectedListener = new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					
					Trajet model = (Trajet)parent.getItemAtPosition(position);
					Log.i(DriverResults.class.getSimpleName(), "onItemSelected -> " + model);
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			};
		}
		
		return onTrajetSelectedListener;
	}

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.results_for_driver);
        
        session = SessionFactory.getCurrentSession(this);
        
        quit = (Button)findViewById(R.id.driver_results_quit_button);
        quit.setOnClickListener(getOnClickListener()); 
        
        departureText = (TextView)findViewById(R.id.driver_results_departure);
        arrivalText = (TextView)findViewById(R.id.driver_results_arrival);
        dateText = (TextView)findViewById(R.id.driver_results_date);
        resultsText = (TextView)findViewById(R.id.driver_results_counts);
        pagesText = (TextView)findViewById(R.id.driver_results_pages);
        
		// Tests
        resultsList = (ListView)findViewById(R.id.driver_results_list_id);
    }
	
	@Override
    protected void onStart() {

    	super.onStart();
    	chargeInfoIntent();
    	
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				routesDetailsAdapter = new CheckAdapter(driverResults, getRoutes());
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
			}
		});
        
        thread.start();
    }
	
	public void chargeInfoIntent() {
        Bundle bundle = this.getIntent().getExtras();
        itineraire = (Itineraire)bundle.getSerializable("itineraire");
        date = (LocalDate)bundle.getSerializable("date");
        
        departureText.setText(itineraire.getLieuDepart());
        arrivalText.setText(itineraire.getLieuDestination());
        dateText.setText(date.toString());
        
        /*
        tvResultat.setText("12 résultats");
        tvPage.setText("Page : 2/5");*/
        
        Log.i(DriverResults.class.getSimpleName(), " Itineraire : " + itineraire);
        Log.i(DriverResults.class.getSimpleName(), " Date : " + date);
        
    }
	
	public List<Trajet> getRoutes() {
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des trajets");
		Trajet trajetModel = new Trajet();
		trajetModel.setLieuDepart(itineraire.getLieuDepart());
		trajetModel.setLieuDestination(itineraire.getLieuDestination());
		trajetModel.setDateTrajet(date.getDateFormatCalendar());
		
		routes = session.getTrajets(trajetModel);
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Fin recherche des trajets : " + routes.size());
		return routes;
	}

	class CheckAdapter extends ArrayAdapter<Trajet> {
		
		Activity context;
		
		CheckAdapter(Activity context, List<Trajet> list) {
			
			super(context, R.layout.trajettrouverow, list);
			this.context = context;
		}
		
		
		/**
		 * Fixer la taille des champs pour avoir la place suffisante
		 * pour tout afficher !
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			ViewWrapper wrapper;
													
			if (row == null) {
				
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.trajettrouverow, null);
				wrapper = new ViewWrapper(row);
				row.setTag(wrapper);
			} else {
				wrapper = (ViewWrapper)row.getTag();
			}

			Trajet trajet = getModel(position);
			
			StringBuilder dateAff = new StringBuilder();
			String dateJour = new LocalDate().getDateFormatCalendar();
			if (trajet.getDateTrajet().equals(dateJour)) {
				dateAff.append("Aujourd'hui");
			} else {
				dateAff.append(trajet.getDateTrajet());
			}
			dateAff.append(" à " + trajet.getHoraireDepart());
			
			wrapper.getDateHeure().setText(dateAff.toString());
			wrapper.getNbrePlaceDispo().setText(trajet.getPlaceDispo() + " places dispo");
			wrapper.getNbrePoint().setText(trajet.getNbrePoint() + " points");
			
			Profil profil = session.find(Profil.class, trajet.getIdProfilConducteur());
			
			wrapper.getNombreAvis().setText(profil.getNombreAvis() + " avis");
			wrapper.getNomConducteur().setText(profil.getPseudo());
			
			return (row);
		}
	}
	
	private Trajet getModel(int position) {
		return routesDetailsAdapter.getItem(position);
	}  
    
    @Override
    protected void onRestart() {
    	super.onRestart();

    }
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == quit) {
						setResult(RESULT_CANCELED);
						finish();
					}
				}
			};
    	}
    	
    	return onClickListener;
    }
	
	public void goTrajetDetail() {
    	startProgressDialogInNewThread(this);
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CODE_TRAJETTROUVE:
    		switch(resultCode) {
    		case RESULT_OK:
    			// Nouvelle recherche
    			break;
    		case RESULT_CANCELED:
    			finish();
    		}
    	}
    	stopProgressDialog();
    }

	@Override
	public void run() {
		Bundle bundle = new Bundle();
        bundle.putSerializable("itineraire", "");
        bundle.putSerializable("date", "");
        
        Intent intent = new Intent(this, TrajetDetail.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_TRAJETTROUVE);
	}

}

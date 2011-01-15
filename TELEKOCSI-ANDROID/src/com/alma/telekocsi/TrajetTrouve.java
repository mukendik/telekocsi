package com.alma.telekocsi;

import java.util.List;

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


public class TrajetTrouve extends ARunnableActivity {
	
	private static final int CODE_TRAJETTROUVE = 20;
	
	private OnClickListener onClickListener = null;
	private Button btNewSerach;
	private Button btQuit;

	private TextView tvVilleDepart;
	private TextView tvVilleArrivee;
	private TextView tvDate;
	private TextView tvResultat;
	private TextView tvPage;
	
	private ListView listView;
	private ArrayAdapter<Trajet> detailTrajetsAdapter;
	private OnItemSelectedListener onTrajetSelectedListener;
	private OnItemClickListener onItemClickListener;
	private TrajetTrouve trajetTrouve = this;
	
	private List<Trajet> trajets = null;
	private Itineraire itineraire = null;
	private LocalDate date = null;
	private Trajet trajet = null;
	
	private Session session;
		
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
		
			tvResultat.setText(trajets.size() + " résultats");
			tvPage.setText("Page : 1/1");
			
			listView.setAdapter(detailTrajetsAdapter);
			listView.setOnItemSelectedListener(getOnTrajetSelectedListener());
			listView.setOnItemClickListener(getOnItemClickListener());
			
			if (trajets.size() == 0) {
				Toast.makeText(trajetTrouve, "Aucun trajet ne correspond à vos critères", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	private OnItemClickListener getOnItemClickListener() {
		
		onItemClickListener = new OnItemClickListener() {
			
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	
		    	listView.setOnItemClickListener(null);
		    	trajet = (Trajet)parent.getItemAtPosition(position);
		        Toast.makeText(trajetTrouve,"Contacter le conducteur", Toast.LENGTH_SHORT).show();
		        goTrajetDetail();
		    }
		};
		
		return onItemClickListener;
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trajettrouve);
        
        session = SessionFactory.getCurrentSession(this);
        
        btNewSerach = (Button)findViewById(R.id.btTTNewSearch);
        btNewSerach.setOnClickListener(getOnClickListener());
        
        btQuit = (Button)findViewById(R.id.btTTQuit);
        btQuit.setOnClickListener(getOnClickListener()); 
        
        tvVilleDepart = (TextView)findViewById(R.id.tvTTVilleDepart);
        tvVilleArrivee = (TextView)findViewById(R.id.tvTTVilleArrivee);
        tvDate = (TextView)findViewById(R.id.tvTTDate);
        tvResultat = (TextView)findViewById(R.id.tvTTResultat);
        tvPage = (TextView)findViewById(R.id.tvTTPage);
        
		// Tests
        listView = (ListView)findViewById(R.id.lvTTListConducteur);
    }
    
    
    @Override
    protected void onStart() {

    	super.onStart();
    	chargeInfoIntent();
    	
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				detailTrajetsAdapter = new CheckAdapter(trajetTrouve, getTrajets());
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
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btQuit) {
						setResult(RESULT_CANCELED);
						finish();
					} else if (v == btNewSerach) {
						setResult(RESULT_OK);
						finish();			
					}
				}
			};
    	}
    	
    	return onClickListener;
    }
    
    
	private OnItemSelectedListener getOnTrajetSelectedListener() {
		
		if (onTrajetSelectedListener == null) {
		
			onTrajetSelectedListener = new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					
					Trajet model = (Trajet)parent.getItemAtPosition(position);
					Log.i(TrajetTrouve.class.getSimpleName(), "onItemSelected -> " + model);
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			};
		}
		
		return onTrajetSelectedListener;
	}

    
    public void chargeInfoIntent() {
        
        Bundle bundle = this.getIntent().getExtras();
        itineraire = (Itineraire)bundle.getSerializable("itineraire");
        date = (LocalDate)bundle.getSerializable("date");
        
        tvVilleDepart.setText(itineraire.getLieuDepart());
        tvVilleArrivee.setText(itineraire.getLieuDestination());
        tvDate.setText(date.toString());
        
        /*
        tvResultat.setText("12 résultats");
        tvPage.setText("Page : 2/5");*/
        
        Log.i(TrajetTrouve.class.getSimpleName(), " Itineraire : " + itineraire);
        Log.i(TrajetTrouve.class.getSimpleName(), " Date : " + date);
        
    }  
    

    private Trajet getModel(int position) {
		
		return detailTrajetsAdapter.getItem(position);
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
			
			wrapper.getVisage().setImageResource(Profile.getImageResource(profil.getSexe()));
			wrapper.getStarsClassement().setImageResource(Profile.getClassementStarImageResource(profil.getClassementMoyen()));
			
			return (row);
		}
	}
	
	
	public List<Trajet> getTrajets() {
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des trajets");
		
		Trajet trajetModel = new Trajet();
		trajetModel.setLieuDepart(itineraire.getLieuDepart());
		trajetModel.setLieuDestination(itineraire.getLieuDestination());
		trajetModel.setDateTrajet(date.getDateFormatCalendar());
		
		trajets = session.getTrajets(trajetModel);
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Fin recherche des trajets : " + trajets.size());
		return trajets;
	}

	
    public void goTrajetDetail() {
    	startProgressDialogInNewThread(this);
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	listView.setOnItemClickListener(getOnItemClickListener());
    	stopProgressDialog();
    	
    	switch (requestCode) {
    	case CODE_TRAJETTROUVE:
    		switch(resultCode) {
    		case RESULT_OK:
    			// on continue....
    			break;
    		case RESULT_CANCELED:
    			finish();
    		}
    	}
    }    
    
    
	@Override
	public void run() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("trajet", trajet);
        
        Intent intent = new Intent(this, TrajetDetail.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_TRAJETTROUVE);
	}
	
}
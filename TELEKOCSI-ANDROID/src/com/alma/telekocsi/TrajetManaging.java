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


public class TrajetManaging extends ARunnableActivity {
	
	private static final int CODE_TRAJETTROUVE = 30;
	
	private OnClickListener onClickListener = null;
	private Button btNew;
	private Button btQuit;

	private TextView tvResultat;
	private TextView tvPage;
	
	private ListView listView;
	private ArrayAdapter<Trajet> detailTrajetsAdapter;
	private OnItemSelectedListener onTrajetSelectedListener;
	private OnItemClickListener onItemClickListener;
	private TrajetManaging trajetManaging = this;
	
	private List<Trajet> trajets = null;
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
				Toast.makeText(trajetManaging, "Aucun trajet disponibles", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	private OnItemClickListener getOnItemClickListener() {
		
		onItemClickListener = new OnItemClickListener() {
			
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	
		    	listView.setOnItemClickListener(null);
		    	trajet = (Trajet)parent.getItemAtPosition(position);
		        Toast.makeText(trajetManaging,"click trajet !", Toast.LENGTH_SHORT).show();
		        //goTrajetDetail();
		    }
		};
		
		return onItemClickListener;
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trajet_managing);
        
        session = SessionFactory.getCurrentSession(this);
        
        btNew = (Button)findViewById(R.id.btTMNewTrajet);
        btNew.setOnClickListener(getOnClickListener());
        
        btQuit = (Button)findViewById(R.id.btTMQuit);
        btQuit.setOnClickListener(getOnClickListener()); 
        
        tvResultat = (TextView)findViewById(R.id.tvTMResultat);
        tvPage = (TextView)findViewById(R.id.tvTMPage);
        
		// Liste des trajets du conducteur
        listView = (ListView)findViewById(R.id.lvTMListTrajet);
    }
    
    
    @Override
    protected void onStart() {

    	super.onStart();
    	
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				detailTrajetsAdapter = new TrajetAdapter(trajetManaging, getTrajets());
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
					} else if (v == btNew) {
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
					Log.i(TrajetManaging.class.getSimpleName(), "onItemSelected -> " + model);
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			};
		}
		
		return onTrajetSelectedListener;
	}
    

    private Trajet getModel(int position) {
		
		return detailTrajetsAdapter.getItem(position);
	}  
    
	
	class TrajetAdapter extends ArrayAdapter<Trajet> {
		
		Activity context;
		
		TrajetAdapter(Activity context, List<Trajet> list) {
			
			super(context, R.layout.trajet_managing_row, list);
			this.context = context;
		}
		
		
		/**
		 * Fixer la taille des champs pour avoir la place suffisante
		 * pour tout afficher !
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			TrajetManagingViewWrapper wrapper;
													
			if (row == null) {
				
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.trajet_managing_row, null);
				wrapper = new TrajetManagingViewWrapper(row);
				row.setTag(wrapper);
			} else {
				wrapper = (TrajetManagingViewWrapper)row.getTag();
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
			
			wrapper.getDate().setText("Date départ : " + dateAff.toString());
			wrapper.getDepart().setText( "De : " + trajet.getLieuDepart());
			wrapper.getArrivee().setText(" A : " + trajet.getLieuDestination());
			wrapper.getInfo().setText("Nbre places : " + trajet.getPlaceDispo()
					+ " Points : " + trajet.getNbrePoint());
						
			return (row);
		}
	}
	
	
	public List<Trajet> getTrajets() {
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des trajets");
		
		trajets = session.getTrajets();
		
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
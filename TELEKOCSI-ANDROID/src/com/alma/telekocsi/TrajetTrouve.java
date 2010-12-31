package com.alma.telekocsi;

import java.util.ArrayList;
import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.util.LocalDate;

import android.app.Activity;
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
import android.widget.AdapterView.OnItemSelectedListener;


public class TrajetTrouve extends Activity {
    
	private OnClickListener onClickListener = null;
	private Button btNewSerach;
	private Button btQuit;

	private TextView tvVilleDepart;
	private TextView tvVilleArrivee;
	private TextView tvDate;
	private TextView tvResultat;
	private TextView tvPage;
	
	private ListView listView;
	ArrayList<RowModel> list;
	private ArrayAdapter<RowModel> detailTrajetsAdapter;
	private OnItemSelectedListener onTrajetSelectedListener;
	private TrajetTrouve trajetTrouve = this;
	private Trajet trajet = null;
	
	
	String[] items={"lorem", "ipsum", "dolor", "sit", "amet",
			"consectetuer", "adipiscing", "elit", "morbi", "vel",
			"ligula", "vitae", "arcu", "aliquet", "mollis",
			"etiam", "vel", "erat", "placerat", "ante",
			"porttitor", "sodales", "pellentesque", "augue",
			"purus"};
	
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
		
			listView.setAdapter(detailTrajetsAdapter);
			listView.setOnItemSelectedListener(getOnTrajetSelectedListener());
		};
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trajettrouve);
        
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
		
				detailTrajetsAdapter = new CheckAdapter(trajetTrouve, getListTrajets());
						        
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
			}
		});
        
        thread.start();
    }
    
    
    List<RowModel> getListTrajets() {
    	
		ArrayList<RowModel> list = new ArrayList<RowModel>();
		for (String s : items) {
			list.add(new RowModel(s));
		}
    	return list;
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
					
					
					
					RowModel model = (RowModel)parent.getItemAtPosition(position);
					Log.i(TrajetTrouve.class.getSimpleName(), "onItemSelected -> " + model);
					
					//trajet = (Trajet)parent.getItemAtPosition(position);
					
					/*
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
					
					refreshBtn();*/
				}
				
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					/*
					itineraire = null;
					refreshBtn();*/
				}
			};
		}
		
		return onTrajetSelectedListener;
	}    

    
    public void chargeInfoIntent() {
        
        Bundle bundle = this.getIntent().getExtras();
        Itineraire itineraire = (Itineraire)bundle.getSerializable("itineraire");
        LocalDate date = (LocalDate)bundle.getSerializable("date");
        
        tvVilleDepart.setText(itineraire.getLieuDepart());
        tvVilleArrivee.setText(itineraire.getLieuDestination());
        tvDate.setText(date.toString());
        tvResultat.setText("12 résultats");
        tvPage.setText("Page : 2/5");
        
        
        Log.i(TrajetTrouve.class.getSimpleName(), " Itineraire : " + itineraire);
        Log.i(TrajetTrouve.class.getSimpleName(), " Date : " + date);
        
    }  
    
    //--------------------------------------------------------
    // TESTS
    //--------------------------------------------------------
    
	private RowModel getModel(int position) {
		
		return detailTrajetsAdapter.getItem(position);
	}  
    
	
	class CheckAdapter extends ArrayAdapter<RowModel> {
		
		Activity context;
		
		CheckAdapter(Activity context, List<RowModel> list) {
			
			super(context, R.layout.trajettrouverow, list);
			this.context = context;
		}
		
		
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

			RowModel model = getModel(position);
			
			wrapper.getDateHeure().setText("Aujourd'hui à 14h30");
			wrapper.getNbrePlaceDispo().setText("2 places dispo");
			wrapper.getNbrePoint().setText(position + " points");
			wrapper.getNombreAvis().setText("1 avis");
			wrapper.getNomConducteur().setText("Dupond G.");
			
			return (row);
		}
	}
	
	
	class RowModel {
		String label;
		float rating = 2.0f;
		
		RowModel(String label) {
			this.label = label;
		}
		
		public String toString() {
			if (rating >= 3.0) {
				return (label.toUpperCase());
			}
			
			return (label);
		}
	}
}
package com.alma.telekocsi;

import java.util.List;

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class TrajetManaging extends ARunnableActivity {
	
	private static final int CODE_TRAJETMANAGING = 40;
	
	private static final int MODIFY = 1;
	private static final int ACTIVATE = 2;
	private static final int DELETE = 3;
	
	private OnClickListener onClickListener = null;
	private Button btNew;
	private Button btQuit;

	private TextView tvResultat;
	private TextView tvPage;
	
	private ListView listView;
	private ArrayAdapter<Trajet> detailTrajetsAdapter;
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
			registerForContextMenu(listView);
			
			if (trajets.size() == 0) {
				Toast.makeText(trajetManaging, "Aucun trajet disponibles", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
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
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MODIFY, 0, R.string.routes_managing_modify);
		menu.add(0, ACTIVATE, 0,  R.string.routes_managing_activate);
		menu.add(0, DELETE, 0,  R.string.routes_managing_delete);
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo infos = (AdapterContextMenuInfo) item.getMenuInfo();
	
		trajet = detailTrajetsAdapter.getItem(infos.position);
				
		switch (item.getItemId()) {
		case MODIFY:
			Log.i(getClass().getSimpleName(), "modify : " + trajet);
			return true;
		case ACTIVATE:
			Log.i(getClass().getSimpleName(), "activate : " + trajet );
			return true;
		case DELETE:
			Log.i(getClass().getSimpleName(), "delete : " + trajet);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}    
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btQuit) {
						finish();
					} else if (v == btNew) {
						goTrajetCreation();
					}
				}
			};
    	}
    	
    	return onClickListener;
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
			
			wrapper.getDate().setText("Date : " + dateAff.toString());
			wrapper.getDepart().setText( "Départ  : " + trajet.getLieuDepart() + " à " + trajet.getHoraireDepart());
			wrapper.getArrivee().setText("Arrivée : " + trajet.getLieuDestination() + " à " + trajet.getHoraireArrivee());
			wrapper.getInfo().setText("Places dispo : " + trajet.getPlaceDispo()
					+ "; Points : " + trajet.getNbrePoint());
						
			return (row);
		}
	}
	
	
	public List<Trajet> getTrajets() {
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des trajets");
		
		trajets = session.getTrajets();
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Fin recherche des trajets : " + trajets.size());
		return trajets;
	}

	
    public void goTrajetCreation() {
    	startProgressDialogInNewThread(this);
    }

    
    public void goTrajetAction() {
    	//
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CODE_TRAJETMANAGING:
    		switch(resultCode) {
    		case RESULT_OK:
				detailTrajetsAdapter = new TrajetAdapter(trajetManaging, getTrajets());
				stopProgressDialog();
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
    			break;
    		case RESULT_CANCELED:
    			stopProgressDialog();
    			break;
    		}
    	}
    }
    
    
	@Override
	public void run() {
        
        Intent intent = new Intent(this, TrajetCreation.class);
        startActivityForResult(intent, CODE_TRAJETMANAGING);
	}
}
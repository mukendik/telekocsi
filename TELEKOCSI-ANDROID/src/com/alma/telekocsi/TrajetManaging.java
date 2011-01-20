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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;


public class TrajetManaging extends ARunnableActivity {
	
	private static final int CODE_TRAJETMANAGING = 40;
	
	private static final int ACTION_ACTIVATE = 1;
	private static final int ACTION_DESACTIVATE = 2;
	private static final int ACTION_DELETE = 3;
	private static final int ACTION_MAP = 4;
	private static final int ACTION_CREATE = 5;
	private static final int ACTION_PASSAGER = 6;
	
	private int currentAction = 0;
	
	private OnClickListener onClickListener = null;
	private OnItemLongClickListener onItemLongClickListener;
	private Button btNew;
	private Button btQuit;
	
	private MenuItem menuActivate;
	private MenuItem menuDesactivate;
	private MenuItem menuDelete;
	private MenuItem menuMap;
	private MenuItem menuPassager;

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
			listView.setOnItemLongClickListener(getOnItemLongClickListener());
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
        
		/* Liste des trajets du conducteur */
        listView = (ListView)findViewById(R.id.lvTMListTrajet);
    }
    
    
    @Override
    protected void onStart() {

    	super.onStart();
    	
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				loadTrajets();
			}
		});
        
        thread.start();
    }
       
    
    private void loadTrajets() {
    	
		detailTrajetsAdapter = new TrajetAdapter(trajetManaging, getTrajets());
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }
    
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    }
    
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menuActivate = menu.add(0, ACTION_ACTIVATE, 0,  R.string.tm_item_activation);
		menuDesactivate = menu.add(0, ACTION_DESACTIVATE, 0,  R.string.tm_item_desactivation);
		menuDelete = menu.add(0, ACTION_DELETE, 0,  R.string.tm_item_suppression);
		menuMap = menu.add(0, ACTION_MAP, 0,  R.string.tm_item_map);
		menuPassager = menu.add(0, ACTION_PASSAGER, 0,  R.string.tm_item_passager);
		
		refreshMenuItem();
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo infos = (AdapterContextMenuInfo) item.getMenuInfo();
	
		trajet = detailTrajetsAdapter.getItem(infos.position);
				
		switch (item.getItemId()) {
		case ACTION_ACTIVATE:
			Log.i(getClass().getSimpleName(), "activate : " + trajet);
			goTrajetActivation();
			return true;
		case ACTION_DESACTIVATE:
			Log.i(getClass().getSimpleName(), "desactivate : " + trajet );
			goTrajetDesactivation();
			return true;
		case ACTION_DELETE:
			Log.i(getClass().getSimpleName(), "delete : " + trajet);
			goTrajetDelete();
			return true;
		case ACTION_MAP:
			Log.i(getClass().getSimpleName(), "map : " + trajet);
			goTrajetMap();
			return true;
		case ACTION_PASSAGER:
			Log.i(getClass().getSimpleName(), "passager : " + trajet);
			goTrajetPassager();
			return true;	
		default:
			return super.onContextItemSelected(item);
		}
	}    
	
	private OnItemLongClickListener getOnItemLongClickListener() {
		
		onItemLongClickListener = new OnItemLongClickListener() {

		    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		    	
		    	trajet = (Trajet)parent.getItemAtPosition(position);
		    	refreshMenuItem();
		    	return false;
		    }
		};		
		return onItemLongClickListener;
	}	
	
	
	private void refreshMenuItem() {

		if (menuActivate != null) {
			
			Trajet activeTrajet = session.getActiveTrajet();
			boolean activable = (activeTrajet == null);
			
			Log.i(trajetManaging.getClass().getSimpleName(), "Trajet " + trajet + (activable ? " activable" : " non activable"));
			menuActivate.setEnabled(activable && (trajet.getEtat() == Trajet.ETAT_DISPO));
			menuDesactivate.setEnabled(trajet.getEtat() == Trajet.ETAT_ACTIF);
			menuMap.setEnabled(trajet.getEtat() == Trajet.ETAT_ACTIF);
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
			
			String state = "";
			switch (trajet.getEtat()) {
				case Trajet.ETAT_DISPO:
					state = "Disponible";
					break;
				case Trajet.ETAT_ACTIF:
					state = "Actif";
					break;
			};
			
			wrapper.getDate().setText(dateAff.toString());
			wrapper.getState().setText("[" + state + "]");
			wrapper.getDepart().setText(trajet.getLieuDepart() + " à " + trajet.getHoraireDepart());
			wrapper.getArrivee().setText(trajet.getLieuDestination() + " à " + trajet.getHoraireArrivee());
			wrapper.getPlace().setText(trajet.getSoldePlaceDispo() + "");
			wrapper.getPoint().setText(trajet.getNbrePoint() + ""); 
						
			return (row);
		}
	}
	
	
	private List<Trajet> getTrajets() {
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des trajets");
		
		trajets = session.getTrajets();
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Fin recherche des trajets : " + trajets.size());
		return trajets;
	}

	
    private void goTrajetCreation() {
    	currentAction = ACTION_CREATE;
    	startProgressDialogInNewThread(this);
    }

    
    private void goTrajetActivation() {
    	currentAction = ACTION_ACTIVATE;
    	startProgressDialogInNewThread(this);
    }

    
    private void goTrajetDesactivation() {
    	currentAction = ACTION_DESACTIVATE;
    	startProgressDialogInNewThread(this);
    }

    
    private void goTrajetDelete() {
    	currentAction = ACTION_DELETE;
    	startProgressDialogInNewThread(this);
    }

    
    private void goTrajetMap() {
    	currentAction = ACTION_MAP;
    	startProgressDialogInNewThread(this);
    }
    
    private void goTrajetPassager() {
    	currentAction = ACTION_PASSAGER;
    	startProgressDialogInNewThread(this);
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
		
		Intent intent;
		
		switch (currentAction) {
		case ACTION_CREATE:
			intent = new Intent(this, TrajetCreation.class);
			startActivityForResult(intent, CODE_TRAJETMANAGING);
			break;
			
		case ACTION_ACTIVATE:
			session.activateTrajet(trajet);
			loadTrajets();
			stopProgressDialog();
			break;
			
		case ACTION_DESACTIVATE:
			session.deactivateTrajet();
			loadTrajets();
			stopProgressDialog();
			break;

		case ACTION_DELETE:
			session.delete(trajet);
			loadTrajets();
			stopProgressDialog();
			break;
			
		case ACTION_MAP:
			startActivity(new Intent(this, GoogleMapActivity.class));
			stopProgressDialog();
			break;
			
		case ACTION_PASSAGER:
			intent = new Intent(this, PassagerTrouve.class);
	        Bundle bundle = new Bundle();
	        bundle.putSerializable("trajet", trajet);
	        intent.putExtras(bundle);
			startActivityForResult(intent, CODE_TRAJETMANAGING);
			break;	
		}
	}
}
package com.alma.telekocsi;

import java.util.ArrayList;
import java.util.List;

import com.alma.telekocsi.dao.event.Event;
import com.alma.telekocsi.dao.event.EventDAO;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetLigne;
import com.alma.telekocsi.dao.trajet.TrajetLigneDAO;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;
import com.alma.telekocsi.util.Tools;

import android.app.Activity;
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


public class PassagerTrouve extends ARunnableActivity {
	
	private static final int ACTION_ACCEPT = 1;
	private static final int ACTION_REJECT = 2;

	private EventDAO eventDAO;
	private TrajetLigneDAO trajetLigneDAO;
	
	private OnClickListener onClickListener = null;
	private Button btAccepter;
	private Button btRefuser;
	private Button btQuit;

	private TextView tvVilleDepart;
	private TextView tvVilleArrivee;
	private TextView tvDate;
	private TextView tvResultat;
	private TextView tvPage;
	
	private ListView listView;
	private ArrayAdapter<PropositionPassager> detailPropositionPassagersAdapter;
	private OnItemLongClickListener onItemLongClickListener;
	private PassagerTrouve passagerTrouve = this;
	
	private List<PropositionPassager> propositionPassagers = null;
	private Trajet trajet = null;
	private PropositionPassager propositionPassager;
	
	private Session session;
	
	private MenuItem menuAccept;
	private MenuItem menuReject;
	private int currentAction = 0;
	
	
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
		
			tvResultat.setText(propositionPassagers.size() + " résultats");
			tvPage.setText("Page : 1/1");
			
			listView.setAdapter(detailPropositionPassagersAdapter);
			listView.setOnItemLongClickListener(getOnItemLongClickListener());
			registerForContextMenu(listView);
			
			if (propositionPassagers.size() == 0) {
				Toast.makeText(passagerTrouve, "Aucune proposition en attente", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menuAccept = menu.add(0, ACTION_ACCEPT, 0,  R.string.pt_item_acccept);
		menuReject = menu.add(0, ACTION_REJECT, 0,  R.string.pt_item_reject);
		refreshMenuItem();
	}
	
	
	private void refreshMenuItem() {

		if (menuAccept != null) {
			
			boolean activable = (trajet.getSoldePlaceDispo() > 0);
			
			menuAccept.setEnabled(activable);
			menuReject.setEnabled(true);
		}
	}
	
	
	private OnItemLongClickListener getOnItemLongClickListener() {
		
		onItemLongClickListener = new OnItemLongClickListener() {

		    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		    	
		    	propositionPassager = (PropositionPassager)parent.getItemAtPosition(position);
		    	refreshMenuItem();
		    	return false;
		    }
		};		
		return onItemLongClickListener;
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo infos = (AdapterContextMenuInfo) item.getMenuInfo();
	
		propositionPassager = detailPropositionPassagersAdapter.getItem(infos.position);
				
		switch (item.getItemId()) {
		case ACTION_ACCEPT:
			Log.i(getClass().getSimpleName(), "accept : " + propositionPassager);
			goPropositionPassagerAccept();
			return true;
		case ACTION_REJECT:
			Log.i(getClass().getSimpleName(), "reject : " + propositionPassager );
			goPropositionPassagerReject();
			return true;	
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	
    private void goPropositionPassagerAccept() {
    	currentAction = ACTION_ACCEPT;
    	startProgressDialogInNewThread(this);
    }
    
    
    private void goPropositionPassagerReject() {
    	currentAction = ACTION_REJECT;
    	startProgressDialogInNewThread(this);
    }
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.passager_trouve);
        
        session = SessionFactory.getCurrentSession(this);
        
        btQuit = (Button)findViewById(R.id.btPTQuit);
        btQuit.setOnClickListener(getOnClickListener()); 
        
        tvVilleDepart = (TextView)findViewById(R.id.tvPTVilleDepart);
        tvVilleArrivee = (TextView)findViewById(R.id.tvPTVilleArrivee);
        tvDate = (TextView)findViewById(R.id.tvPTDate);
        tvResultat = (TextView)findViewById(R.id.tvPTResultat);
        tvPage = (TextView)findViewById(R.id.tvPTPage);
        
		// Tests
        listView = (ListView)findViewById(R.id.lvPTListPassager);
    }
    
    
    @Override
    protected void onStart() {

    	super.onStart();
    	chargeInfoIntent();
    	
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				loadPropositionPassagers();
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
					}
				}
			};
    	}
    	return onClickListener;
    }

    
    public void chargeInfoIntent() {
        
        Bundle bundle = this.getIntent().getExtras();
        trajet = (Trajet)bundle.getSerializable("trajet");
        
        tvVilleDepart.setText(trajet.getLieuDepart());
        tvVilleArrivee.setText(trajet.getLieuDestination());
        tvDate.setText(trajet.getDateTrajet());
        
        Log.i(PassagerTrouve.class.getSimpleName(), " Trajet : " + trajet);
    }  
    

    private PropositionPassager getModel(int position) {
		
		return detailPropositionPassagersAdapter.getItem(position);
	}  
    
	
	class PropositionAdapter extends ArrayAdapter<PropositionPassager> {
		
		Activity context;
		
		PropositionAdapter(Activity context, List<PropositionPassager> list) {
			
			super(context, R.layout.passager_trouverow, list);
			this.context = context;
		}
		
		
		/**
		 * Fixer la taille des champs pour avoir la place suffisante
		 * pour tout afficher !
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			TrajetTrouveViewWrapper wrapper;
													
			if (row == null) {
				
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.passager_trouverow, null);
				wrapper = new TrajetTrouveViewWrapper(row);
				row.setTag(wrapper);
			} else {
				wrapper = (TrajetTrouveViewWrapper)row.getTag();
			}

			PropositionPassager pp = getModel(position);
			
			wrapper.getNbrePlaceDispo().setText(pp.getNbrePlaces() + " place(s)");
			wrapper.getNbrePoint().setText(pp.getNbrePoints() + " points");
			
			wrapper.getNombreAvis().setText(pp.getNbreAvis() + " avis");
			wrapper.getNomConducteur().setText(pp.getNomPassager());
			
			wrapper.getVisage().setImageResource(Profile.getImageResource(pp.getSexe()));
			wrapper.getStarsClassement().setImageResource(Profile.getClassementStarImageResource(pp.getClassementMoyen()));
			
			return (row);
		}
	}
	
	
	class PropositionPassager {
		
		private int nbrePlaces;
		private int nbrePoints;
		private String nomPassager;
		private int nbreAvis;
		private int classementMoyen;
		private String sexe;
		private Event event;
		
		
		public PropositionPassager() {
		}

		public int getNbrePlaces() {
			return nbrePlaces;
		}

		public void setNbrePlaces(int nbrePlaces) {
			this.nbrePlaces = nbrePlaces;
		}

		public int getNbrePoints() {
			return nbrePoints;
		}

		public void setNbrePoints(int nbrePoints) {
			this.nbrePoints = nbrePoints;
		}

		public String getNomPassager() {
			return nomPassager;
		}

		public void setNomPassager(String nomPassager) {
			this.nomPassager = nomPassager;
		}

		public int getNbreAvis() {
			return nbreAvis;
		}

		public void setNbreAvis(int nbreAvis) {
			this.nbreAvis = nbreAvis;
		}

		public int getClassementMoyen() {
			return classementMoyen;
		}

		public void setClassementMoyent(int classementMoyent) {
			this.classementMoyen = classementMoyent;
		}

		public String getSexe() {
			return sexe;
		}

		public void setSexe(String sexe) {
			this.sexe = sexe;
		}

		public Event getEvent() {
			return event;
		}

		public void setEvent(Event event) {
			this.event = event;
		}
	}
	
    
    private void loadPropositionPassagers() {
    	
		detailPropositionPassagersAdapter = new PropositionAdapter(passagerTrouve, getPropositionPassagers());
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }	
	
	
	public List<PropositionPassager> getPropositionPassagers() {
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Debut recherche des events");
		
		propositionPassagers = new ArrayList<PropositionPassager>();
		
		List<Event> list = getEventDAO().getListTo(session.getActiveProfile().getId());
		
		for (Event event : list) {
			// NP : non presente
			// P : presente
			if ((event.getTypeEvent() == 110) && (event.getEtat().equals("NP"))) {
				
				String[] values = event.getDescription().split(";");
				
				String idTrajet = values[0]; 
				if (! idTrajet.equals(trajet.getId()))
					continue;
				
				int nbrePoints = Integer.parseInt(values[1]);
				int nbrePlaces = Integer.parseInt(values[2]);
				
				PropositionPassager pp = new PropositionPassager();
				Profil profilPassager = session.find(Profil.class, event.getIdProfilFrom());
				pp.setClassementMoyent(profilPassager.getClassementMoyen());
				pp.setNbreAvis(profilPassager.getNombreAvis());
				pp.setNomPassager(Tools.getName(profilPassager));
				pp.setNbrePlaces(nbrePlaces);
				pp.setNbrePoints(nbrePoints);
				pp.setSexe(profilPassager.getSexe());
				pp.setEvent(event);
				
				propositionPassagers.add(pp);
			}
		}
		
		Log.i(TrajetRecherche.class.getSimpleName(), "Fin recherche des events : " + propositionPassagers.size());
		return propositionPassagers;
	}

	
	private EventDAO getEventDAO() {
		if (eventDAO == null) {
			eventDAO = new EventDAO(); 
		}
		return eventDAO;
	}

	
	private TrajetLigneDAO getTrajetLigneDAO() {
		if (trajetLigneDAO == null) {
			trajetLigneDAO = new TrajetLigneDAO(); 
		}
		return trajetLigneDAO;
	}
	
    
	@Override
	public void run() {

		Event event = propositionPassager.getEvent();
		event.setEtat("P");
		getEventDAO().update(event);
		
		switch (currentAction) {
		case ACTION_ACCEPT:
			
			TrajetLigne trajetLigne = new TrajetLigne();
			trajetLigne.setIdTrajet(trajet.getId());
			trajetLigne.setIdProfilPassager(event.getIdProfilFrom());
			trajetLigne.setNbrePoint(propositionPassager.getNbrePoints());
			trajetLigne.setPlaceOccupee(propositionPassager.getNbrePlaces());
			getTrajetLigneDAO().insert(trajetLigne);
			
			
			
			loadPropositionPassagers();
			stopProgressDialog();
			break;
			
		case ACTION_REJECT:
			loadPropositionPassagers();
			stopProgressDialog();
			break;
		}
	}
}
package com.alma.telekocsi;


import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class TrajetDetail extends ARunnableActivity {

	private OnClickListener onClickListener = null;
	private Trajet trajet;
	
	private TextView tvDepartArrivee;
	private TextView tvVilleDepart;
	private TextView tvDTHeureDepart;
	private TextView tvDTVariableDepart;
	private TextView tvDTVilleArrivee;
	private TextView tvDTHeureArrivee;
	private TextView tvDTAutoroute;
	private TextView tvDTPrix;
	private TextView tvDTPlaceDispo;
	private ImageView ivDTVisage;
	private TextView tvDTNomConducteur;
	private ImageView ivDTStars;
	private TextView tvDTNombreAvis;
	private TextView tvDTTelephone;
	private RadioGroup rgDTsmoker;
	/*private RadioButton rbDTsmoking_yes;
	private RadioButton rbDTsmoking_no;
	private RadioButton rbDTsmoking_any;*/
    
	private RadioGroup rgDTanimals;
	/*private RadioButton rbDTanimals_yes;
	private RadioButton rbDTanimals_no;
	private RadioButton rbDTanimals_any;*/
    
	private RadioGroup rgDTdetours;
	/*private RadioButton rbDTdetours_yes;
	private RadioButton rbDTdetours_no;
	private RadioButton rbDTdetours_any;*/

	private RadioGroup rgDTmusic;
	/*private RadioButton rbDTmusic_yes;
	private RadioButton rbDTmusic_no;
	private RadioButton rbDTmusic_any;*/

	private RadioGroup rgDTdiscussion;
	/*private RadioButton rbDTdiscussion_yes;
	private RadioButton rbDTdiscussion_no;
	private RadioButton rbDTdiscussion_any;*/
	
	private Button btRetour;
	private Session session;
	
	
	final Handler handler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
	
			tvDepartArrivee.setText(trajet.getLieuDepart() + " -> " + trajet.getLieuDestination());
			tvVilleDepart.setText(trajet.getLieuDepart());
			tvDTHeureDepart.setText(trajet.getHoraireDepart());
			tvDTVariableDepart.setText(trajet.getVariableDepart());
			tvDTVilleArrivee.setText(trajet.getLieuDestination());
			tvDTHeureArrivee.setText(trajet.getHoraireArrivee());
			tvDTAutoroute.setText(trajet.isAutoroute() ? "oui" : "non");
			tvDTPrix.setText(trajet.getNbrePoint() + "");
			tvDTPlaceDispo.setText(trajet.getPlaceDispo() + "");
			
			ProfilDAO profilDAO = new ProfilDAO();
			Profil conducteur = profilDAO.getProfil(trajet.getIdProfilConducteur());
			
			
			//ivDTVisage;
			//ivDTStars;
			
			tvDTNomConducteur.setText(conducteur.getPrenom() + " " + (conducteur.getNom() + "     ").substring(0, 4));
			tvDTNombreAvis.setText(conducteur.getNombreAvis() + " avis");
			tvDTTelephone.setText(conducteur.getTelephone());
			
			String val;
			
			val  =  Profile.getStringVal(conducteur.getFumeur());
			for (int i = 0; i < rgDTsmoker.getChildCount(); i++) {
				RadioButton rb = (RadioButton)rgDTsmoker.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}
			
			val  =  Profile.getStringVal(conducteur.getAnimaux());
			for (int i = 0; i < rgDTanimals.getChildCount(); i++) {
				RadioButton rb = (RadioButton)rgDTanimals.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}
			
			val  =  Profile.getStringVal(conducteur.getDetours());
			for (int i = 0; i < rgDTdetours.getChildCount(); i++) {
				RadioButton rb = (RadioButton)rgDTdetours.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}

			val  =  Profile.getStringVal(conducteur.getMusique());
			for (int i = 0; i < rgDTmusic.getChildCount(); i++) {
				RadioButton rb = (RadioButton)rgDTmusic.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}

			val  =  Profile.getStringVal(conducteur.getDiscussion());
			for (int i = 0; i < rgDTdiscussion.getChildCount(); i++) {
				RadioButton rb = (RadioButton)rgDTdiscussion.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}
			
		}
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trajetdetail);
        
        session = SessionFactory.getCurrentSession(this);
        
        tvDepartArrivee = (TextView)findViewById(R.id.tvDTDepartArrivee);
        tvVilleDepart = (TextView)findViewById(R.id.tvDTVilleDepart);
        tvDTHeureDepart = (TextView)findViewById(R.id.tvDTHeureDepart);
        tvDTVariableDepart = (TextView)findViewById(R.id.tvDTVariableDepart);
        tvDTVilleArrivee = (TextView)findViewById(R.id.tvDTVilleArrivee);
        tvDTHeureArrivee = (TextView)findViewById(R.id.tvDTHeureArrivee);
        tvDTAutoroute = (TextView)findViewById(R.id.tvDTAutoroute);
        tvDTPrix = (TextView)findViewById(R.id.tvDTPrix);
        tvDTPlaceDispo = (TextView)findViewById(R.id.tvDTPlaceDispo);
        ivDTVisage = (ImageView)findViewById(R.id.ivDTVisage);
        tvDTNomConducteur = (TextView)findViewById(R.id.tvDTNomConducteur);
        ivDTStars = (ImageView)findViewById(R.id.ivDTStars);
        tvDTNombreAvis = (TextView)findViewById(R.id.tvDTNombreAvis);
        tvDTTelephone = (TextView)findViewById(R.id.tvDTTelephone);
        rgDTsmoker = (RadioGroup)findViewById(R.id.rgDTsmoker);
        
        /*rbDTsmoking_yes = (RadioButton)findViewById(R.id.rbDTsmoking_yes);
        rbDTsmoking_no = (RadioButton)findViewById(R.id.rbDTsmoking_no);
        rbDTsmoking_any = (RadioButton)findViewById(R.id.rbDTsmoking_any);*/
        
        rgDTanimals = (RadioGroup)findViewById(R.id.rgDTanimals);
        /*rbDTanimals_yes = (RadioButton)findViewById(R.id.rbDTanimals_yes);
        rbDTanimals_no = (RadioButton)findViewById(R.id.rbDTanimals_no);
        rbDTanimals_any = (RadioButton)findViewById(R.id.rbDTanimals_any);*/
        
        rgDTdetours = (RadioGroup)findViewById(R.id.rgDTdetours);
        /*rbDTdetours_yes = (RadioButton)findViewById(R.id.rbDTdetours_yes);
        rbDTdetours_no = (RadioButton)findViewById(R.id.rbDTdetours_no);
        rbDTdetours_any = (RadioButton)findViewById(R.id.rbDTdetours_any);*/

        rgDTmusic = (RadioGroup)findViewById(R.id.rgDTmusic);
        /*rbDTmusic_yes = (RadioButton)findViewById(R.id.rbDTmusic_yes);
        rbDTmusic_no = (RadioButton)findViewById(R.id.rbDTmusic_no);
        rbDTmusic_any = (RadioButton)findViewById(R.id.rbDTmusic_any);*/

        rgDTdiscussion = (RadioGroup)findViewById(R.id.rgDTdiscussion);
        /*rbDTdiscussion_yes = (RadioButton)findViewById(R.id.rbDTdiscussion_yes);
        rbDTdiscussion_no = (RadioButton)findViewById(R.id.rbDTdiscussion_no);
        rbDTdiscussion_any = (RadioButton)findViewById(R.id.rbDTdiscussion_any);*/

        
        //tvDepartArrivee.setText("LES HERBIERS -> ANGERS");
        
        btRetour = (Button)findViewById(R.id.btDTRestour);
        btRetour.setOnClickListener(getOnClickListener());
    } 
    
    
    @Override
    protected void onStart() {

    	super.onStart();
    	chargeInfoIntent();
    	
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
		
		        Message msg = handler.obtainMessage();
		        handler.sendMessage(msg);
			}
		});
        
        thread.start();
    }    
    
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btRetour) {
						setResult(RESULT_OK);
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
        
        /*
        tvVilleDepart.setText(itineraire.getLieuDepart());
        tvVilleArrivee.setText(itineraire.getLieuDestination());
        tvDate.setText(date.toString());*/
        
        /*
        tvResultat.setText("12 résultats");
        tvPage.setText("Page : 2/5");*/
        
        Log.i(TrajetTrouve.class.getSimpleName(), " Trajet : " + trajet);        
    }

	@Override
	public void run() {

	}

}

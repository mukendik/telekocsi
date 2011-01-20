package com.alma.telekocsi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alma.telekocsi.dao.avis.Avis;
import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetLigne;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;
import com.alma.telekocsi.util.Tools;


public class Transaction extends ARunnableActivity {
	public final static String ORIGINATOR = "transaction.profile.originator";
	public final static String DESTINATOR = "transaction.profile.destinator";
	
	OnClickListener onClickListener = null;
	Button btValider;
	Button btAnnuler;
	private EditText commentText;
	private TextView destinationText;
	private TextView destinatorNameText;
	private TextView originText;
	private TextView destinatorTitleText;
	private TextView originatorNameText;
	private TextView originatorTitleText;
	private TextView placesCountText;
	private ImageButton pointDownButton;
	private TextView pointText;
	private ImageButton pointUpButton;
	private TextView travelDateText;
	private RatingBar ratingBar;
	
	/**
	 * Trajet concerner par la validation
	 */
	private Trajet route;
	private Trajet demoRoute;
	/**
	 * Le profil de celui qui note
	 */
	private Profil originator;
	/**
	 * Le profil de celui qui est noté
	 */
	private Profil destinator;
	private Itineraire iti;
	private Session session;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        session = SessionFactory.getCurrentSession(this);
        
        route = session.getActiveTrajet();
        
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
        	originator = (Profil)extras.get(ORIGINATOR);
        	destinator = (Profil)extras.get(DESTINATOR);
        	demoRoute = (Trajet)extras.get("DEMO_ROUTE");
        }
        
        /*
         * Partie paiement et evaluation
         * ----------------------------- 
         */
         setContentView(R.layout.transaction);

         btValider = (Button)findViewById(R.id.btTransactionValider);
         btValider.setOnClickListener(getOnClickListener()); 
         
         btAnnuler = (Button)findViewById(R.id.btTransactionAnnuler);
         btAnnuler.setOnClickListener(getOnClickListener()); 
         
         commentText = (EditText)findViewById(R.id.transaction_comment_editText);
         destinationText = (TextView)findViewById(R.id.transaction_destination_point_text);
         destinatorNameText = (TextView)findViewById(R.id.transaction_destinator_name_text);
         destinatorTitleText = (TextView)findViewById(R.id.transaction_destinator_title_text);
         originText = (TextView)findViewById(R.id.transaction_origin_point_text);
         originatorNameText = (TextView)findViewById(R.id.transaction_originator_name_text);
         originatorTitleText = (TextView)findViewById(R.id.transaction_originator_title_text);
         placesCountText = (TextView)findViewById(R.id.transaction_places_count_text);
         pointDownButton = (ImageButton)findViewById(R.id.transaction_point_down_button);
         pointText = (TextView)findViewById(R.id.transaction_point_text);
         pointUpButton = (ImageButton)findViewById(R.id.transaction_point_up_button);
         travelDateText = (TextView)findViewById(R.id.transaction_travel_date_text);
         ratingBar = (RatingBar)findViewById(R.id.transaction_rating_bar);
         
         pointDownButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	setPointsText(getPoints()-1);
			}
			
		});
         
         pointUpButton.setOnClickListener(new OnClickListener() {
			
			@Override  
			public void onClick(View v) {
		    	setPointsText(getPoints()+1);
			}
			
		});
         
    }
  
    
    @Override
	protected void onStart() {
		super.onStart();
		
		
		if((route=demoRoute!=null?demoRoute:session.getActiveTrajet())==null 
				|| originator==null 
				|| destinator==null 
				|| (iti = session.find(Itineraire.class,route.getIdItineraire()))==null)
		{
			finish();
			Toast.makeText(this, getString(R.string.no_route_registered), Toast.LENGTH_SHORT).show();
		}
		
		initValues();
	}

    /**
     * Peupler la vue
     */
    private void initValues(){  
    	travelDateText.setText(route.getDateTrajet());
    	destinationText.setText(iti.getLieuDestination());
    	originText.setText(iti.getLieuDepart());
    	if(originator.getId()!=null && originator.getId().equals(route.getIdProfilConducteur())){
    		originatorTitleText.setText(getString(R.string.conducteur));
    		destinatorTitleText.setText(getString(R.string.passager));
    	}
    	else{
    		destinatorTitleText.setText(getString(R.string.conducteur));
    		originatorTitleText.setText(getString(R.string.passager));
    	}
    	originatorNameText.setText(originator.getPrenom()+" "+originator.getNom().substring(0, 1).toUpperCase()+".");
    	destinatorNameText.setText(destinator.getPrenom()+" "+destinator.getNom().substring(0, 1).toUpperCase()+".");
    	
    	Integer point=route.getNbrePoint();
    	setPointsText(point==null?0:point);
    	setPlacesCountText(1);
    }
    
	/**
     * 
     * @return Le nombre de points
     */
    private int getPoints(){    	
    	Pattern p = Pattern.compile("\\s*(\\-?\\d+).*");
    	String txt = pointText.getText().toString();
    	Matcher m = p.matcher(txt);
    	if(m.matches()){
    		try{
    			return Integer.valueOf(m.group(1));    		
    		} catch(Throwable e) {
    			Log.d(getClass().getName(),e.getMessage());
    		}
    	}
    	return 0;
    }

    /**
     * Mettre a jour le text du nombre de points
     * @param points
     */
    private void setPointsText(int points){
    	pointText.setText(String.format("%d %s%s",points,getString(R.string.point),Math.abs(points)>1?"s":""));    	
    }
    
    protected int getPlacesCount(){
    	Pattern p = Pattern.compile("\\s*(\\d+).*");
    	String txt = placesCountText.getText().toString();
    	Matcher m = p.matcher(txt);
    	if(m.matches()){
    		try{
    			return Integer.valueOf(m.group(1));    		
    		} catch(Throwable e) {
    			Log.d(getClass().getName(),e.getMessage());
    		}
    	}
    	return 0;
    }
    
    /**
     * 
     * @param places
     */
    private void setPlacesCountText(int places){
    	placesCountText.setText(String.format("%d %s%s",places,getString(R.string.place),Math.abs(places)>1?"s":""));
    }
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btAnnuler) {
						finish();
					} else if (v == btValider) {
						actionValider();
						finish();
					}
				}
			};
    	}
    		
    	return onClickListener;
    }
 
    
    private void actionValider() {
    	run();
    }


	@Override
	public void run() {
		//Validation de la transaction		
		com.alma.telekocsi.dao.transaction.Transaction t = new com.alma.telekocsi.dao.transaction.Transaction();
		
		TrajetLigne tl = null;
		String idDriver = route.getIdProfilConducteur(); 
		t.setIdProfilConducteur(idDriver);
		if(idDriver.equals(originator.getId())){
			t.setIdProfilPassager(destinator.getId());			
			tl = session.getActiveTrajetLineFor(destinator.getId());
		}
		else{
			t.setIdProfilPassager(originator.getId());
			tl = session.getActiveTrajetLineFor(originator.getId());
		}
		t.setPointEchange(getPoints());
		t.setHeureTransaction(route.getHoraireDepart());
		
		if(tl!=null){
			t.setIdTrajetLigne(tl.getId());
		}
//		else{
//			Toast.makeText(this, R.string.transaction_validation_failure, Toast.LENGTH_SHORT).show();
//			return;
//		}
		
		Avis avis = new Avis();
		avis.setClassement(Math.round(ratingBar.getRating()));
		avis.setCommentaire(commentText.getText().toString());
		avis.setDateAvis(new SimpleDateFormat("d-M-y").format(new Date()));
		avis.setHeureAvis(new SimpleDateFormat("HH:mm").format(new Date()));
		avis.setChecked(false);
		//TODO remplir dans l'avis: Etat
		avis = session.save(avis);
//		session.save(t);
//		Toast.makeText(this, R.string.transaction_validation_success, Toast.LENGTH_SHORT).show();
		
		if(session.save(t)!=null){
			try{
				Toast.makeText(this, R.string.transaction_validation_success, Toast.LENGTH_SHORT).show();
			} catch (Exception ex){
				Log.d(getClass().getName(), ex.getMessage(), ex);
			}
			finish();
		}
		else{
			try{
				Toast.makeText(this, R.string.transaction_validation_failure, Toast.LENGTH_SHORT).show();
			} catch (Exception ex){
				Log.d(getClass().getName(), ex.getMessage(), ex);
			}
		}
	}
	
}
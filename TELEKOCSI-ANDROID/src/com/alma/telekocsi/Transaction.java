package com.alma.telekocsi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;


public class Transaction extends ARunnableActivity {
	/**
	 * Le parametre a fournir pour la transaction
	 */
	public final static String ACTIVE_ROUTE = "transaction.active.route";
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
	
	/**
	 * Trajet concerner par la validation
	 */
	private Trajet route;
	/**
	 * Le profil de celui qui note
	 */
	private Profil originator;
	/**
	 * Le profil de celui qui est noté
	 */
	private Profil destinator;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
        	route = (Trajet)extras.get(ACTIVE_ROUTE);
        	originator = (Profil)extras.get(ORIGINATOR);
        	destinator = (Profil)extras.get(DESTINATOR);
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
         
         pointDownButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pointDown();
			}
			
		});
         
         pointUpButton.setOnClickListener(new OnClickListener() {
			
			@Override  
			public void onClick(View v) {
				pointUp();
			}
			
		});
    }
  
    
    @Override
	protected void onStart() {
		super.onStart();
		
		if(route==null || originator==null || destinator==null){
			finish();
			Toast.makeText(getApplicationContext(), getString(R.string.no_route_registered), Toast.LENGTH_SHORT).show();
		}
		
		initValues();
	}

    /**
     * Peupler la vue
     */
    private void initValues(){    
    }
    
	/**
     * 
     * @return Le nombre de points
     */
    private int getPoints(){    	
    	Pattern p = Pattern.compile("\\s*(\\d+).*");
    	String txt = pointText.getText().toString();
    	Matcher m = p.matcher(txt);
    	if(m.matches()){
    		try{
    			return Integer.valueOf(txt);    		
    		} catch(Throwable e) {
    			Log.d(getClass().getName(),e.getMessage());
    		}
    	}
    	return 0;
    }
    
    /**
     * Augmenter le nombre de point
     */
    private void pointUp(){
    	int point = getPoints()+1;
    	pointText.setText(String.format("%d %s%s",point,getString(R.string.point),Math.abs(point)>1?"s":""));
    }
    
    /**
     * Diminuer le nombre the point
     */
    private void pointDown(){
    	int point = getPoints()-1;
    	pointText.setText(String.format("%d %s%s",point,getString(R.string.point),Math.abs(point)>1?"s":""));
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
    	startProgressDialogInNewThread(this);
    }


	@Override
	public void run() {
		//Validation de la transaction		
		com.alma.telekocsi.dao.transaction.Transaction t = new com.alma.telekocsi.dao.transaction.Transaction();
	}
	
}
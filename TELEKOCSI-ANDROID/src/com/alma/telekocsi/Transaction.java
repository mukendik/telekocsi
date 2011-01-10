package com.alma.telekocsi;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alma.telekocsi.dao.trajet.Trajet;


public class Transaction extends ARunnableActivity {
	
	OnClickListener onClickListener = null;
	Button btValider;
	Button btAnnuler;
	private EditText commentText;
	private TextView destinationText;
	private TextView destinatorNameText;
	private TextView originText;
	private TextView destinatorTitleText;
	private TextView originatorNameText;
	private TextView originatorTitleTex;
	private TextView placesCountText;
	private Button pointDownButton;
	private TextView pointText;
	private Button pointUpButton;
	private TextView travelDateText;
	
	/**
	 * Trajet concerner par la validation
	 */
	private Trajet route;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
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
         originatorTitleTex = (TextView)findViewById(R.id.transaction_originator_title_text);
         placesCountText = (TextView)findViewById(R.id.transaction_places_count_text);
         pointDownButton = (Button)findViewById(R.id.transaction_point_down_button);
         pointText = (TextView)findViewById(R.id.transaction_point_text);
         pointUpButton = (Button)findViewById(R.id.transaction_point_up_button);
         travelDateText = (TextView)findViewById(R.id.transaction_travel_date_text);
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
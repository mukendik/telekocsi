package com.alma.telekocsi;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Transaction extends ARunnableActivity {
	
	OnClickListener onClickListener = null;
	Button btValider;
	Button btAnnuler;
	
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
    	
    }


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
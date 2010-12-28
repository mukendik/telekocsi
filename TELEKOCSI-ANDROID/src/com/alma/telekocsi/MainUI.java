package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainUI extends Activity {
    
	OnClickListener onClickListener = null;
	Button btRechTrajet;
	Button btTransaction;
	Button btQuitter;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        btRechTrajet = (Button)findViewById(R.id.btMainTrajetRecherche);
        btRechTrajet.setOnClickListener(getOnClickListener());
        
        btTransaction = (Button)findViewById(R.id.btMainTransaction);
        btTransaction.setOnClickListener(getOnClickListener());
        
        btQuitter = (Button)findViewById(R.id.btMainQuit);
        btQuitter.setOnClickListener(getOnClickListener());
    }
    
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    }
    
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btQuitter) {
						finish();
					} else if (v == btRechTrajet) {
						goRechercheTrajet();
					} else if (v == btTransaction) {
						goTransaction();
					}
						
				}
			};
    	}
    		
    	return onClickListener;
    }
    
    public void goRechercheTrajet() {
        Intent intent = new Intent(this, TrajetRecherche.class);
        startActivity(intent);
    }
    
    
    public void goTransaction() {
        Intent intent = new Intent(this, Transaction.class);
        startActivity(intent);
    }
}
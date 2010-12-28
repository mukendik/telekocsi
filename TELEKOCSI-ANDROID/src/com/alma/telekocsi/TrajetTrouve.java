package com.alma.telekocsi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TrajetTrouve extends Activity {
    
	OnClickListener onClickListener = null;
	Button btNewSerach;
	Button btQuit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trajettrouve);
        
        btNewSerach = (Button)findViewById(R.id.btTTNewSearch);
        btNewSerach.setOnClickListener(getOnClickListener());
        
        btQuit = (Button)findViewById(R.id.btTTQuit);
        btQuit.setOnClickListener(getOnClickListener());        

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

}
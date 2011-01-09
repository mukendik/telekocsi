package com.alma.telekocsi;

import com.alma.telekocsi.dao.GAE;
import com.alma.telekocsi.dao.localisation.LocalisationTestDAO;
import com.alma.telekocsi.init.DataContext;
import com.alma.telekocsi.init.LoadData;
import com.alma.telekocsi.init.LoadDataBruno;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainUI extends Activity {
    
	OnClickListener onClickListener = null;
	Button btLoadDataBruno;
	Button btLoadData;
	Button btRechTrajet;
	Button btTransaction;
	Button btQuitter;
	Button btMap;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        btLoadDataBruno = (Button)findViewById(R.id.btMainLoadDataBruno);
        btLoadDataBruno.setOnClickListener(getOnClickListener());
        
        btLoadData = (Button)findViewById(R.id.btMainLoadData);
        btLoadData.setOnClickListener(getOnClickListener());
        
        btRechTrajet = (Button)findViewById(R.id.btMainTrajetRecherche);
        btRechTrajet.setOnClickListener(getOnClickListener());
        
        btTransaction = (Button)findViewById(R.id.btMainTransaction);
        btTransaction.setOnClickListener(getOnClickListener());
        
        btQuitter = (Button)findViewById(R.id.btMainQuit);
        btQuitter.setOnClickListener(getOnClickListener());
        
        btMap = (Button)findViewById(R.id.btGoogleMap);
        btMap.setOnClickListener(getOnClickListener());
    }
    
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	Thread threadInitHttpClient = new Thread(new Runnable() {
    		@Override
    		public void run() {
    			Log.i(MainUI.class.getSimpleName(), "Debut initialisation http client en thread");
    			GAE.getHttpClient();
    			GAE.getGson();
    			DataContext.getCurrentProfil();
    			Log.i(MainUI.class.getSimpleName(), "Fin initialisation http client en thread");
    		}
    	});
    	threadInitHttpClient.start();
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
					
					if (v == btQuitter) {
						finish();
					} else if (v == btRechTrajet) {
						goRechercheTrajet();
					} else if (v == btTransaction) {
						goTransaction();
					} else if (v == btMap) {
						goMap();
					} else if (v == btLoadData) {
						loadData();
					} else if (v == btLoadDataBruno) {
						loadDataBruno();
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
    
    public void goMap() {
        Intent intent = new Intent(this, GoogleMapActivity.class);
        startActivity(intent);
    }
    
    
    public void loadData() {
    	LoadData loadData = new LoadData();
    	loadData.load();
    }
    
    public void loadDataBruno() {
    	LoadDataBruno loadDataBruno = new LoadDataBruno();
    	loadDataBruno.load();
    }    
}
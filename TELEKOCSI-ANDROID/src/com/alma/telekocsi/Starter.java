package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Starter extends Activity {
	
	private OnClickListener onClickListener = null;
	private Button profileSettingsButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        boolean exist = false;
        boolean disconnected = true;
        //le compte de la personne n'est pas enregistre
        if(!exist){
            setContentView(R.layout.connection_parameters);
            profileSettingsButton = (Button)findViewById(R.id.profile_settings_button);
            profileSettingsButton.setOnClickListener(getOnClickListener());
        }
        //sinon savoir s'il s'etait deconnecte ou non
        else if(disconnected){
        	//si deconnecte, on affiche l'ecran de connexion
        	setContentView(R.layout.identification);
        }
        else{
        	//sinon on affiche l'ecran principal
        	setContentView(R.layout.main_menu);
        }
    }
    
    protected OnClickListener getOnClickListener(){
    	if(onClickListener==null){
    		onClickListener = makeOnClickListener();
    	}
    	return onClickListener;
    }
    
    protected OnClickListener makeOnClickListener(){
    	return new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==profileSettingsButton){
					startProfileSettings();
				}
			}
    		
    	};
    }
    
    private void startProfileSettings(){
    	Intent intent = new Intent(this, ProfileSettings.class);
        startActivity(intent);
    }

}

package com.alma.telekocsi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alma.telekocsi.checking.ConnectionParameters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Starter extends Activity {
	
	private OnClickListener onClickListener = null;
	private Button profileSettingsButton;
	private EditText email;
	private EditText password1;
	private EditText password2;
	
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
            email = (EditText)findViewById(R.id.connection_parameters_email);
            password1 = (EditText)findViewById(R.id.connection_parameters_password1);
            password2 = (EditText)findViewById(R.id.connection_parameters_password2);
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
    
    private static final String emailPattern = "^[\\w-\\.]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$";
    
    private void startProfileSettings(){
    	
    	Pattern p = Pattern.compile(Starter.emailPattern);
    	Matcher m = p.matcher(email.getText().toString());
    	
    	if(!m.find()){
    		email.setTextColor(Color.RED);
    	}
    	else{
    		if(!password1.getText().toString().equals(password2.getText().toString())
    				|| password1.getText().toString().equals("")){
        		password1.setTextColor(Color.RED);
        		password2.setTextColor(Color.RED);
    		}
    		else{
        		Intent intent = new Intent(this, ProfileSettings.class);
        		startActivity(intent);
    		}
    	}
    }

}

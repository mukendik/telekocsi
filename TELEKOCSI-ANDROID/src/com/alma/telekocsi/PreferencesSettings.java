package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PreferencesSettings extends Activity {

	private OnClickListener onClickListener = null;
	private Button backButton;
	private Button registeringSubmitButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.preferences_settings);

        backButton = (Button)findViewById(R.id.preferences_settings_back_button);
        backButton.setOnClickListener(getOnClickListener()); 
         
        registeringSubmitButton = (Button)findViewById(R.id.registering_submit_button);
        registeringSubmitButton.setOnClickListener(getOnClickListener());
    }
	
	private OnClickListener getOnClickListener(){
		if(onClickListener==null){
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	private OnClickListener makeOnClickListener(){
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==backButton){
					startProfileSettings();
				}
				else if(v==registeringSubmitButton){
					startMainMenu();
				}
			}
			
		};
	}
	
	private void startProfileSettings(){
		Intent intent = new Intent(this, ProfileSettings.class);
		startActivity(intent);
	}
	
	private void startMainMenu(){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}

}


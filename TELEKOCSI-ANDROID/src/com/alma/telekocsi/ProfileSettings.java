package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProfileSettings extends Activity {

	private OnClickListener onClickListener = null;
	private Button backButton;
	private Button preferencesSettingsButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.profile_settings);

        backButton = (Button)findViewById(R.id.profile_settings_back_button);
        backButton.setOnClickListener(getOnClickListener()); 
         
        preferencesSettingsButton = (Button)findViewById(R.id.preferences_settings_button);
        preferencesSettingsButton.setOnClickListener(getOnClickListener()); 
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
					startConnectionParameters();
				}
				else if(v==preferencesSettingsButton){
					startPreferencesSettings();
				}
			}
			
		};
	}
	
	private void startConnectionParameters(){
		Intent intent = new Intent(this, Starter.class);
		startActivity(intent);
	}
	
	private void startPreferencesSettings(){
		Intent intent = new Intent(this, PreferencesSettings.class);
		startActivity(intent);
	}

}


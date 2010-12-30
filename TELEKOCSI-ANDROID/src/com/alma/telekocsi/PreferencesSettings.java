package com.alma.telekocsi;

import com.alma.telekocsi.checking.PreferencesChecking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PreferencesSettings extends Activity {
	
	private static final int CHECKING = 1;
	private static final int MAIN_MENU = 2;

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
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			startActivityForResult(new Intent(this, MainMenu.class), MAIN_MENU);
    			break;
    		}
		case MAIN_MENU:
			switch(resultCode){
			case RESULT_CANCELED:
				break;
			}
			break;
    	}
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
					goBack();
				}
				else if(v==registeringSubmitButton){
					startMainMenu();
				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
	private void startMainMenu(){
		Intent intent = new Intent(this, PreferencesChecking.class);
    	startActivityForResult(intent, CHECKING);
	}

}


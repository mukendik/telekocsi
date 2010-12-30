package com.alma.telekocsi;


import com.alma.telekocsi.checking.ConnectionParametersChecking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectionParameters extends Activity {
	
	private static final int CHECKING = 1;
	private static final int NEXT_STEP = 2;
	
	private OnClickListener onClickListener = null;
	private Button profileSettingsButton;
	private EditText email;
	private EditText password1;
	private EditText password2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

        System.out.println("nawak-connection-parameters");

		setContentView(R.layout.connection_parameters);
        profileSettingsButton = (Button)findViewById(R.id.profile_settings_button);
        profileSettingsButton.setOnClickListener(getOnClickListener());
        email = (EditText)findViewById(R.id.connection_parameters_email);
        password1 = (EditText)findViewById(R.id.connection_parameters_password1);
        password2 = (EditText)findViewById(R.id.connection_parameters_password2);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			startActivityForResult(new Intent(this, ProfileSettings.class), NEXT_STEP);
    			break;
    		case ConnectionParametersChecking.INVALID_EMAIL:
    			email.setTextColor(Color.RED);
    			break;
    		case ConnectionParametersChecking.INVALID_PASSWORD:
    			password1.setTextColor(Color.RED);
    			password2.setTextColor(Color.RED);
    			break;
    		}
		case NEXT_STEP:
			switch(resultCode){
			case RESULT_CANCELED:
				break;
			}
			break;
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
    	Intent intent = new Intent(this, ConnectionParameters.class);
    	startActivityForResult(intent, NEXT_STEP);
    }


}

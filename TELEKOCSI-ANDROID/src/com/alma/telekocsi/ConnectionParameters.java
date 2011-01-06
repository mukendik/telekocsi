package com.alma.telekocsi;


import com.alma.telekocsi.checking.ConnectionParametersChecking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConnectionParameters extends ARunnableActivity {
	
	private static final int CHECKING = 1;
	
	private OnClickListener onClickListener = null;
	private Button profileSettingsButton;
	private EditText email;
	private EditText password1;
	private EditText password2;
	private TextView emailLabel;
	private TextView password1Label;
	private TextView password2Label;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.connection_parameters);
        profileSettingsButton = (Button)findViewById(R.id.profile_settings_button);
        profileSettingsButton.setOnClickListener(getOnClickListener());
        
        email = (EditText)findViewById(R.id.connection_parameters_email);
        password1 = (EditText)findViewById(R.id.connection_parameters_password1);
        password2 = (EditText)findViewById(R.id.connection_parameters_password2);
        
        emailLabel = (TextView)findViewById(R.id.emailLabelStep1);
        password1Label = (TextView)findViewById(R.id.password1LabelStep1);
        password2Label = (TextView)findViewById(R.id.password2LabelStep1);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:{
    			Intent intent = new Intent(this, ProfileSettings.class);
    			intent = intent.putExtra("email", email.getText().toString());
    			intent = intent.putExtra("password", password1.getText().toString());
    			startActivity(intent);
    		}break;
    		case ConnectionParametersChecking.INVALID_EMAIL:
    			emailLabel.setTextColor(Color.RED);
    			password1Label.setTextColor(Color.BLACK);
    			password2Label.setTextColor(Color.BLACK);
    			break;
    		case ConnectionParametersChecking.INVALID_PASSWORD:
    			emailLabel.setTextColor(Color.BLACK);
    			password1Label.setTextColor(Color.RED);
    			password2Label.setTextColor(Color.RED);
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
    	Intent intent = new Intent(this, ConnectionParametersChecking.class);
    	intent = intent.putExtra("email", email.getText().toString());
    	intent = intent.putExtra("password1", password1.getText().toString());
    	intent = intent.putExtra("password2", password2.getText().toString());
    	startActivityForResult(intent, CHECKING);
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//on n'affiche pas le menu quand on est connecte
		return false;
	}


}

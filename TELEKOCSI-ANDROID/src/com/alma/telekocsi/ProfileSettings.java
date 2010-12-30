package com.alma.telekocsi;

import com.alma.telekocsi.checking.ProfileChecking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileSettings extends Activity {

	private static final int CHECKING = 1;
	private static final int NEXT_STEP = 2;
	
	private OnClickListener onClickListener = null;
	private Button backButton;
	private Button preferencesSettingsButton;
	private EditText name;
	private EditText firstName;
	private EditText jj;
	private EditText mm;
	private EditText aaaa;
	private EditText phone;
	private TextView nameLabel;
	private TextView firstNameLabel;
	private TextView jjLabel;
	private TextView mmLabel;
	private TextView aaaaLabel;
	private TextView phoneLabel;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        System.out.println("nawak-profile-settings");
        
        setContentView(R.layout.profile_settings);
        backButton = (Button)findViewById(R.id.profile_settings_back_button);
        backButton.setOnClickListener(getOnClickListener()); 
        preferencesSettingsButton = (Button)findViewById(R.id.preferences_settings_button);
        preferencesSettingsButton.setOnClickListener(getOnClickListener());
        
        nameLabel = (TextView)findViewById(R.id.labelname);
        firstNameLabel = (TextView)findViewById(R.id.labelfirstname);
        jjLabel = (TextView)findViewById(R.id.labelday);
        mmLabel = (TextView)findViewById(R.id.lanelmonth);
        aaaaLabel = (TextView)findViewById(R.id.labelyear);
        phoneLabel = (TextView)findViewById(R.id.labelphone);
        
        name = (EditText)findViewById(R.id.respname);
        firstName = (EditText)findViewById(R.id.respfirstname);
        jj = (EditText)findViewById(R.id.respday);
        mm = (EditText)findViewById(R.id.respmonth);
        aaaa = (EditText)findViewById(R.id.respyear);
        phone = (EditText)findViewById(R.id.respphone);
        
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			startActivityForResult(new Intent(this, PreferencesSettings.class), NEXT_STEP);
    			break;
    		case ProfileChecking.INVALID_NAME:
    			nameLabel.setTextColor(Color.RED);
    			break;
    		case ProfileChecking.INVALID_FIRST_NAME:
    			firstNameLabel.setTextColor(Color.RED);
    			break;
    		case ProfileChecking.INVALID_JJ:
    			jjLabel.setTextColor(Color.RED);
    			break;
    		case ProfileChecking.INVALID_MM:
    			mmLabel.setTextColor(Color.RED);
    			break;
    		case ProfileChecking.INVALID_AAAA:
    			aaaaLabel.setTextColor(Color.RED);
    			break;
    		case ProfileChecking.INVALID_PHONE:
    			phoneLabel.setTextColor(Color.RED);
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
				else if(v==preferencesSettingsButton){
					startPreferencesSettings();
				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
	private void startPreferencesSettings(){
		Intent intent = new Intent(this, ProfileChecking.class);
    	intent = intent.putExtra("name", name.getText().toString());
    	intent = intent.putExtra("firstName", firstName.getText().toString());
    	intent = intent.putExtra("jj", jj.getText().toString());
    	intent = intent.putExtra("mm", mm.getText().toString());
    	intent = intent.putExtra("aaaa", aaaa.getText().toString());
    	intent = intent.putExtra("phone", phone.getText().toString());
    	startActivityForResult(intent, CHECKING);
	}

}


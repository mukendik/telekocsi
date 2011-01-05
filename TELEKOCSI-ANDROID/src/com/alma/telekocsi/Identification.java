package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alma.telekocsi.checking.IdentificationChecking;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class Identification extends Activity {

	private static final int CHECKING = 1;

	private OnClickListener onClickListener = null;
	private Button identificationButton;
	private Button newProfileButton;
	private EditText email;
	private EditText password;
	private TextView identificationError;
	private Session session = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.identification);
		
		session = SessionFactory.getCurrentSession(this);
		Profil profile = session.getActiveProfile();
		
		identificationButton = (Button)findViewById(R.id.identification_button);
		identificationButton.setOnClickListener(getOnClickListener());
        
        email = (EditText)findViewById(R.id.identification_email);    
        password = (EditText)findViewById(R.id.identification_password);
        
        identificationError = (TextView)findViewById(R.id.identification_error);
        
        newProfileButton = (Button)findViewById(R.id.new_profile_button);
        newProfileButton.setOnClickListener(getOnClickListener());
        
        if(profile!=null){
        	email.setText(profile.getEmail());
        }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			startActivity(new Intent(this, MainMenu.class));
    			break;
    		case IdentificationChecking.IDENTIFICATION_ERROR:
    			identificationError.setTextColor(Color.RED);
    			break;
    		}
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
				if(v==identificationButton){
					startProfileSettings();
				}
				else if(v==newProfileButton){
					startProfileCreation();
				}
			}
    		
    	};
    }
    
    private void startProfileCreation(){    
    	startActivity(new Intent(this, ConnectionParameters.class));
    }
    
    private void startProfileSettings(){
    	Intent intent = new Intent(this, IdentificationChecking.class);
    	intent = intent.putExtra("email", email.getText().toString());
    	intent = intent.putExtra("password", password.getText().toString());
    	startActivityForResult(intent, CHECKING);
    }


}

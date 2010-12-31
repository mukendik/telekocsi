package com.alma.telekocsi;

import com.alma.telekocsi.checking.IdentificationChecking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Identification extends Activity {

	private static final int CHECKING = 1;
	private static final int NEXT_STEP = 2;

	private OnClickListener onClickListener = null;
	private Button identificationButton;
	private EditText email;
	private EditText password;
	private TextView identificationError;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.identification);
		
		identificationButton = (Button)findViewById(R.id.identification_button);
		identificationButton.setOnClickListener(getOnClickListener());
        
        email = (EditText)findViewById(R.id.identification_email);
        password = (EditText)findViewById(R.id.identification_password);
        
        identificationError = (TextView)findViewById(R.id.identification_error);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			startActivityForResult(new Intent(this, MainMenu.class), NEXT_STEP);
    			break;
    		case IdentificationChecking.IDENTIFICATION_ERROR:
    			identificationError.setTextColor(Color.RED);
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
				if(v==identificationButton){
					startProfileSettings();
				}
			}
    		
    	};
    }
    
    private void startProfileSettings(){
    	Intent intent = new Intent(this, IdentificationChecking.class);
    	intent = intent.putExtra("email", email.getText().toString());
    	intent = intent.putExtra("password", password.getText().toString());
    	startActivityForResult(intent, CHECKING);
    }


}

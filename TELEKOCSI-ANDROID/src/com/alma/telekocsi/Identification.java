package com.alma.telekocsi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alma.telekocsi.checking.IdentificationChecking;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class Identification extends ARunnableActivity {

	private static final int CHECKING = 1;

	private OnClickListener onClickListener = null;
	private Button identificationButton;
	private Button newProfileButton;
	private Button sendEmailButton;
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
        
		sendEmailButton = (Button)findViewById(R.id.send_email_button);
		sendEmailButton.setOnClickListener(getOnClickListener());

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
    	stopProgressDialog();
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
					startIdentificationChecking();
				}
				else if(v==newProfileButton){
					startProfileCreation();
				}
				else if(v==sendEmailButton){
					sendEmail();
				}
			}
    		
    	};
    }
    
    private void startProfileCreation(){    
    	startActivity(new Intent(this, ConnectionParameters.class));
    }
    
    private void startIdentificationChecking(){
    	startProgressDialogInNewThread(this);
    }
    
    private void sendEmail(){
    	Session session = SessionFactory.getCurrentSession(this);
		Profil profile = session.getActiveProfile();
    	Intent sendIntent = new Intent(Intent.ACTION_SEND);
    	sendIntent.putExtra(Intent.EXTRA_EMAIL, profile.getEmail().toString());
    	sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Android Telekocsi Application. Mot de passe.");
    	sendIntent.putExtra(Intent.EXTRA_TEXT,"Votre mot de passe : "+profile.getMotDePasse().toString());
    	sendIntent.setType("text/plain");
    	startActivity(Intent.createChooser(sendIntent, "MySendMail"));
//		EMailSender sender = new EMailSender(); // SUBSTITUTE HERE                    
//		try {
//			sender.sendMail(
//				"about Hello World by email",   //subject.getText().toString(),   
//				"body of the world ",           //body.getText().toString(),   
//				"anybody@hotmail.com",          //from.getText().toString(),  
//				"somebody@gmail.com"            //to.getText().toString()  
//			);
//		}catch (Exception e){
//			Log.e("SendMail", e.getMessage(), e);
//		}
    }

	@Override
	public void run() {
		Intent intent = new Intent(this, IdentificationChecking.class);
    	intent = intent.putExtra("email", email.getText().toString());
    	intent = intent.putExtra("password", password.getText().toString());
    	startActivityForResult(intent, CHECKING);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//on n'affiche pas le menu quand on est connecte
		return false;
	}
    
}

package com.alma.telekocsi.checking;

import android.app.Activity;
import android.os.Bundle;

import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class IdentificationChecking extends Activity {
	
	public static final int IDENTIFICATION_ERROR = 1;
	Session session;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 
		 super.onCreate(savedInstanceState);
		 
		 session = SessionFactory.getCurrentSession(this);
		 
		 Bundle extras = getIntent().getExtras();
		 String  email = extras.getString("email").toString();
		 String password = extras.getString("password").toString();
		 
		 System.out.println("email [" + email + "]");
		 System.out.println("password [" + password + "]");
	     
	     if(!session.login(email, password)){
	    	 setResult(IDENTIFICATION_ERROR);
	     }
	     else{
	    	 setResult(RESULT_OK);
	     }
	     
	     finish();
	 }
	 
}

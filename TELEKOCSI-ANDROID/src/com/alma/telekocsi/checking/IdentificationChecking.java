package com.alma.telekocsi.checking;

import android.app.Activity;
import android.os.Bundle;

public class IdentificationChecking extends Activity {
	
	public static final int IDENTIFICATION_ERROR = 1;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 
		 super.onCreate(savedInstanceState);
		 
		 Bundle extras = getIntent().getExtras();
		 String  email = extras.getString("email").toString();
		 String password = extras.getString("password").toString();
		 
		 System.out.println(email);
		 System.out.println(password);
		 
	     
	     if(identificationError(email, password)){
	    	 setResult(IDENTIFICATION_ERROR);
	     }
	     else{
	    	 setResult(RESULT_OK);
	     }
	     
	     finish();
	 }
	 
	 private boolean identificationError(String email, String password){
		 return true;
	 }

}

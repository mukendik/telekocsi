package com.alma.telekocsi.checking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class ConnectionParametersChecking extends Activity {
	
	public static final int INVALID_EMAIL = 1;
	public static final int INVALID_PASSWORD = 2;
	private static final String emailPattern = "^[\\w-\\.]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$";
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 
		 super.onCreate(savedInstanceState);
		 
		 System.out.println("nawak-cheking");
		 
		 EditText email = (EditText) savedInstanceState.get("email");
		 System.out.println("nawak");
		 EditText password1 = (EditText) savedInstanceState.get("password1");
		 EditText password2 = (EditText) savedInstanceState.get("password2");
		 
		 Pattern p = Pattern.compile(ConnectionParametersChecking.emailPattern);
	     Matcher m = p.matcher(email.getText().toString());
	     
	     if(!m.find()){
	    	 setResult(ConnectionParametersChecking.INVALID_EMAIL);
	     }
	     else{
	    	 if(!password1.getText().toString().equals(password2.getText().toString())
	    			|| password1.getText().toString().equals("")){
	    		 setResult(ConnectionParametersChecking.INVALID_PASSWORD);
	         }
	    	 else{
	    		 setResult(RESULT_OK);
	    	 }
	     }
	     
	     finish();
	 }
	 

}

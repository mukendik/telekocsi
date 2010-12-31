package com.alma.telekocsi.checking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.os.Bundle;

public class ConnectionParametersChecking extends Activity {
	
	public static final int INVALID_EMAIL = 1;
	public static final int INVALID_PASSWORD = 2;
	private static final String emailPattern = "^[\\w-\\.]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$";
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 
		 super.onCreate(savedInstanceState);
		 
		 Bundle extras = getIntent().getExtras();
		 String  email = extras.getString("email").toString();
		 String password1 = extras.getString("password1").toString();
		 String password2 = extras.getString("password2").toString();
		 
		 System.out.println(email);
		 System.out.println(password1);
		 System.out.println(password2);
		 
	     
	     if(wrongEmail(email)){
	    	 setResult(ConnectionParametersChecking.INVALID_EMAIL);
	     }
	     else{
	    	 if(!samePasswords(password1, password2)){
	    		 setResult(ConnectionParametersChecking.INVALID_PASSWORD);
	         }
	    	 else{
	    		 setResult(RESULT_OK);
	    	 }
	     }
	     
	     finish();
	 }
	 
	 private boolean wrongEmail(String email){
		 Pattern p = Pattern.compile(ConnectionParametersChecking.emailPattern);
	     Matcher m = p.matcher(email);
		 return !m.find();
	 }
	 
	 private boolean samePasswords(String password1, String password2){
		 return password1.equals(password2) && !password1.equals("");
	 }
	 

}

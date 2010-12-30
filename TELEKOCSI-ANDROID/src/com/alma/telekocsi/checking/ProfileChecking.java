package com.alma.telekocsi.checking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;

public class ProfileChecking extends Activity {

	public static final int INVALID_NAME = 2;
	public static final int INVALID_FIRST_NAME = 3;
	public static final int INVALID_JJ = 4;
	public static final int INVALID_MM = 5;
	public static final int INVALID_AAAA= 6;
	public static final int INVALID_PHONE = 7;
	
	private static final String phonePattern = "^[0-9]{10,10}$";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		String name = extras.getString("name").toString();
		String firstName = extras.getString("firstName").toString();
		String jj = extras.getString("jj").toString();
		String mm = extras.getString("mm").toString();
		String aaaa = extras.getString("aaaa").toString();
		String phone = extras.getString("phone").toString();
		
		System.out.println(name);
		System.out.println(firstName);
		System.out.println(jj);
		System.out.println(mm);
		System.out.println(aaaa);
		System.out.println(phone);
		
		if(name.equals("")){
			setResult(INVALID_NAME);
		}else if(firstName.equals("")){
			setResult(INVALID_FIRST_NAME);
		}else if(jj.equals("")){
			setResult(INVALID_JJ);
		}else if(mm.equals("")){
			setResult(INVALID_MM);
		}else if(aaaa.equals("")){
			setResult(INVALID_AAAA);
		}else{
			Pattern p = Pattern.compile(phonePattern);
			Matcher m = p.matcher(phone);
			if(!m.find()){
				setResult(INVALID_PHONE);
			}
			else{
				setResult(RESULT_OK);
			}
		}
		finish();
	}
}

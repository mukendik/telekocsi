package com.alma.telekocsi.checking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;

public class ProfileChecking extends Activity {

	public static final int INVALID_NAME = 1;
	public static final int INVALID_FIRST_NAME = 2;
	public static final int INVALID_JJ = 3;
	public static final int INVALID_MM = 4;
	public static final int INVALID_AAAA= 5;
	public static final int INVALID_PHONE = 6;
	
	private static final String phonePattern = "^0[1-9][0-9]{8,8}$";
	private static final String dayPattern = "^[0-9]{2}$";
	private static final String monthPattern = "^[0-9]{2}$";
	private static final String yearPattern = "^[0-9]{4}$";
	
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
		
		if(wrongName(name)){
			setResult(INVALID_NAME);
		}
		else if(wrongFirstName(firstName)){
			setResult(INVALID_FIRST_NAME);
		}
		else if(wrongDay(jj)){
			setResult(INVALID_JJ);
		}
		else if(wrongMonth(mm)){
			setResult(INVALID_MM);
		}
		else if(wrongYear(aaaa)){
			setResult(INVALID_AAAA);
		}
		else if(wrongPhone(phone)){
			setResult(INVALID_PHONE);
		}
		else{
			setResult(RESULT_OK);
		}
		finish();
	}
	
	private boolean wrongName(String name){
		return name.equals("");
	}

	private boolean wrongFirstName(String firstName){
		return firstName.equals("");
	}
	
	private boolean wrongDay(String jj){
		Pattern p = Pattern.compile(dayPattern);
		Matcher m = p.matcher(jj);
		if(!m.find()){
			return true;
		}
		int i = Integer.valueOf(jj);
		if(i<1 || i>31){
			return true;
		}
		return false;
	}
	
	private boolean wrongMonth(String mm){
		Pattern p = Pattern.compile(monthPattern);
		Matcher m = p.matcher(mm);
		if(!m.find()){
			return true;
		}
		int i = Integer.valueOf(mm);
		if(i<1 || i>12){
			return true;
		}
		return false;
	}

	private boolean wrongYear(String aaaa){
		Pattern p = Pattern.compile(yearPattern);
		Matcher m = p.matcher(aaaa);
		if(!m.find()){
			return true;
		}
		int i = Integer.valueOf(aaaa);
		if(i<1900 || i>2012){
			return true;
		}
		return false;
	}
	
	private boolean wrongPhone(String phone){
		Pattern p = Pattern.compile(phonePattern);
		Matcher m = p.matcher(phone);
		if(!m.find()){
			return true;
		}
		return false;
	}

}

package com.alma.telekocsi.checking;

import android.app.Activity;
import android.os.Bundle;

public class PreferencesChecking extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_OK);
		finish();
	}

}

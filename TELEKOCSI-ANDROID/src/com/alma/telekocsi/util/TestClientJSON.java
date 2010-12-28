package com.alma.telekocsi.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.TextView;


public class TestClientJSON extends Activity implements AdapterView.OnItemSelectedListener {
	
	NetWorkInfo netWorkInfo = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState); 
        
        netWorkInfo = new NetWorkInfo();
        
        String result = resultatGAPJSON();
        
        TextView monTextView = new TextView(this);
        setContentView(monTextView);
        monTextView.setText(result);
        
        monTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}	
        });
        
    }


	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	
	public String resultatGAPJSON() {
		
		String LOG_TAG = "Client-JSON";
		String NOM_HOTE = "http://alma-telekocsi.appspot.com";
		String PATH_METHOD = "/resources/hr/employee";
		String PARAM_METHOD = "/tug@grallandco.com";
		String url = NOM_HOTE + PATH_METHOD + PARAM_METHOD;
		
		String email = "";
		String firstName = "";
		String lastName = "";
		String erreur = "";
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			
			HttpGet httpGet = new HttpGet(url);
			
			/* Permet de dialoguer en JSON sinon format XML */
			httpGet.addHeader(new BasicHeader("Accept", "application/json"));
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder stringBuilder = new StringBuilder();
				String ligneLue = bufferReader.readLine();
				while (ligneLue != null) {
					stringBuilder.append(ligneLue + "\n");
					ligneLue = bufferReader.readLine();
				}
				bufferReader.close();
				
				JSONObject jsonObject = new JSONObject(stringBuilder.toString());
				email = jsonObject.getString("email");
				firstName = jsonObject.getString("firstName");
				lastName = jsonObject.getString("lastName");
			}
			
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			erreur = e.getMessage();
			e.printStackTrace();
		}
		
		Log.i(LOG_TAG, "Networkinfo : " + netWorkInfo.getNetWorkInfo());
		Log.i(LOG_TAG, "Email : " + email);
		Log.i(LOG_TAG, "FirstName : " + firstName);
		Log.i(LOG_TAG, "LastName : " + lastName);
		
		return netWorkInfo.getNetWorkInfo() + "; email : " + email + "; firstName : " + firstName + "; lastName : " + lastName + "; erreur : " + erreur;
	}
}
package com.alma.telekocsi.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;


/**
 * 
 * @author ALMA
 * 
 * Necessite d'avoir la permission suivante dans le fichier AndroidManifest.xml :
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 * 
 */
public class NetWorkInfo extends Activity {

	
	public NetWorkInfo() {
		
	}

	
	public String getNetWorkInfo() {
		
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		int networkType;
		State networkState;

		if (networkInfo != null) {
			
			/* Type de connexion : reseau ou wi-fi */
			networkType = networkInfo.getType();

			/* Verification etat de la connexion */
			networkState = networkInfo.getState();

			if ((networkState != null) && (networkState.compareTo(State.CONNECTED) == 0)) {
				return "Connected : " + networkType;
			}
		}
		return "Not connected";
	}
	
}

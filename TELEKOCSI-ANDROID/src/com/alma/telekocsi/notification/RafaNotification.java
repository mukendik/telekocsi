package com.alma.telekocsi.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class RafaNotification extends Activity {
	
	public static final int RAFA = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		TextView txt=new TextView(this);
		txt.setText("Resultat de la notif: rafa is back");
 
		setContentView(txt);
 
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(RAFA);
	}

}

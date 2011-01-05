package com.alma.telekocsi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.alma.telekocsi.notification.RafaNotification;

public class MainMenu extends TabActivity {

	private TabHost tabHost;
	private TabSpec tabSpec;
	private Button addRafaNotification;
	private Button removeRafaNotification;
	private OnClickListener onClickListener = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        tabHost = getTabHost();
        Intent intent = new Intent().setClass(this, DriverTab.class);
        tabSpec = tabHost.newTabSpec("buildings").setIndicator("Conducteur",getResources().getDrawable(R.drawable.volant)).setContent(intent);
        tabHost.addTab(tabSpec);
     
        intent = new Intent().setClass(this, OccupantTab.class);
        tabSpec = tabHost.newTabSpec("research").setIndicator("Passager",getResources().getDrawable(R.drawable.autostop)).setContent(intent);
        tabHost.addTab(tabSpec);
        
		addRafaNotification = (Button)findViewById(R.id.add_notification_test);
		addRafaNotification.setOnClickListener(getOnClickListener());
		
		removeRafaNotification = (Button)findViewById(R.id.add_notification_test);
		removeRafaNotification.setOnClickListener(getOnClickListener());
		
    }
	
	private OnClickListener getOnClickListener(){
		if(onClickListener==null){
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	private OnClickListener makeOnClickListener(){
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==addRafaNotification){
					createNotify();
				}
				else if(v==removeRafaNotification){
					cancelNotify();
				}
			}
			
		};
	}
	
   private void createNotify(){
    	NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);        
    	Notification notification = new Notification(R.drawable.notifications, "Rafa notification!", System.currentTimeMillis());  
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, RafaNotification.class), 0);
        notification.setLatestEventInfo(this, "Vamos", "AO 2011 in ze pocket", pendingIntent);
        notification.vibrate = new long[] {0,200,100,200,100,200};
        notificationManager.notify(RafaNotification.RAFA, notification);
    }
 
    private void cancelNotify(){
    	NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancel(RafaNotification.RAFA);
    }
	
}
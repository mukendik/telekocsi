package com.alma.telekocsi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainMenu extends TabActivity {

	private TabHost tabHost;
	private TabSpec tabSpec;
	private Button profileModificationButton;
	private Button routeModificationButton;
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
        
        profileModificationButton = (Button)findViewById(R.id.profile_modification_button);
		profileModificationButton.setOnClickListener(getOnClickListener());
		
		routeModificationButton = (Button)findViewById(R.id.route_modification_button);
		routeModificationButton.setOnClickListener(getOnClickListener());
     
//        tabHost.setCurrentTab(0);
        
//        Intent intent = new Intent(this, ActiviteTab.class);
//        tabHost = getTabHost();
//        
//        intent.putExtra("valeur","hi");
//        
//        tabSpec = tabHost.newTabSpec("conducteur").setIndicator("Conducteur", 
//        		getResources().getDrawable(R.drawable.volant)).setContent(intent);
//        tabHost.addTab(tabSpec);
//        
//        
//        intent.putExtra("valeur", "Hello world !");
//        
//        tabSpec = tabHost.newTabSpec("passager").setIndicator("Passager", 
//        		getResources().getDrawable(R.drawable.autostop)).setContent(intent);
//        tabHost.addTab(tabSpec);
        
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
				if(v==profileModificationButton){
					startProfileModification();
				}
				else if(v==routeModificationButton){
					startRouteModification();
				}
			}
			
		};
	}
	
	private void startProfileModification(){
		Intent intent = new Intent(this, ProfileSettings.class);
		startActivity(intent);
	}
	
	private void startRouteModification(){
		Intent intent = new Intent(this, RouteModification.class);
		startActivity(intent);
	}
	
}
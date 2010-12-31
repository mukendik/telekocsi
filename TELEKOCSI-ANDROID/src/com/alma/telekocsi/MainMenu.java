package com.alma.telekocsi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainMenu extends TabActivity {

	private TabHost tabHost;
	private TabSpec tabSpec;
	
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
}
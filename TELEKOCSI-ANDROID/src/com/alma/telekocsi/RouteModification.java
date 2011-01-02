package com.alma.telekocsi;

import android.app.Activity;
import android.os.Bundle;

public class RouteModification extends OptionsMenu {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//        setContentView(R.layout.route_modification);
        
	}

	@Override
	protected void showMainMenu() {
		//appelee a partir du menu principal donc on peut finir
		finish();
	}
        

}

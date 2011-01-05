package com.alma.telekocsi;

import android.os.Bundle;

public class RouteModification extends OptionsMenu {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void showMainMenu() {
		finish();
	}
        

}

package com.alma.telekocsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuOld extends OptionsMenu {
	
	private OnClickListener onClickListener = null;
	private Button routeActivationButton;
	private Button routeSearchButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
    
		setContentView(R.layout.main_menu);
		
		routeActivationButton = (Button)findViewById(R.id.route_activation_button);
		routeActivationButton.setOnClickListener(getOnClickListener());
		
		routeSearchButton = (Button)findViewById(R.id.route_search_button);
		routeSearchButton.setOnClickListener(getOnClickListener()); 
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
				if(v==routeSearchButton){
					startSearch();
				}
			}
			
		};
	}
	
	private void startSearch(){
		Intent intent = new Intent(this, TrajetRecherche.class);
		startActivity(intent);
	}

	@Override
	protected void showMainMenu() {
		//nothing to do
	}

}

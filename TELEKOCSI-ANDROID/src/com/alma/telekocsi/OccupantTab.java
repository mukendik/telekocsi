package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OccupantTab extends Activity {
	
	private OnClickListener onClickListener = null;
	private Button routeSearchButton;
	private Button occupantTransactionButton;
	private Button profileModificationButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onglet_passager);

		routeSearchButton = (Button)findViewById(R.id.route_search_button);
		routeSearchButton.setOnClickListener(getOnClickListener());
		
		occupantTransactionButton = (Button)findViewById(R.id.occupant_transaction_button);
		occupantTransactionButton.setOnClickListener(getOnClickListener());
		
		profileModificationButton = (Button)findViewById(R.id.profile_modification_button);
		profileModificationButton.setOnClickListener(getOnClickListener());
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
				else if(v==occupantTransactionButton){
					startTransaction();
				}
				else if(v==profileModificationButton){
					startProfileModification();
				}
			}
			
		};
	}
	
	private void startSearch(){
		Intent intent = new Intent(this, TrajetRecherche.class);
		startActivity(intent);
	}
	
	private void startTransaction(){
		Intent intent = new Intent(this, Transaction.class);
		startActivity(intent);
	}
	
	private void startProfileModification(){
		Intent intent = new Intent(this, ProfileSettings.class);
		startActivity(intent);
	}
	
}

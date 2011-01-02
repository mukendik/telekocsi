package com.alma.telekocsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OccupantTab extends OptionsMenu {
	
	private OnClickListener onClickListener = null;
	private Button routeSearchButton;
	private Button occupantTransactionButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onglet_passager);

		routeSearchButton = (Button)findViewById(R.id.route_search_button);
		routeSearchButton.setOnClickListener(getOnClickListener());
		
		occupantTransactionButton = (Button)findViewById(R.id.occupant_transaction_button);
		occupantTransactionButton.setOnClickListener(getOnClickListener());
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
	
}

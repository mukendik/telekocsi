package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DriverTab extends Activity {
	
	private OnClickListener onClickListener = null;
	private Button routeActivationButton;
	private Button driverTransactionButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onglet_conducteur);

		routeActivationButton = (Button)findViewById(R.id.route_activation_button);
		routeActivationButton.setOnClickListener(getOnClickListener());
		
		driverTransactionButton = (Button)findViewById(R.id.driver_transaction_button);
		driverTransactionButton.setOnClickListener(getOnClickListener());
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
				if(v==routeActivationButton){
					startActivation();
				}
				else if(v==driverTransactionButton){
					startTransaction();
				}
			}
			
		};
	}
	
	private void startActivation(){
		Intent intent = new Intent(this, RouteActivation.class);
		startActivity(intent);
	}
	
	private void startTransaction(){
		Intent intent = new Intent(this, Transaction.class);
		startActivity(intent);
	}

}

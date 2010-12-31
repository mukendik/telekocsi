package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RouteActivation extends Activity {
	
	private Button routeActivationButton;
	private Button cancelButton;
	private OnClickListener onClickListener = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.route_activation);
        
        routeActivationButton = (Button)findViewById(R.id.start_route_activation);
        routeActivationButton.setOnClickListener(getOnClickListener());
        
        cancelButton = (Button)findViewById(R.id.cancel_route_activation);
        cancelButton.setOnClickListener(getOnClickListener());
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
				if(v==cancelButton){
					goBack();
				}
//				else if(v==routeActivationButton){
//				startRouteActivation();
//			}
			}
			
		};
	}
	
//	private void startRouteActivation(){
//		Intent intent = new Intent(this, RouteActivationStarter.class);
//		startActivity(intent);
//	}
	
	private void goBack(){
		finish();
	}
	

}

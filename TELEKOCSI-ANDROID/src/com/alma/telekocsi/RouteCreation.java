package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RouteCreation extends Activity {

	private Button startRouteCreationButton;
	private Button cancelRouteCreationButton;
	private OnClickListener onClickListener = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.route_creation);
        
        startRouteCreationButton = (Button)findViewById(R.id.start_route_creation_button);
        startRouteCreationButton.setOnClickListener(getOnClickListener());

        cancelRouteCreationButton = (Button)findViewById(R.id.cancel_route_creation_button);
        cancelRouteCreationButton.setOnClickListener(getOnClickListener());
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
				if(v==cancelRouteCreationButton){
					goBack();
				}
//				else if(v==startRouteCreationButton){
//					startRouteCreation();
//				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
//	private void startRouteCreation(){
//		Intent intent = new Intent(this, StartRouteCreation.class);
//		startActivity(intent);
//	}
	
}

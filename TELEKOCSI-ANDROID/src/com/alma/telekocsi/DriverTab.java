package com.alma.telekocsi;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class DriverTab extends OptionsMenu {
	
	private OnClickListener onClickListener = null;
	private Button routeActivationButton;
	private Button routeCreationButton;
	private Button driverTransactionButton;
	private Button routeModificationButton;
	private Button activatedRouteButton;
	private Session session = null;
	Profil profile = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onglet_conducteur);

		routeActivationButton = (Button)findViewById(R.id.route_activation_button);
		routeActivationButton.setOnClickListener(getOnClickListener());
		
		driverTransactionButton = (Button)findViewById(R.id.driver_transaction_button);
		driverTransactionButton.setOnClickListener(getOnClickListener());
		
		routeCreationButton = (Button)findViewById(R.id.route_creation_button);
		routeCreationButton.setOnClickListener(getOnClickListener());
		
		routeModificationButton = (Button)findViewById(R.id.route_modification_button);
		routeModificationButton.setOnClickListener(getOnClickListener());
		
		activatedRouteButton = (Button)findViewById(R.id.activated_route_map);
		activatedRouteButton.setOnClickListener(getOnClickListener());

		session = SessionFactory.getCurrentSession(this);
		profile = session.getActiveProfile();
		
		session.switchToDriverType();
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
					startRouteActivation();
				}
				else if(v==driverTransactionButton){
					startDriverTransaction();
				}
				else if(v==routeCreationButton){
					startRouteCreation();
				}
				else if(v==routeModificationButton){
					startRouteModification();
				}
				else if(v==activatedRouteButton){
					showActiveRoute();
				}
			}
			
		};
	}
	
	private void startRouteActivation(){
		Intent intent = new Intent(this, RouteActivation.class);
		startActivity(intent);
	}
	
	private void startDriverTransaction(){
		Intent intent = new Intent(this, Transaction.class);
		startActivity(intent);
	}
	
	private void startRouteCreation(){
		Intent intent = new Intent(this, RouteCreation.class);
		startActivity(intent);
	}


	private void startRouteModification(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.route_modification));
		
		final List<Trajet> routes = session.getRoutes();
		String[] names = new String[routes.size()];
		for(int i=0;i<names.length;++i){
			Trajet route = routes.get(i);
			names[i] = String.format("%s -> %s",route.getLieuDepart(),route.getLieuDestination());
		}
		final AlertDialog dialog;
		
		builder.setSingleChoiceItems(names, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(DriverTab.this, RouteModification.class);
				intent.putExtra(RouteModification.ROUTE_ARG, routes.get(which).getId());
				startActivity(intent);
			}
			
		});
		
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		dialog = builder.create();
		dialog.show();
	}	
 	
	private void showActiveRoute(){
		startActivity(new Intent(this, GoogleMapActivity.class));
	}
	
	@Override
	protected void showMainMenu() {
		//nothing to do
	}

}

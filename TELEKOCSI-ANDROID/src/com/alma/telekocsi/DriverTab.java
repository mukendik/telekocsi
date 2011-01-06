package com.alma.telekocsi;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class DriverTab extends ListActivity {
	
	private OnItemClickListener onItemClickListener = null;
	private ListView listView;
	private Session session = null;
	Profil profile = null;
	
	private static final String ACTIVATE = "Activer un trajet";
	private static final String CREATE = "Créer un nouveau trajet";
	private static final String MODIFY = "Modifier un trajet";
	private static final String ACTIVATED = "Trajet actif";
	private static final String TRANSACTION = "Valider une transaction";
	
	private static final String[] DRIVER_FUNCTIONS = new String[] {
		ACTIVATE, CREATE, MODIFY, ACTIVATED, TRANSACTION
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this
												,R.layout.onglet_conducteur
												,DRIVER_FUNCTIONS)
		);

		listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(getOnItemClickListener());
		
		session = SessionFactory.getCurrentSession(this);
		profile = session.getActiveProfile();
		
		session.switchToDriverType();
	}
	
	private OnItemClickListener getOnItemClickListener(){
		if(onItemClickListener==null){
			onItemClickListener = makeOnItemClickListener();
		}
		return onItemClickListener;
	}
	
	private OnItemClickListener makeOnItemClickListener(){
		
		return new OnItemClickListener() {
			
		    public void onItemClick(AdapterView<?> parent
		    						,View view
		    						,int position
		    						,long id) {
		    	if(((TextView) view).getText().equals(ACTIVATE)){
		    		startRouteActivation();
		    	}
		    	else if(((TextView) view).getText().equals(CREATE)){
		    		startRouteCreation();
		    	}
		    	else if(((TextView) view).getText().equals(MODIFY)){
		    		startRouteModification();
		    	}
		    	else if(((TextView) view).getText().equals(ACTIVATED)){
		    		showActiveRoute();
		    	}
		    	else if(((TextView) view).getText().equals(TRANSACTION)){
		    		startDriverTransaction();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.disconnection_id:
			disconnect();
			return true;
		case R.id.notifications_id:
			showNotifications();
			return true;
		case R.id.main_menu_id:
			return true;
		case R.id.preferences_id:
			showPreferences();
			return true;
		case R.id.profile_id:
			showProfileSettings();
			return true;
	    }
	    return false;
	}
	
	private void disconnect(){
		//fermeture de la session
		SessionFactory.getCurrentSession(this).logout();
		
		//fermeture de lactivite
		finish();
		
		//retour a la page didentification
		Intent intent = new Intent(this, Identification.class);
		startActivity(intent);
	}
	
	private void showNotifications(){
	}

	private void showPreferences(){
		Intent intent = new Intent(this, Preferences.class);
		startActivityForResult(intent, Preferences.RESULT);
	}
	
	private void showProfileSettings(){		
		Intent intent = new Intent(this, ProfileSettings.class);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == Preferences.RESULT) {
			saveProfileAndNotify();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void saveProfileAndNotify(){
		//recuperation des preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//et du profil du user
		Session session = SessionFactory.getCurrentSession(this);
		Profil profile = session.getActiveProfile();
		//mise a jour
		profile.setEmail(preferences.getString("email", profile.getEmail()));
		profile.setMotDePasse(preferences.getString("password", profile.getMotDePasse()));
		session.saveProfile(profile);
		//notification rapide
		Toast.makeText(this, "Modifications enregistrées", Toast.LENGTH_SHORT).show();
	}
	

}

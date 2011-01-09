package com.alma.telekocsi;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
	
	private String ACTIVATE;
	private String CREATE;
	private String MODIFY;
	private String ACTIVATED;
	private String TRANSACTION;
	
	private String[] DRIVER_FUNCTIONS;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Penser a l'internationalisation 
		ACTIVATE = getString(R.string.route_activation_title);
		CREATE = getString(R.string.route_creation);
		MODIFY = getString(R.string.route_modification);
		ACTIVATED = getString(R.string.activated_route_map_text);
		TRANSACTION = getString(R.string.validerTransaction);

		DRIVER_FUNCTIONS = new String[] {
				ACTIVATE, CREATE, ACTIVATED, TRANSACTION
		};
		
		setListAdapter(new ArrayAdapter<String>(this
												,R.layout.main_menu_tab_list
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
//		    	else if(((TextView) view).getText().equals(MODIFY)){
//		    		startRouteModification();
//		    	}
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
		Intent intent = new Intent(this, TrajetActivation.class);
		startActivity(intent);
	}
	
	private void startDriverTransaction(){
		Intent intent = new Intent(this, Transaction.class);
		startActivity(intent);
	}
	
	private void startRouteCreation(){
		Intent intent = new Intent(this, ItineraireCreation.class);
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
				//on affiche la l'ecran de creation
				//au lieu de repeter le code dans modification
				Intent intent = new Intent(DriverTab.this, ItineraireCreation.class);
				intent.putExtra(ItineraireCreation.ROUTE_ARG, routes.get(which).getId());
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
			//nothing to do
			return true;
		case R.id.preferences_id:
			showPreferences();
			return true;
		case R.id.profile_id:
			showProfileSettings();
			return true;
		case R.id.routes_id:
			showRoutesManaging();
			return true;
	    }
	    return false;
	}
	
	private void showRoutesManaging(){
		Intent intent = new Intent(this, ItinerairesManaging.class);
		startActivity(intent);
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
			System.out.println("RESULT");
			saveProfileAndNotify();
		}else{
			System.out.println("RESULT NO");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void saveProfileAndNotify(){
		//recuperation des preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//et du profil du user
		Session session = SessionFactory.getCurrentSession(this);
		Profil profile = session.getActiveProfile();
		Log.d(getClass().getSimpleName(), "saveProfilAndNotify : " + profile);
		//mise a jour
		profile.setEmail(preferences.getString("email", profile.getEmail()));
		profile.setPseudo(preferences.getString("email", profile.getPseudo()));
		profile.setMotDePasse(preferences.getString("password", profile.getMotDePasse()));
		session.saveProfile(profile);
		System.out.println("PROFILE="+profile);
		Log.i(getClass().getName(), profile.toString());
		//notification rapide
		Toast.makeText(this, getString(R.string.profile_creation_ongoing), Toast.LENGTH_SHORT).show();
	}
	

}

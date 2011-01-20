package com.alma.telekocsi;

import java.util.Arrays;

import android.app.ListActivity;
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
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class OccupantTab extends ListActivity {

	private OnItemClickListener onItemClickListener = null;
	private ListView listView;
	private Session session = null;
	Profil profile = null;
	
	private  String SEARCH;
	private  String ACTIVATED;
	private  String TRANSACTION;
	
	private  String[] OCCUPANT_FUNCTIONS;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		//Internationalisation
		SEARCH = getString(R.string.route_search);
		ACTIVATED = getString(R.string.activated_route_map_text);
		TRANSACTION = getString(R.string.transaction_validation);
		OCCUPANT_FUNCTIONS = new String[] {
				SEARCH, ACTIVATED, TRANSACTION
		};
		
		setListAdapter(new ArrayAdapter<String>(this
												,R.layout.main_menu_tab_list
												,OCCUPANT_FUNCTIONS)
		);

		listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(getOnItemClickListener());

		session = SessionFactory.getCurrentSession(this);
		profile = session.getActiveProfile();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		session.switchToPassengerType();
	}


	@Override
	protected void onStart() {
		super.onStart();
		session.switchToPassengerType();
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
		    	if(((TextView) view).getText().equals(SEARCH)){
		    		startSearch();
		    	}
		    	else if(((TextView) view).getText().equals(ACTIVATED)){
		    		showActiveRoute();
		    	}
		    	else if(((TextView) view).getText().equals(TRANSACTION)){
		    		//startTransaction();
		    		startTransactionForDemo();
		    	}
		    }
		};
	}
	
	private void startSearch(){
		Intent intent = new Intent(this, TrajetRecherche.class);
		startActivity(intent);
	}
	
	private void startTransaction(){
		Trajet route = session.getActiveTrajet();
		if(route==null){
			Toast.makeText(this, R.string.no_active_route, Toast.LENGTH_SHORT).show();
			return;
		}
		
		Profil driver = session.find(Profil.class, route.getIdProfilConducteur());
		if(driver==null){
			Toast.makeText(this, R.string.no_transaction_to_validate, Toast.LENGTH_SHORT).show();
			return;
		}
		
		Bundle bundle = new Bundle();		
		bundle.putSerializable(Transaction.ORIGINATOR, profile);
		bundle.putSerializable(Transaction.DESTINATOR, driver);
		
		Intent intent = new Intent(this, Transaction.class);
		intent.putExtras(bundle);
		
		startActivity(intent);
	}

	private void startTransactionForDemo(){
		ProfilDAO profilDAO = new ProfilDAO();
		
		final String marcChristieID = profilDAO.login("passager@mail.com", "aze").getId();
		final String rgID = profilDAO.login("rg@passager.fr", "aze").getId();

		Profil lewisHamilton = profilDAO.login("conducteur@mail.com","aze");
		
		Log.i(getClass().getName(),"Fetching active route");

//		String trajetId = "ag5hbG1hLXRlbGVrb2NzaXIOCxIGVHJhamV0GM2LEQw";	
//		session.find(Trajet.class, trajetId);
		Trajet route = new TrajetDAO().getList(lewisHamilton.getId()).get(0); 
		if(route==null || !Arrays.asList(marcChristieID,rgID).contains(profile.getId())){
			Toast.makeText(this, R.string.no_active_route, Toast.LENGTH_SHORT).show();
			return;
		}

		Log.i(getClass().getName(),"Fetching driver profile");
		
		Profil driver = lewisHamilton;
		if(driver==null){
			Toast.makeText(this, R.string.no_transaction_to_validate, Toast.LENGTH_SHORT).show();
			return;
		}

		Log.i(getClass().getName(),"Starting intent");
		
		Bundle bundle = new Bundle();		
		bundle.putSerializable("DEMO_ROUTE",route);
		bundle.putSerializable(Transaction.ORIGINATOR, profile);
		bundle.putSerializable(Transaction.DESTINATOR, driver);
		
		Intent intent = new Intent(this, Transaction.class);
		intent.putExtras(bundle);
		
		startActivity(intent);
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
		Intent intent = new Intent(this, ItineraireManaging.class);
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

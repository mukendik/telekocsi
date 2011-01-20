package com.alma.telekocsi;

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
import com.alma.telekocsi.dao.profil.ProfilDAO;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class DriverTab extends ListActivity {
	
	private OnItemClickListener onItemClickListener = null;
	private ListView listView;
	private Session session = null;
	Profil profile = null;
	
	private String TRAJET_MANAGING;
	private String CREATE_ITINERAIRE;
	private String VIEW_TRAJET_MAP;
	private String TRANSACTION;
	
	private String[] DRIVER_FUNCTIONS;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		TRAJET_MANAGING = getString(R.string.tm_titre_trajetManaging);
		CREATE_ITINERAIRE = getString(R.string.route_creation);
		VIEW_TRAJET_MAP = getString(R.string.activated_route_map_text);
		TRANSACTION = getString(R.string.validerTransaction);

		DRIVER_FUNCTIONS = new String[] {
				TRAJET_MANAGING, CREATE_ITINERAIRE, VIEW_TRAJET_MAP, TRANSACTION
		};
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.main_menu_tab_list,
				DRIVER_FUNCTIONS));

		listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(getOnItemClickListener());
		
		session = SessionFactory.getCurrentSession(this);
		profile = session.getActiveProfile();
		
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		session.switchToDriverType();
	}


	@Override
	protected void onStart() {
		super.onStart();
		session.switchToDriverType();
	}


	private OnItemClickListener getOnItemClickListener(){
		if (onItemClickListener == null) {
			onItemClickListener = makeOnItemClickListener();
		}
		return onItemClickListener;
	}
	
	private OnItemClickListener makeOnItemClickListener(){
		
		return new OnItemClickListener() {
			
		    public void onItemClick(AdapterView<?> parent
		    		, View view, int position, long id) {

		    	if (((TextView) view).getText().equals(TRAJET_MANAGING)) {
		    		startTrajetManaging();
		    	}
		    	else if(((TextView) view).getText().equals(CREATE_ITINERAIRE)) {
		    		startRouteCreation();
		    	}
		    	else if(((TextView) view).getText().equals(VIEW_TRAJET_MAP)) {
		    		showActiveRouteOnMap();
		    	}
		    	else if(((TextView) view).getText().equals(TRANSACTION)) {
		    		//startDriverTransaction();
		    		startTransactionForDemo();
		    	}
		    }
		};
	}

	
	private void startTrajetManaging(){
		Intent intent = new Intent(this, TrajetManaging.class);
		startActivity(intent);
	}
	
	
	private void startDriverTransaction() {
		final Profil[] passengers = session.getActivePassengersProfiles();
		final Profil[] selected = { null };
		
		//On doit choisir le passager destinataire des points de la transaction
		if(passengers.length<1){
			Toast.makeText(this, R.string.no_transaction_to_validate, Toast.LENGTH_SHORT).show();
			return;
		}
		else if(passengers.length==1){
			doValidateTransaction(profile, passengers[0]);
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final AlertDialog dialog;
			builder.setTitle(getString(R.string.passager));
			
			String[] names = new String[passengers.length];
			for(int i=0;i<passengers.length;++i){
				names[i] = passengers[i].getPrenom()+passengers[i].getNom().substring(0, 1).toUpperCase()+".";
			}
			builder.setSingleChoiceItems(names, 0, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(dialog!=null){
						dialog.dismiss();
					}
					doValidateTransaction(profile, passengers[which]);
				}
				
			});
			dialog = builder.create();
			dialog.show();
		}
	}
	
	private void startRouteCreation(){
		Intent intent = new Intent(this, ItineraireCreation.class);
		startActivity(intent);
	}	
 	
	private void showActiveRouteOnMap(){
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

	private void startTransactionForDemo(){
		ProfilDAO profilDAO = new ProfilDAO();
		
		final Profil marcChristie = profilDAO.login("passager@mail.com", "aze");
		final Profil rg = profilDAO.login("rg@passager.fr", "aze");
		
		final Profil[] passengers = marcChristie==null?new Profil[]{}:new Profil[]{marcChristie,rg};
		
		//On doit choisir le passager destinataire des points de la transaction
		if(passengers.length<1 || marcChristie.getId().equals(profile.getId()) || rg.getId().equals(profile.getId())){
			Toast.makeText(this, R.string.no_transaction_to_validate, Toast.LENGTH_SHORT).show();
			return;
		}
		else if(passengers.length==1){
			doValidateTransaction(profile, passengers[0]);
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final AlertDialog dialog;
			builder.setTitle(getString(R.string.passager));
			
			String[] names = new String[passengers.length];
			for(int i=0;i<passengers.length;++i){
				names[i] = passengers[i].getPrenom()+" "+passengers[i].getNom().substring(0, 1).toUpperCase()+".";
			}
			builder.setSingleChoiceItems(names, 0, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(dialog!=null){
						dialog.dismiss();
					}
					doValidateTransaction(profile, passengers[which]);
				}
				
			});
			dialog = builder.create();
			dialog.show();
		}
		
	}
	
	private void doValidateTransaction(Profil originator,Profil destinator){
		Bundle bundle = new Bundle();		
		bundle.putSerializable(Transaction.ORIGINATOR, originator);
		bundle.putSerializable(Transaction.DESTINATOR, destinator);
		
		Intent intent = new Intent(this, Transaction.class);
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
	
}

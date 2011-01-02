package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class OptionsMenu extends Activity {
	
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
			return true;
		case R.id.main_menu_id:
			showMainMenu();
			return true;
	    }
	    return false;
	}
	
	private void showMainMenu(){
		
	}
	
	private void disconnect(){
		//fermer la session
		
		//retour a la page didentification
		Intent intent = new Intent(this, Identification.class);
		startActivity(intent);
	}


}
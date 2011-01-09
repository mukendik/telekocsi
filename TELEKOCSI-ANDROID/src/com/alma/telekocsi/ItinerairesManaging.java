package com.alma.telekocsi;

import java.util.List;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ItinerairesManaging extends ARunnableActivity {
	
	private static final int MODIFY = 1;
	private static final int ACTIVATE = 2;
	private static final int DELETE = 3;
	
	Button backButton;
	Button createNewRouteButton;
	ListView listView;
	OnClickListener onClickListener = null;
	
	Session session;
	Profil profile;
	List<Trajet> routes;
	List<Itineraire> itineraires;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.routes_managing);
        
        backButton = (Button)findViewById(R.id.routes_managing_back_id);
        backButton.setOnClickListener(getOnClickListener());
        
        createNewRouteButton = (Button)findViewById(R.id.routes_managing_creation_id);
        createNewRouteButton.setOnClickListener(getOnClickListener());

        listView = (ListView)findViewById(R.id.routes_array_id);
        
        session = SessionFactory.getCurrentSession(this);
		profile = session.getActiveProfile();
		
		printItineraires();
	}
	
	private void printItineraires(){
		itineraires = session.getItineraires();
		if(itineraires.isEmpty()){
			Toast.makeText(this, R.string.no_route_registered, Toast.LENGTH_SHORT).show();
		}
		else{
			String[] names = new String[itineraires.size()];
			for(int i=0;i<names.length;++i){
				Itineraire itineraire = itineraires.get(i);
				names[i] = String.format("%s -> %s",itineraire.getLieuDepart(),itineraire.getLieuDestination());
			}
			listView.setAdapter(new ArrayAdapter<String>(this
														,R.layout.main_menu_tab_list
														,names)
			);
			registerForContextMenu(listView);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MODIFY, 0, R.string.routes_managing_modify);
		menu.add(0, ACTIVATE, 0,  R.string.routes_managing_activate);
		menu.add(0, DELETE, 0,  R.string.routes_managing_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo infos = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case MODIFY:
			startRouteModification(infos);
			return true;
		case ACTIVATE:
			startRouteActivation(infos);
			return true;
		case DELETE:
			startRouteDeletation(infos);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private void startRouteModification(AdapterContextMenuInfo routeInfos){
		Intent intent = new Intent(ItinerairesManaging.this, ItineraireCreation.class);
		intent.putExtra(ItineraireCreation.ROUTE_ARG, itineraires.get(routeInfos.position).getId());
		startActivity(intent);
	}
	
	private void startRouteDeletation(AdapterContextMenuInfo routeInfos){
		
	}
	
	private void startRouteActivation(AdapterContextMenuInfo routeInfos){
		Intent intent = new Intent(this, TrajetActivation.class);
		startActivity(intent);
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
				if(v==backButton){
					goBack();
				}
				else if(v==createNewRouteButton){
					startRouteCreation();
				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
	private void startRouteCreation(){
		Intent intent = new Intent(this, ItineraireCreation.class);
		startActivity(intent);
	}
	
	@Override
	public void run() {
		
		
	}

}

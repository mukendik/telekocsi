package com.alma.telekocsi;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.dao.trajet.TrajetDAO;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class DriverRouteList extends ListActivity {
	Session session;
	Profil profile;
	TrajetDAO trajetDAO;
	List<Trajet> routes;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		listView = new ListView(this);
		listView.setId(android.R.id.list);
		listView.setBackgroundColor(getResources().getColor(R.color.fond));	
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		LayoutParams layoutParams = new LayoutParams();
		layoutParams.width = LayoutParams.FILL_PARENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		addContentView(listView,layoutParams);
		
		TextView textView = new TextView(this);
		textView.setId(android.R.id.empty);
		textView.setBackgroundColor(getResources().getColor(R.color.fond));
		textView.setText("No Data");
		layoutParams = new LayoutParams();
		layoutParams.width = LayoutParams.FILL_PARENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		addContentView(textView,layoutParams);
		
		session = SessionFactory.getCurrentSession(this);
		profile = session.getActiveProfile();
		trajetDAO = new TrajetDAO();

		setListAdapter(new MyAdapter());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onStart() {
		super.onStart();		
	}

	/**
	 *  A custom adapter
	 * @author steg
	 *
	 */
	class MyAdapter extends ArrayAdapter<Trajet>{
		public MyAdapter() {
			super(DriverRouteList.this, android.R.id.empty , routes=session.getRoutes());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {		
			if(convertView==null){
				
			}
			TextView row = new TextView(DriverRouteList.this);
			row.setFocusable(true);
			row.setLongClickable(true);
			row.setClickable(true);
			row.setCursorVisible(true);
			row.setBackgroundColor(getResources().getColor(R.color.fond));
			row.setTextColor(getResources().getColor(android.R.color.black));
			row.setHighlightColor(getResources().getColor(android.R.color.background_light));
			row.setGravity(Gravity.CENTER);
			Trajet trajet = routes.get(position);
			row.setText(String.format("%s -> %s", trajet.getLieuDepart(),trajet.getLieuDestination(),trajet.getId()));
			return row;
		}
		
		
	}
}

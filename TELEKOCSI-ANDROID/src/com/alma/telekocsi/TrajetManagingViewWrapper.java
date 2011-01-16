package com.alma.telekocsi;

import android.view.View;
import android.widget.TextView;


public class TrajetManagingViewWrapper {

	View base;

	TextView tvDate = null;
	TextView tvState = null;
	TextView tvDepart = null;
	TextView tvArrivee = null;
	TextView tvPlace = null;
	TextView tvPoint = null;
	
	
	TrajetManagingViewWrapper(View base) {
		
		this.base = base;
	}

	
	TextView getDate() {
		
		if (tvDate == null) {
			tvDate = (TextView)base.findViewById(R.id.tvTMRowDate);
		}
		return tvDate;
	}

	TextView getState() {
		
		if (tvState == null) {
			tvState = (TextView)base.findViewById(R.id.tvTMRowState);
		}
		return tvState;
	}
	
	TextView getDepart() {
		
		if (tvDepart == null) {
			tvDepart = (TextView)base.findViewById(R.id.tvTMRowDepart);
		}
		return tvDepart;
	}

	
	TextView getArrivee() {
		
		if (tvArrivee == null) {
			tvArrivee = (TextView)base.findViewById(R.id.tvTMRowArrivee);
		}
		return tvArrivee;
	}

	
	TextView getPlace() {
		
		if (tvPlace == null) {
			tvPlace = (TextView)base.findViewById(R.id.tvTMRowPlace);
		}
		return tvPlace;
	}

	
	TextView getPoint() {
		
		if (tvPoint == null) {
			tvPoint = (TextView)base.findViewById(R.id.tvTMRowPoint);
		}
		return tvPoint;
	}
}

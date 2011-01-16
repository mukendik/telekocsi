package com.alma.telekocsi;

import android.view.View;
import android.widget.TextView;


public class TrajetManagingViewWrapper {

	View base;

	TextView tvDate = null;
	TextView tvDepart = null;
	TextView tvArrivee = null;
	TextView tvInfo = null;
	
	
	TrajetManagingViewWrapper(View base) {
		
		this.base = base;
	}

	
	TextView getDate() {
		
		if (tvDate == null) {
			tvDate = (TextView)base.findViewById(R.id.tvTMRowDate);
		}
		return tvDate;
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

	
	TextView getInfo() {
		
		if (tvInfo == null) {
			tvInfo = (TextView)base.findViewById(R.id.tvTMRowInfo);
		}
		return tvInfo;
	}
	
}

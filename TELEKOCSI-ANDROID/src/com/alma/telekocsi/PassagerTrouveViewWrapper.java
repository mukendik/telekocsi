package com.alma.telekocsi;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class PassagerTrouveViewWrapper {

	View base;
	TextView nomConducteur = null;
	TextView nombreAvis = null;
	TextView dateHeure = null;
	TextView nbrePlaceDispo = null;
	TextView nbrePoint = null;
	ImageView visage = null;
	ImageView starsClassement = null;
	
	
	PassagerTrouveViewWrapper(View base) {
		
		this.base = base;
	}

	
	TextView getNomConducteur() {
		
		if (nomConducteur == null) {
			nomConducteur = (TextView)base.findViewById(R.id.tvTTRowNomConducteur);
		}
		return nomConducteur;
	}

	
	TextView getNombreAvis() {
		
		if (nombreAvis == null) {
			nombreAvis = (TextView)base.findViewById(R.id.tvTTRowNombreAvis);
		}
		return nombreAvis;
	}

	
	TextView getDateHeure() {
		
		if (dateHeure == null) {
			dateHeure = (TextView)base.findViewById(R.id.tvTTRowDateHeure);
		}
		return dateHeure;
	}

	
	TextView getNbrePlaceDispo() {
		
		if (nbrePlaceDispo == null) {
			nbrePlaceDispo = (TextView)base.findViewById(R.id.tvTTRowNbPlaceDispo);
		}
		return nbrePlaceDispo;
	}
	

	TextView getNbrePoint() {
		
		if (nbrePoint == null) {
			nbrePoint = (TextView)base.findViewById(R.id.tvTTRowNbPoint);
		}
		return nbrePoint;
	}

	
	ImageView getVisage() {
		
		if (visage == null) {
			visage = (ImageView)base.findViewById(R.id.ivTTVisage);
		}
		return visage;
	}

	
	ImageView getStarsClassement() {
		
		if (starsClassement == null) {
			starsClassement = (ImageView)base.findViewById(R.id.ivTTStarts);
		}
		return starsClassement;
	}
	
}

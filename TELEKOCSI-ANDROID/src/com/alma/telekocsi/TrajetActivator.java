package com.alma.telekocsi;

import com.alma.telekocsi.dao.itineraire.Itineraire;
import com.alma.telekocsi.dao.trajet.Trajet;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;
import com.alma.telekocsi.util.LocalDate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TrajetActivator extends Activity {

	private Itineraire itineraire = null;
	private LocalDate date = null;
	
	Session session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		/*
		session = SessionFactory.getCurrentSession(this);
        
		Bundle bundle = this.getIntent().getExtras();
        itineraire = (Itineraire)bundle.getSerializable("itineraire");
        date = (LocalDate)bundle.getSerializable("date");
        
        Trajet trajet = new Trajet();
        trajet.setAutoroute(itineraire.isAutoroute());
        trajet.setCommentaire(itineraire.getCommentaire());
        trajet.setDateTrajet(date.getDateFomatHeure());
        trajet.setFrequenceTrajet(itineraire.getFrequenceTrajet());
        trajet.setHoraireArrivee(itineraire.getHoraireArrivee());
        trajet.setHoraireDepart(itineraire.getHoraireDepart());
        trajet.setId(itineraire.getId());
        trajet.setIdProfilConducteur(itineraire.getIdProfil());
        trajet.setLieuDepart(itineraire.getLieuDepart());
        trajet.setLieuDestination(itineraire.getLieuDestination());
        trajet.setNbrePoint(itineraire.getNbrePoint());
        trajet.setPlaceDispo(itineraire.getPlaceDispo());
        
        Log.i(TrajetActivator.class.getSimpleName(), " Itineraire : " + itineraire);
        Log.i(TrajetActivator.class.getSimpleName(), " Date : " + date);
        */
        setResult(RESULT_OK);
        
        finish();
	}
	
}

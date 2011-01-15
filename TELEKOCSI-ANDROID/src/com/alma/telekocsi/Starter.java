package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class Starter extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        Log.i(getClass().getSimpleName(),"nawak-starter");
        
        //Initialization de la session
        Session session = SessionFactory.getCurrentSession(this);
        Profil profile = session.getActiveProfile();
        
        boolean exist = (profile != null);
        boolean disconnected = (! session.isConnected());
        
        //le compte de la personne n'est pas enregistre
        if (! exist) {
        	Log.d(getClass().getSimpleName(),"le compte de la personne n'est pas enregistre");
        	startActivity(new Intent(this, Identification.class));
        }
        //sinon savoir s'il s'etait deconnecte ou non
        else if (disconnected) {
        	Log.d(getClass().getSimpleName(),"La session est deconnecte : Identification");
        	startActivity(new Intent(this, Identification.class));
        }
        else {
        	//sinon on affiche l'ecran principal
        	Log.d(getClass().getSimpleName(),"La session est deja connecté : Ecran principal");
        	startActivity(new Intent(this, MainMenu.class));
        }
        
        finish();
    }   
}
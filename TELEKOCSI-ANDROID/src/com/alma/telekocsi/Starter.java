package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Starter extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        System.out.println("nawak-starter");

        boolean exist = false;
        boolean disconnected = true;
        //le compte de la personne n'est pas enregistre
        if(!exist){
        	startActivity(new Intent(this, ConnectionParameters.class));
        }
        //sinon savoir s'il s'etait deconnecte ou non
        else if(disconnected){
        	startActivity(new Intent(this, Identification.class));
        }
        else{
        	//sinon on affiche l'ecran principal
        	startActivity(new Intent(this, MainMenu.class));
        }
        
        finish();
    }
    
    
}
package com.alma.telekocsi.init;

import android.util.Log;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

public class DataContext {

	public static Profil profil;
	
	
	public static synchronized Profil getCurrentProfil() {
		if (profil == null) {
			Log.i(DataContext.class.getSimpleName(), "Debut initialisation du profil de connection");
			ProfilDAO profilDAO = new ProfilDAO();
			profil = profilDAO.login("bbelin", "alma");
			Log.i(DataContext.class.getSimpleName(), "profil : "+profil);
			Log.i(DataContext.class.getSimpleName(), "Fin initialisation du profil de connection");
		}
		return profil;
	}
}

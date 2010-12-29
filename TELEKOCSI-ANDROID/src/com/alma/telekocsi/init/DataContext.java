package com.alma.telekocsi.init;

import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.dao.profil.ProfilDAO;

public class DataContext {

	public static Profil profil;
	
	
	public static Profil getCurrentProfil() {
		if (profil == null) {
			ProfilDAO profilDAO = new ProfilDAO();
			profil = profilDAO.login("bbelin", "alma");
		}
		return profil;
	}
}

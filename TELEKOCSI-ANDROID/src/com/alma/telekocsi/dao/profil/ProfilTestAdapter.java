package com.alma.telekocsi.dao.profil;

import java.util.List;

import com.alma.telekocsi.dao.IAdapter;

import android.util.Log;


public class ProfilTestAdapter implements IAdapter<Profil> {

	public static final String TAG_LOG = "PROFIL";

	public ProfilTestAdapter() {
		
	}


	@Override
	public void dismissDialog(int dialogType) {
		Log.i(TAG_LOG, "dismissDialog : " + dialogType);
	}

	@Override
	public Profil remove(Profil fiche) {
		Log.i(TAG_LOG, "remove : " + fiche);
		return fiche;
	}

	@Override
	public void setFiche(Profil fiche) {
		Log.i(TAG_LOG, "setFiche : " + fiche);
	}

	@Override
	public void setList(List<Profil> list) {
		Log.i(TAG_LOG, "setList");
		int cpt = 0;
		for (Profil profil : list) {
			Log.i(TAG_LOG, "Profil : " + (++cpt) + " " + profil);
		}
	}

	@Override
	public void showDialog(int dialogType) {
		Log.i(TAG_LOG, "showDialog : " + dialogType);
	}

	@Override
	public Profil update(Profil fiche) {
		Log.i(TAG_LOG, "update : " + fiche);
		return null;
	}

	@Override
	public Profil add(Profil fiche) {
		Log.i(TAG_LOG, "add : " + fiche);
		return null;
	}


	@Override
	public void clear(String nb) {
		Log.i(TAG_LOG, "clear : " + nb);
	}


	@Override
	public void login(Profil fiche) {
		Log.i(TAG_LOG, "login : " + fiche);	
	}
}

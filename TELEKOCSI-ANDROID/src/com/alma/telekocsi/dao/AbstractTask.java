package com.alma.telekocsi.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;

import android.util.Log;

import com.alma.telekocsi.dao.profil.ProfilDAO;


public abstract class AbstractTask <T> {

	public static Object lock = new Object();
	
	public T execute(final HttpRequestBase request) {
		
		HttpResponse response = null;
		synchronized (lock) {
			try {
				response = GAE.getHttpClient().execute(request);
			} catch (Exception e) {
				Log.e(ProfilDAO.class.getSimpleName(), "Erreur d'appel au serveur", e);
			}
		}
		
		if ((response == null) || !(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
			Log.e(ProfilDAO.class.getSimpleName(), "erreur response incoherente");
		} else {
			try {
				return handleJson(response.getEntity().getContent());
			} catch (IOException e) {
				Log.e(ProfilDAO.class.getSimpleName(), "Erreur de flux", e);
			}
		}
		return null;
	}
	
	protected abstract T handleJson(final InputStream in);
}
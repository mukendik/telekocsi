package com.telekocsi.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Tools {

	
	public static final boolean LOCAL = true;
	public static final String BASE_URL_LOCAL = "http://localhost:8888";
	public static final String BASE_URL_DISTANT = "http://alma-telekocsi.appspot.com";
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final EntityManagerFactory ENTITY_MANAGER = 
		Persistence.createEntityManagerFactory("transactions-optional");
	
	public static EntityManager getEntityManager() {
		return ENTITY_MANAGER.createEntityManager();
	}
	
	
	public static String getURL(String path) {
		if (LOCAL)
			return BASE_URL_LOCAL + path;
		else
			return BASE_URL_DISTANT + path;
	}
	
	
	/**
	 * Recuperation d'un objet Date
	 * @param date au format "jj/MM/aaaa"
	 * @return java.util.Date
	 */
	public static Date getDate(String date) {
	
		Date dateRef = null;
		try {
			dateRef = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return dateRef;
	}
}

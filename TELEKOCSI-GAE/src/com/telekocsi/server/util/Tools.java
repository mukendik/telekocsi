package com.telekocsi.server.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Tools {

	public static final boolean LOCAL = true;
	public static final String BASE_URL_LOCAL = "http://localhost:8888";
	public static final String BASE_URL_DISTANT = "http://alma-telekocsi.appspot.com";
	
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
	
}

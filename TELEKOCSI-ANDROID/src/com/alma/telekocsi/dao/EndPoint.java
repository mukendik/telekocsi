package com.alma.telekocsi.dao;


public class EndPoint {

	public static final boolean LOCAL = false;
	public static final String BASE_URL_LOCAL = "http://localhost:8888";
	public static final String BASE_URL_DISTANT = "http://alma-telekocsi.appspot.com";

	
	public static String getURL(String path) {
		if (LOCAL)
			return BASE_URL_LOCAL + path;
		else
			return BASE_URL_DISTANT + path;
	}
	
	
	public static String getProfilEndPoint() {
		return getURL("/profil");
	}
}

package com.alma.telekocsi.dao;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.google.gson.Gson;


public class GAE {

	private static final boolean LOCAL = false;
	private static final String BASE_URL_LOCAL = "http://10.0.2.2:8888";
	private static final String BASE_URL_DISTANT = "http://alma-telekocsi.appspot.com";

	private static final DefaultHttpClient httpClient = new DefaultHttpClient();
	
	static {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		httpClient.setParams(params); 
	}
	
	private static final Gson gson = new Gson();
	private static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
	private static final String ENCODING_UTF_8 = "UTF-8";	
	
	
	public static DefaultHttpClient getHttpClient() {
		return httpClient;
	}
	
	
	public static Gson getGson() {
		return gson;
	}
	
	
	public static String getContentType() {
		return JSON_CONTENT_TYPE;
	}
	
	
	public static String getEncoding() {
		return ENCODING_UTF_8;
	}
	
	
	public static String getURL(String path) {
		if (LOCAL)
			return BASE_URL_LOCAL + path;
		else
			return BASE_URL_DISTANT + path;
	}
	
	
	public static String getProfilEndPoint() {
		return getURL("/profil");
	}
	
	public static String getItineraireEndPoint() {
		return getURL("/itineraire");
	}
	
	public static String getTrajetEndPoint() {
		return getURL("/trajet");
	}

	public static String getTrajetLigneEndPoint() {
		return getURL("/trajet/ligne");
	}
	
	public static String getTransactionEndPoint() {
		return getURL("/transaction");
	}
}

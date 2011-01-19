package com.alma.telekocsi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alma.telekocsi.dao.profil.Profil;


public class Tools {

	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	
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
	
	
	/**
	 * @param date
	 * @return date formattee sous la forme jj/MM/aaaa
	 */
	public static String getFormattedDate(Date date){
		return sdf.format(date);
	}
	
	
	public static String getName(Profil profil) {
		
		String name;
		StringBuffer result = new StringBuffer();
		if (profil.getPrenom() != null) {
			result.append((profil.getPrenom() + " ").substring(0,1));
		}
		result.append(". " + profil.getNom());
		if (result.length() > 10) {
			name = result.substring(0, 10);
		} else {
			name = result.toString();
		}
		return name;
	}
}

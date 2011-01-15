package com.alma.telekocsi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
}

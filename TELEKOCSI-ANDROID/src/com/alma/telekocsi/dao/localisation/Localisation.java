package com.alma.telekocsi.dao.localisation;


public class Localisation {

	private String id;
	
	private String idProfil;
	private String pointGPS;
	private double longitude;
	private double latitude;	
	private String dateLocalisation;
	private String heureLocalisation;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getIdProfil() {
		return idProfil;
	}

	public void setIdProfil(String idProfil) {
		this.idProfil = idProfil;
	}

	public String getPointGPS() {
		return pointGPS;
	}

	public void setPointGPS(String pointGPS) {
		this.pointGPS = pointGPS;
	}

	public String getDateLocalisation() {
		return dateLocalisation;
	}

	public void setDateLocalisation(String dateLocalisation) {
		this.dateLocalisation = dateLocalisation;
	}

	public String getHeureLocalisation() {
		return heureLocalisation;
	}

	public void setHeureLocalisation(String heureLocalisation) {
		this.heureLocalisation = heureLocalisation;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}	
	
	@Override
	public boolean equals(Object o) {
		return o != null 
				&& (o instanceof Localisation) 
				&& this.id != null 
				&& this.id.equals(((Localisation) o).getId());
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; id profil : " + getIdProfil());
		stringBuilder.append("; point gps : " + getPointGPS());
		stringBuilder.append("; longitude : " + getLongitude());
		stringBuilder.append("; latitude : " + getLatitude());
		stringBuilder.append("; Date : " + getDateLocalisation());
		stringBuilder.append("; Heure : " + getHeureLocalisation());
		return stringBuilder.toString();
	}
}

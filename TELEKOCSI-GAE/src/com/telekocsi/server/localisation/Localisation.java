package com.telekocsi.server.localisation;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.datanucleus.jpa.annotations.Extension;


@Entity
@XmlRootElement
public class Localisation implements Serializable {

	/**
	 * 1 : Version initiale
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	
	private String idProfil;
	private String pointGPS;
	private long longitude;
	private long latitude;
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
	
	public long getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
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

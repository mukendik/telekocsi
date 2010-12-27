package com.telekocsi.server.event;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.datanucleus.jpa.annotations.Extension;


@Entity
@XmlRootElement
public class Event implements Serializable {

	/**
	 * 1 : Version initiale
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	
	private String idProfilFrom;
	private String idProfilTo;
	private int typeEvent;
	private String dateEvent;
	private String heureEvent;
	private String description;
	private String etat;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getIdProfilFrom() {
		return idProfilFrom;
	}

	public void setIdProfilFrom(String idProfilFrom) {
		this.idProfilFrom = idProfilFrom;
	}

	public String getIdProfilTo() {
		return idProfilTo;
	}

	public void setIdProfilTo(String idProfilTo) {
		this.idProfilTo = idProfilTo;
	}

	public int getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(int typeEvent) {
		this.typeEvent = typeEvent;
	}

	public String getDateEvent() {
		return dateEvent;
	}

	public void setDateEvent(String dateEvent) {
		this.dateEvent = dateEvent;
	}

	public String getHeureEvent() {
		return heureEvent;
	}

	public void setHeureEvent(String heureEvent) {
		this.heureEvent = heureEvent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}	
	
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; id profil from: " + getIdProfilFrom());
		stringBuilder.append("; id profil to : " + getIdProfilTo());
		stringBuilder.append("; Type : " + getTypeEvent());
		stringBuilder.append("; description : " + getDescription());
		stringBuilder.append("; Date : " + getDateEvent());
		stringBuilder.append("; Heure : " + getHeureEvent());
		stringBuilder.append("; Etat : " + getEtat());
		
		return stringBuilder.toString();
	}	
}

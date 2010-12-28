package com.alma.telekocsi.dao.event;


public class Event {

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
	public boolean equals(Object o) {
		return o != null 
				&& (o instanceof Event) 
				&& this.id != null 
				&& this.id.equals(((Event) o).getId());
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

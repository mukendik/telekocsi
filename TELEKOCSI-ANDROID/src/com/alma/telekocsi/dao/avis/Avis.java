package com.alma.telekocsi.dao.avis;


public class Avis  {

	private String id;
	
	private String idProfilFrom;
	private String idProfilTo;
	private int classement;
	private String commentaire;
	private String dateAvis;
	private String heureAvis;
	private boolean checked;
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

	public int getClassement() {
		return classement;
	}

	public void setClassement(int classement) {
		this.classement = classement;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getDateAvis() {
		return dateAvis;
	}

	public void setDateAvis(String dateAvis) {
		this.dateAvis = dateAvis;
	}

	public String getHeureAvis() {
		return heureAvis;
	}

	public void setHeureAvis(String heureAvis) {
		this.heureAvis = heureAvis;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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
				&& (o instanceof Avis) 
				&& this.id != null 
				&& this.id.equals(((Avis) o).getId());
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; id profil from: " + getIdProfilFrom());
		stringBuilder.append("; id profil to : " + getIdProfilTo());
		stringBuilder.append("; Classement : " + getClassement());
		stringBuilder.append("; commentaire : " + getCommentaire());
		stringBuilder.append("; Date : " + getDateAvis());
		stringBuilder.append("; Heure : " + getHeureAvis());
		stringBuilder.append("; isChecked : " + isChecked());
		stringBuilder.append("; Etat : " + getEtat());
		
		return stringBuilder.toString();
	}	
}

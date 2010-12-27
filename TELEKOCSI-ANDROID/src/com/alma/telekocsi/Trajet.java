package com.alma.telekocsi;

public class Trajet {

	String villeDepart;
	String villeArrivee;
	String heure;
	String heureRange;
	String jours;
	boolean autoroute;
	String commentaire;
	boolean actif;
	
	
	public Trajet() {
	}
	
	
	public String getVilleDepart() {
		return villeDepart;
	}


	public void setVilleDepart(String villeDepart) {
		this.villeDepart = villeDepart;
	}


	public String getVilleArrivee() {
		return villeArrivee;
	}


	public void setVilleArrivee(String villeArrivee) {
		this.villeArrivee = villeArrivee;
	}


	public String getHeure() {
		return heure;
	}


	public void setHeure(String heure) {
		this.heure = heure;
	}


	public String getHeureRange() {
		return heureRange;
	}


	public void setHeureRange(String heureRange) {
		this.heureRange = heureRange;
	}


	public String getJours() {
		return jours;
	}


	public void setJours(String jours) {
		this.jours = jours;
	}


	public boolean isAutoroute() {
		return autoroute;
	}


	public void setAutoroute(boolean autoroute) {
		this.autoroute = autoroute;
	}


	public String getCommentaire() {
		return commentaire;
	}


	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}


	public boolean isActif() {
		return actif;
	}


	public void setActif(boolean actif) {
		this.actif = actif;
	}
}

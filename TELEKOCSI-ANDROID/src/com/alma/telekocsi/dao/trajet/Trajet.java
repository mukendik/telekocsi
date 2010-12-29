package com.alma.telekocsi.dao.trajet;


/**
 * @author ALMA
 * 
 * Contrairement a l'itineraire, le trajet represente l'action
 * liee a l'itineraire (pour une date precise, ....)
 *
 */
public class Trajet {

	private String id;
	
	private String lieuDepart;
	private String lieuDestination;
	private int placeDispo;
	private String horaireDepart;
	private String variableDepart;
	private String horaireArrivee;
	private boolean autoroute;
	private String frequenceTrajet;
	private int nbrePoint;
	private String commentaire;

	private String idItineraire;
	private String idProfilConducteur;
	private String dateTrajet;
	private int soldePlaceDispo;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	

	public String getLieuDepart() {
		return lieuDepart;
	}

	public void setLieuDepart(String lieuDepart) {
		this.lieuDepart = lieuDepart;
	}

	public String getLieuDestination() {
		return lieuDestination;
	}

	public void setLieuDestination(String lieuDestination) {
		this.lieuDestination = lieuDestination;
	}

	public int getPlaceDispo() {
		return placeDispo;
	}

	public void setPlaceDispo(int placeDispo) {
		this.placeDispo = placeDispo;
	}

	public String getHoraireDepart() {
		return horaireDepart;
	}

	public void setHoraireDepart(String horaireDepart) {
		this.horaireDepart = horaireDepart;
	}

	public String getVariableDepart() {
		return variableDepart;
	}

	public void setVariableDepart(String variableDepart) {
		this.variableDepart = variableDepart;
	}

	public String getHoraireArrivee() {
		return horaireArrivee;
	}

	public void setHoraireArrivee(String horaireArrivee) {
		this.horaireArrivee = horaireArrivee;
	}

	public boolean isAutoroute() {
		return autoroute;
	}

	public void setAutoroute(boolean autoroute) {
		this.autoroute = autoroute;
	}

	public String getFrequenceTrajet() {
		return frequenceTrajet;
	}

	public void setFrequenceTrajet(String frequenceTrajet) {
		this.frequenceTrajet = frequenceTrajet;
	}

	public int getNbrePoint() {
		return nbrePoint;
	}

	public void setNbrePoint(int nbrePoint) {
		this.nbrePoint = nbrePoint;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}


	public String getIdItineraire() {
		return idItineraire;
	}

	public void setIdItineraire(String idItineraire) {
		this.idItineraire = idItineraire;
	}

	public String getIdProfilConducteur() {
		return idProfilConducteur;
	}

	public void setIdProfilConducteur(String idProfilConducteur) {
		this.idProfilConducteur = idProfilConducteur;
	}

	public String getDateTrajet() {
		return dateTrajet;
	}

	public void setDateTrajet(String dateTrajet) {
		this.dateTrajet = dateTrajet;
	}

	public int getSoldePlaceDispo() {
		return soldePlaceDispo;
	}

	public void setSoldePlaceDispo(int soldePlaceDispo) {
		this.soldePlaceDispo = soldePlaceDispo;
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null 
				&& (o instanceof Trajet) 
				&& this.id != null 
				&& this.id.equals(((Trajet) o).getId());
	}

	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getLieuDepart());
		stringBuilder.append(" -> ");
		stringBuilder.append(getLieuDestination());
		
		return stringBuilder.toString();
	}
	
	
	public String toStringDetail() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; Commentaire : " + getCommentaire());
		stringBuilder.append("; Frequence trajet : " + getFrequenceTrajet());
		stringBuilder.append("; Horaire arrivée : " + getHoraireArrivee());
		stringBuilder.append("; Horaire départ : " + getHoraireDepart());
		stringBuilder.append("; Lieu départ : " + getLieuDepart());
		stringBuilder.append("; Lieu destination : " + getLieuDestination());
		stringBuilder.append("; Nombre de points : " + getNbrePoint());
		stringBuilder.append("; Place disponibles : " + getPlaceDispo());
		stringBuilder.append("; Variable de départ : " + getVariableDepart());
		stringBuilder.append("; Autoroute : " + isAutoroute());
		stringBuilder.append("; Profil conducteur : " + getIdProfilConducteur());
		stringBuilder.append("; Id Itinéraire : " + getIdItineraire());
		stringBuilder.append("; Date trajet : " + getDateTrajet());
		stringBuilder.append("; Place restantes : " + getSoldePlaceDispo());
		
		return stringBuilder.toString();
	}	
	
}

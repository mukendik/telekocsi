package com.alma.telekocsi.dao.transaction;


public class Transaction  {

	private String id;
	
	private String idTrajetLigne;
	private String idProfilConducteur;
	private String idProfilPassager;
	private String dateTransaction;
	private String heureTransaction;
	private int pointEchange;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getIdProfilConducteur() {
		return idProfilConducteur;
	}

	public void setIdProfilConducteur(String idProfilConducteur) {
		this.idProfilConducteur = idProfilConducteur;
	}

	public String getIdProfilPassager() {
		return idProfilPassager;
	}

	public void setIdProfilPassager(String idProfilPassager) {
		this.idProfilPassager = idProfilPassager;
	}

	public String getDateTransaction() {
		return dateTransaction;
	}

	public void setDateTransaction(String dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	public String getHeureTransaction() {
		return heureTransaction;
	}

	public void setHeureTransaction(String heureTransaction) {
		this.heureTransaction = heureTransaction;
	}

	public int getPointEchange() {
		return pointEchange;
	}

	public void setPointEchange(int pointEchange) {
		this.pointEchange = pointEchange;
	}

	public String getIdTrajetLigne() {
		return idTrajetLigne;
	}

	public void setIdTrajetLigne(String idTrajetLigne) {
		this.idTrajetLigne = idTrajetLigne;
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null 
				&& (o instanceof Transaction) 
				&& this.id != null 
				&& this.id.equals(((Transaction) o).getId());
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; id trajet ligne : " + getIdTrajetLigne());
		stringBuilder.append("; id profil conducteur : " + getIdProfilConducteur());
		stringBuilder.append("; id profil passager : " + getIdProfilPassager());
		stringBuilder.append("; date transactin : " + getDateTransaction());
		stringBuilder.append("; heure transaction : " + getHeureTransaction());
		stringBuilder.append("; Nbre points echanges : " + getPointEchange());
		
		return stringBuilder.toString();
	}	
}

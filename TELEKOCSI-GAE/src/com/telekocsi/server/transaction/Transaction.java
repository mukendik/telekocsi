package com.telekocsi.server.transaction;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.datanucleus.jpa.annotations.Extension;


@Entity
@XmlRootElement
public class Transaction implements Serializable {

	/**
	 * 1 : Version initiale
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
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

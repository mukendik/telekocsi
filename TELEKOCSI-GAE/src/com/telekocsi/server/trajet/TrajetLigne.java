package com.telekocsi.server.trajet;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.datanucleus.jpa.annotations.Extension;


@Entity
@XmlRootElement
public class TrajetLigne implements Serializable {

	/**
	 * 1 : Version initiale
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	
	private String idTrajet;
	private String idProfilPassager;
	private int nbrePoint;
	private int placeOccupee;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getIdTrajet() {
		return idTrajet;
	}

	public void setIdTrajet(String idTrajet) {
		this.idTrajet = idTrajet;
	}

	public String getIdProfilPassager() {
		return idProfilPassager;
	}

	public void setIdProfilPassager(String idProfilPassager) {
		this.idProfilPassager = idProfilPassager;
	}

	public int getNbrePoint() {
		return nbrePoint;
	}

	public void setNbrePoint(int nbrePoint) {
		this.nbrePoint = nbrePoint;
	}

	public int getPlaceOccupee() {
		return placeOccupee;
	}

	public void setPlaceOccupee(int placeOccupee) {
		this.placeOccupee = placeOccupee;
	}	

	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; idProfilPassager : " + getIdProfilPassager());
		stringBuilder.append("; idTrajet : " + getIdTrajet());
		stringBuilder.append("; Nbre de points : " + getNbrePoint());
		stringBuilder.append("; Nbre de places occupees : " + getPlaceOccupee());
		
		return stringBuilder.toString();
	}
}

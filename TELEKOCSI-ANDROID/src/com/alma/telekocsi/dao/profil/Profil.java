package com.alma.telekocsi.dao.profil;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Profil implements Serializable {

	private String id;
	
	private String nom;
	private String prenom;
	private String pseudo;
	
	/* Homme ou Femme */
	private String sexe;
	
	private String dateNaissance;
	private String motDePasse;
	private String email;
	private String telephone;
	private String pathPhoto;
	
	/* Classement moyen de 0 à 5 */
	private int classementMoyen;
	
	private int pointsDispo;
	private boolean connecte;
	
	/* Conducteur ou Passager pour la session en cours */
	private String typeProfil;
	
	/* Classe de véhicule : de 1 à 3 */
	private int classeVehicule;
	
	private int nombreAvis;
	
	/* Modele du véhicule */
	private String vehicule;
	
	private String fumeur;
	private String animaux;
	private String detours;
	private String musique;
	private String discussion;
	
	/* Conducteur ou Passager (valeur a proposer par defaut au login) */
	private String typeProfilHabituel;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPathPhoto() {
		return pathPhoto;
	}

	public void setPathPhoto(String pathPhoto) {
		this.pathPhoto = pathPhoto;
	}

	public int getClassementMoyen() {
		return classementMoyen;
	}

	public void setClassementMoyen(int classementMoyen) {
		this.classementMoyen = classementMoyen;
	}
	
	public int getNombreAvis() {
		return nombreAvis;
	}

	public void setNombreAvis(int nombreAvis) {
		this.nombreAvis = nombreAvis;
	}
	
	public int getPointsDispo() {
		return pointsDispo;
	}

	public void setPointsDispo(int pointsDispo) {
		this.pointsDispo = pointsDispo;
	}

	public boolean isConnecte() {
		return connecte;
	}

	public void setConnecte(boolean connecte) {
		this.connecte = connecte;
	}

	public String getTypeProfil() {
		return typeProfil;
	}

	public void setTypeProfil(String typeProfil) {
		this.typeProfil = typeProfil;
	}

	public int getClasseVehicule() {
		return classeVehicule;
	}

	public void setClasseVehicule(int classeVehicule) {
		this.classeVehicule = classeVehicule;
	}

	public String getVehicule() {
		return vehicule;
	}

	public void setVehicule(String vehicule) {
		this.vehicule = vehicule;
	}

	public String getFumeur() {
		return fumeur;
	}

	public void setFumeur(String fumeur) {
		this.fumeur = fumeur;
	}

	public String getAnimaux() {
		return animaux;
	}

	public void setAnimaux(String animaux) {
		this.animaux = animaux;
	}

	public String getDetours() {
		return detours;
	}

	public void setDetours(String detours) {
		this.detours = detours;
	}

	public String getMusique() {
		return musique;
	}

	public void setMusique(String musique) {
		this.musique = musique;
	}

	public String getDiscussion() {
		return discussion;
	}

	public void setDiscussion(String discussion) {
		this.discussion = discussion;
	}

	public String getTypeProfilHabituel() {
		return typeProfilHabituel;
	}

	public void setTypeProfilHabituel(String typeProfilHabituel) {
		this.typeProfilHabituel = typeProfilHabituel;
	}	
	
	@Override
	public boolean equals(Object o) {
		return o != null 
				&& (o instanceof Profil) 
				&& this.id != null 
				&& this.id.equals(((Profil) o).getId());
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Id : " + getId());
		stringBuilder.append("; Nom : " + getNom());
		stringBuilder.append("; Prenom : " + getPrenom());
		stringBuilder.append("; Pseudo : " + getPseudo());
		stringBuilder.append("; Date naissance : " + getDateNaissance());
		stringBuilder.append("; Mot de passe : " + getMotDePasse());
		stringBuilder.append("; Sexe : " + getSexe());
		stringBuilder.append("; Telephone : " + getTelephone());
		stringBuilder.append("; Animaux : " + getAnimaux());
		stringBuilder.append("; Classement moyen : " + getClassementMoyen());
		stringBuilder.append("; Classement véhicule : " + getClasseVehicule());
		stringBuilder.append("; Detours : " + getDetours());
		stringBuilder.append("; Discution : " + getDiscussion());
		stringBuilder.append("; Fumeur : " + getFumeur());
		stringBuilder.append("; Musique : " + getMusique());
		stringBuilder.append("; Photo : " + getPathPhoto());
		stringBuilder.append("; Points : " + getPointsDispo());
		stringBuilder.append("; Type profil actuel : " + getTypeProfil());
		stringBuilder.append("; Type profil habituel : " + getTypeProfilHabituel());
		stringBuilder.append("; Véhicule : " + getVehicule());
		stringBuilder.append("; email : " + getEmail());
		stringBuilder.append("; connecte : " + isConnecte());
		
		return stringBuilder.toString();
	}
	
}

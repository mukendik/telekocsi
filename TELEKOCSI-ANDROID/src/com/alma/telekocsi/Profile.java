package com.alma.telekocsi;

import java.util.Date;

public class Profile {

	private static final String O = "Oui";
	private static final String N = "Non";
	public static final String SI = "Sans importance";
	private static final String H = "Homme";
	private static final String F = "Femme";

	private String name;
	private String firstName;
	private String email;
	private String password;
	private Date birth;
	private String phone;
	private char sex;


	public static String getStringVal(String str) {
		if(str!=null){
			if(str.equals("O")){
				return O;
			}else if(str.equals("N")){
				return N;
			}else if(str.equals("SI")){
				return SI;
			}else if(str.equals("H")){
				return H;
			}else if(str.equals("F")){
				return F;
			}
		}
		return null;
	}
	public static String getBdVal(String str) {
		if(str!=null){
			if(str.equals(O)){
				return "O";
			}else if(str.equals(N)){
				return "N";
			}else if(str.equals(SI)){
				return "SI";
			}else if(str.equals(H)){
				return "H";
			}else if(str.equals(F)){
				return "F";
			}
		}

		return null;
	}
	
	public static int getImageResource(String sexe) {
		if (sexe.equals("H")) {
			return R.drawable.man;
		} else {
			return R.drawable.woman;
		}
	}

	public static int getClassementStarImageResource(int classement) {
		switch (classement) {
		case 0:
			return R.drawable.zerostarjaune;
		case 1:
			return R.drawable.onestarjaune;
		case 2:
			return R.drawable.twostarjaune;
		case 3:
			return R.drawable.treestarjaune;
		case 4:
			return R.drawable.fourstarjaune;
		case 5:
			return R.drawable.fivestarjaune;
		default:
			return R.drawable.fivestarjaune;
		}
	}
	

	public void setName(String name){
		this.name = name;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public void setBirth(Date birth){
		this.birth = birth;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public void setSex(char sex){
		this.sex = sex;
	}

	public String getName(){
		return name;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getEmail(){
		return email;
	}

	public String getPassword(){
		return password;
	}

	public Date getBirth(){
		return birth;
	}

	public String getPhone(){
		return phone;
	}

	public char getSex(){
		return sex;
	}

}

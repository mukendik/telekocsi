package com.alma.telekocsi;

import java.util.Date;

public class Profile {
	
	private String name;
	private String firstName;
	private String email;
	private String password;
	private Date birth;
	private String phone;
	private char sex;
	
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

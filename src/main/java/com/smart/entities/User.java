package com.smart.entities;

import java.util.*;
import javax.persistence.CascadeType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;



@Entity
@Table(name="USER")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotBlank(message ="Name field is required")
	@Size(min=2,max=20,message="min 2 and max=20 characters are allowed")
	private String name;
	@NotBlank(message="password field is required")
    private String password;
	private String role;
	@Column(unique=true)
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	private String imageUrl;
	private boolean enabled;
	private String about;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="user",orphanRemoval=true)
	private List<Contact> contacts =new ArrayList<>();
	public int getId() {
		return id;
	}
	//Getter setter method
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	//toString method
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", role=" + role + ", email=" + email
				+ ", imageUrl=" + imageUrl + ", enabled=" + enabled + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	// Parameterised constructor 
	
	public User(int id, String name, String password, String role, String email, String imageUrl, boolean enabled,
			String about, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role;
		this.email = email;
		this.imageUrl = imageUrl;
		this.enabled = enabled;
		this.about = about;
		this.contacts = contacts;
	}
	
	
	//Default constructor
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	

}

package com.smart.entities;

import javax.persistence.*;


@Entity
@Table(name="CONTACT")
public class Contact {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	private String secondName;
	private String work;
	private String email;
	private String phone;
	private String image;
    @Column(length=150)
	private String description;

	@ManyToOne
	private User user;

	//Generate Getter Setter method 
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	// Generator parameterised constructor 

	public Contact(int id, String name, String secondName, String work, String email, String phone, String image,
			String description, User user) {
		super();
		this.id = id;
		this.name = name;
		this.secondName = secondName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
		this.user = user;
	}
    
	//Generate default constructor   
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
     
	//Generate toString method
   /* @Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user
				+ "]";
	}*/
	@Override
	public boolean equals(Object obj) {
		return this.id==((Contact)obj).getId();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

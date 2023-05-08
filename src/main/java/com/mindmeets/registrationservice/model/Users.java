package com.mindmeets.registrationservice.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

	@Id
	private String username;
	private String password;
	private boolean enabled;
	
	@ManyToMany
	@JoinTable(
			  name = "authorities", 
			  joinColumns = @JoinColumn(name = "username"), 
			  inverseJoinColumns = @JoinColumn(name = "authority"))
	private List<Roles> authorities;

	public List<Roles> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Roles> authorities) {
		this.authorities = authorities;
	}

	public Users() {}

	public Users(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


}
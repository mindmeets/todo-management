package com.mindmeets.todomanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "users")
public class User {

	@Id
	private String username;
	@JsonIgnore
	private String password;
	private boolean enabled;

	public User() {
	}
	
	public User(String username, boolean enabled) {
		this.username = username;
		this.enabled = enabled;
	}

	public User(String username, String password, boolean enabled) {
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

	@Override
	public String toString() {
		return "User [username=" + username + ", enabled=" + enabled + "]";
	}

}

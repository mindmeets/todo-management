package com.mindmeets.registrationservice.dto;

import java.util.List;

public class UserRequest {

	private String username;
	private String password;
	private List<String> roles;
	
	public UserRequest() {
	}

	public UserRequest(String username, String password, List<String> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserRequest [username=" + username + ", password=[Protected], roles=" + roles + "]";
	}
}

package com.mindmeets.registrationservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mindmeets.registrationservice.dto.UserRequest;
import com.mindmeets.registrationservice.model.Roles;
import com.mindmeets.registrationservice.model.Users;
import com.mindmeets.registrationservice.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	public void registerUser(UserRequest userRequest) {
		Users theUser = new Users(userRequest.getUsername(),
				passwordEncoder.encode(userRequest.getPassword()), true);
		var authorities = userRequest.getRoles().stream()
				.map(role -> {
					var authority = new Roles(role);
					authority.setUsers(List.of(theUser));
					return authority;
				})
				.collect(Collectors.toList());
		theUser.setAuthorities(authorities);
		
		userRepo.save(theUser);
		
	}

}

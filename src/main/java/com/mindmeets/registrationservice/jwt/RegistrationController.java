package com.mindmeets.registrationservice.jwt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindmeets.registrationservice.dto.UserRequest;
import com.mindmeets.registrationservice.service.UserService;

@RestController
public class RegistrationController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;

	@PostMapping("signup")
	public void registerUser(@RequestBody UserRequest userRequest){
		log.info("Registering user {}", userRequest);
		userService.registerUser(userRequest);
	}
}


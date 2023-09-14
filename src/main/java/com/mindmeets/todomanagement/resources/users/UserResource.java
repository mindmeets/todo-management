package com.mindmeets.todomanagement.resources.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindmeets.todomanagement.dto.PasswordDto;
import com.mindmeets.todomanagement.model.User;
import com.mindmeets.todomanagement.repo.TodoJpaRepo;
import com.mindmeets.todomanagement.repo.UserRepo;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserResource {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private TodoJpaRepo todoRepo;

	@Autowired
	private JdbcUserDetailsManager userDetailsManager;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<User> getUser(@PathVariable String username) {
		if (!userDetailsManager.userExists(username)) {
			return ResponseEntity.notFound().build();
		}
		UserDetails user = userDetailsManager.loadUserByUsername(username);
		User fetchedUser = new User(user.getUsername(), user.isEnabled());
		return ResponseEntity.ok(fetchedUser);
	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		userDetailsManager.deleteUser(username);
		todoRepo.deleteAllByUsername(username);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
		if (!userDetailsManager.userExists(username)) {
			return ResponseEntity.notFound().build();
		}
		if (!username.equals(user.getUsername()) && userDetailsManager.userExists(user.getUsername())) {
			return ResponseEntity.badRequest().build();
		}
		UserDetails userToBeUpdated = userDetailsManager.loadUserByUsername(username);
		userDetailsManager.updateUser(org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername()).password(userToBeUpdated.getPassword())
				.authorities(userToBeUpdated.getAuthorities()).disabled(!user.isEnabled()).build());

		UserDetails updatedUserDetails = userDetailsManager.loadUserByUsername(user.getUsername());

		User updatedUser = new User(updatedUserDetails.getUsername(), updatedUserDetails.isEnabled());

		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		if (userDetailsManager.userExists(user.getUsername())) {
			return ResponseEntity.badRequest().build();
		}
		UserDetails userToBeCreated = org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername()).password("dummy").passwordEncoder(pwd -> passwordEncoder.encode(pwd))
				.roles("USER").disabled(!user.isEnabled()).build();
		userDetailsManager.createUser(userToBeCreated);

		UserDetails createdUserDetails = userDetailsManager.loadUserByUsername(user.getUsername());

		User updatedUser = new User(createdUserDetails.getUsername(), createdUserDetails.isEnabled());

		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}

	@PostMapping("/users/{username}")
	public ResponseEntity<Void> updatePassword(@PathVariable String username, @RequestBody PasswordDto passwordDto) {
		if (!userDetailsManager.userExists(username)) {
			return ResponseEntity.notFound().build();
		}

		userDetailsManager.loadUserByUsername(username);
		userDetailsManager.changePassword(passwordDto.getOldPassword(),
				passwordEncoder.encode(passwordDto.getNewPassword()));

		return ResponseEntity.ok().build();
	}

}

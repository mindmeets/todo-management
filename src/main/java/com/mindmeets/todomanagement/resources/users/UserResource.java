package com.mindmeets.todomanagement.resources.users;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.GrantedAuthority;
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
	
	private boolean hasAdminRole(Collection<? extends GrantedAuthority> authorities) {
		List<String> roles = authorities.stream().map(entry -> entry.getAuthority()).collect(Collectors.toList());
		return roles.contains("ROLE_ADMIN");
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUsers() {
		List<User> users = userRepo.findAll();
		users = users.stream().map(user -> {
			UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getUsername());
			user.setAdmin(hasAdminRole(userDetails.getAuthorities()));
			return user;
		}).collect(Collectors.toList());
		return users;
	}

	@GetMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> getUser(@PathVariable String username) {
		if (!userDetailsManager.userExists(username)) {
			return ResponseEntity.notFound().build();
		}
		UserDetails user = userDetailsManager.loadUserByUsername(username);
		User fetchedUser = new User(user.getUsername(), user.isEnabled());
		fetchedUser.setAdmin(hasAdminRole(user.getAuthorities()));
		
		return ResponseEntity.ok(fetchedUser);
	}

	@DeleteMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		userDetailsManager.deleteUser(username);
		todoRepo.deleteAllByUsername(username);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
		if (!userDetailsManager.userExists(username)) {
			return ResponseEntity.notFound().build();
		}
		if (!username.equals(user.getUsername()) && userDetailsManager.userExists(user.getUsername())) {
			return ResponseEntity.badRequest().build();
		}
		UserDetails userToBeUpdated = userDetailsManager.loadUserByUsername(username);
		UserBuilder userBuilder =  getUserBuilder(
				user.getUsername(),
				userToBeUpdated.getPassword(),
				user.isEnabled(),
				user.isAdmin()
			);
//		org.springframework.security.core.userdetails.User
//				.withUsername(user.getUsername()).password(userToBeUpdated.getPassword())
//				.disabled(!user.isEnabled());
//		if (user.isAdmin()) {
//			userBuilder = userBuilder.roles("ADMIN", "USER");
//		} else {
//			userBuilder = userBuilder.authorities(userToBeUpdated.getAuthorities());
//		}
		userDetailsManager.updateUser(userBuilder.build());

		UserDetails updatedUserDetails = userDetailsManager.loadUserByUsername(user.getUsername());

		User updatedUser = new User(updatedUserDetails.getUsername(), updatedUserDetails.isEnabled());
		updatedUser.setAdmin(hasAdminRole(updatedUserDetails.getAuthorities()));
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}

	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		if (userDetailsManager.userExists(user.getUsername())) {
			return ResponseEntity.badRequest().build();
		}
		UserBuilder userBuilder =  getUserBuilder(
				user.getUsername(),
				"dummy",
				user.isEnabled(),
				user.isAdmin()
			);
		userBuilder = userBuilder.passwordEncoder(pwd -> passwordEncoder.encode(pwd));
		
//		UserDetails userToBeCreated = org.springframework.security.core.userdetails.User
//				.withUsername(user.getUsername()).password("dummy").passwordEncoder(pwd -> passwordEncoder.encode(pwd))
//				.roles("USER").disabled(!user.isEnabled()).build();
		
		userDetailsManager.createUser(userBuilder.build());

		UserDetails createdUserDetails = userDetailsManager.loadUserByUsername(user.getUsername());

		User createdUser = new User(createdUserDetails.getUsername(), createdUserDetails.isEnabled());
		createdUser.setAdmin(hasAdminRole(createdUserDetails.getAuthorities()));
		return new ResponseEntity<User>(createdUser, HttpStatus.OK);
	}
	
	private UserBuilder getUserBuilder(String username, String password, boolean enabled, boolean admin) {
		UserBuilder userBuilder = org.springframework.security.core.userdetails.User
				.withUsername(username).password(password)
				.disabled(!enabled);
		if (admin) {
			userBuilder = userBuilder.roles("ADMIN", "USER");
		} else {
			userBuilder = userBuilder.roles("USER");
		}
		
		return userBuilder;
	}

	@PostMapping("/users/{username}")
	@PreAuthorize("#username == authentication.name")
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

package com.mindmeets.registrationservice.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoResource {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final List<Todo> TODOS_LIST = List.of(new Todo("user", "Learn AWS"),
			new Todo("user", "Get AWS Certified"));

	@GetMapping("/todos")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Todo> retriveAllTodos() {
		return TODOS_LIST;
	}

//	 and #username == authentication.name
	@GetMapping("/users/{username}/todos")
	@PreAuthorize("hasRole('USER')")
	public Todo retriveTodosForSpecificUser(@PathVariable String username) {
		return TODOS_LIST.get(0);
	}

	@PostMapping("/users/{username}/todos")
	public void createTodosForSpecificUser(@PathVariable String username, @RequestBody Todo todo) {
		logger.info("Create {} for {}", todo, username);
	}
}

record Todo(String username, String description) {
}
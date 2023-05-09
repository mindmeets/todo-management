package com.mindmeets.todomanagement.resources.todos;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mindmeets.todomanagement.model.Todo;
import com.mindmeets.todomanagement.service.TodoService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('USER') and #username == authentication.name")
public class TodoResource {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/{username}/todos")
	public List<Todo> getAllTodos(@PathVariable String username) {
		log.info("Retriving all todos!!");
		return todoService.findAll();
	}
	
	@GetMapping("/{username}/todos/{id}")
	public Todo getTodo(@PathVariable String username, @PathVariable Long id) {
		return todoService.findById(id);
	}
	
	@DeleteMapping("/{username}/todos/{id}")
	public ResponseEntity<Void> deleteTodo(@PathVariable String username, @PathVariable Long id) {
		var todo = todoService.deleteById(id);
		
		if (todo != null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{username}/todos/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable String username, @PathVariable Long id, @RequestBody Todo todo) {
		todo.setId(id);
		Todo updatedTodo = todoService.save(todo);
		
		return new ResponseEntity<Todo>(updatedTodo, HttpStatus.OK);
	}
	
	@PostMapping("/{username}/todos")
	public ResponseEntity<Todo> createTodo(@PathVariable String username, @RequestBody Todo todo) {
		log.info("Todo id: {}", todo.getId());
		todo.setId(-1L);
		Todo createdTodo = todoService.save(todo);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}

}

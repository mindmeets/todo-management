package com.mindmeets.todomanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mindmeets.todomanagement.model.Todo;

@Service
public class TodoService {
	
	private static List<Todo> todos = new ArrayList<>();
	private static Long idCount = 0L;
	
	static {
		todos.add(new Todo(++idCount, "user", "Get AWS certified", new Date(), false));
		todos.add(new Todo(++idCount, "user", "Learn React", new Date(), false));
		todos.add(new Todo(++idCount, "user", "Workout in morning", new Date(), false));
	}
	
	public List<Todo> findAll() {
		return todos.stream().sorted().toList();
	}
	
	public Todo deleteById(Long id) {
		Todo todo = findById(id);
		if (todo != null)
			todos.remove(todo);
		return todo;
	}

	public Todo findById(Long id) {
		return todos.stream().filter(entry -> entry.getId() == id).findFirst().orElse(null);
	}
	
	public Todo save(Todo todo) {
		if (todo.getId() == -1) {
			todo.setId(++idCount);
			todos.add(todo);
		} else {
			deleteById(todo.getId());
			todos.add(todo);
		}
		return todo;
	}

}

package com.mindmeets.todomanagement.resources.play;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindmeets.todomanagement.model.HelloWorldBean;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HelloWorldResource {

	@GetMapping("/hello-world")
	public HelloWorldBean helloWorld() {
//		throw new RuntimeException("Some Error has happened!");
		return new HelloWorldBean("Hello World!");
	}
}

package com.mindmeets.todomanagement.model;

import java.util.Date;
import java.util.Objects;

public class Todo implements Comparable<Todo>{
	private Long id;
	private String username;
	private String description;
	private Date targetDate;
	private boolean done;

	public Todo(Long id, String username, String description, Date targetDate, boolean done) {
		this.id = id;
		this.username = username;
		this.description = description;
		this.targetDate = targetDate;
		this.done = done;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Todo other = (Todo) obj;
		return Objects.equals(id, other.id);
	}

	public Todo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public int compareTo(Todo todo) {
		if (this.id > todo.getId())
			return 1;
		else if (this.id < todo.getId())
			return -1;
		else
			return 0;
	}

}

package com.mindmeets.todomanagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindmeets.todomanagement.model.Todo;

import jakarta.transaction.Transactional;

@Repository
public interface TodoJpaRepo extends JpaRepository<Todo, Long>{

	List<Todo> findByUsername(String username);

	@Transactional
	void deleteAllByUsername(String username);
}

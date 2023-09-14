package com.mindmeets.todomanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindmeets.todomanagement.model.User;

public interface UserRepo extends JpaRepository<User, String> {

}

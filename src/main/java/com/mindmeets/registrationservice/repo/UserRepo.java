package com.mindmeets.registrationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindmeets.registrationservice.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, String>{

}

package com.daniellecaricati.personalblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniellecaricati.personalblog.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUser(String user);
}

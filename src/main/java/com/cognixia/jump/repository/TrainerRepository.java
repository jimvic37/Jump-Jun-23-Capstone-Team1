package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.User;

public interface TrainerRepository extends JpaRepository<User, Integer> {

	public Optional<User> findByUsername(String username);
}
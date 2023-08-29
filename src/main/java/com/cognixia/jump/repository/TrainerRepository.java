package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

	public Optional<Trainer> findByUsername(String username);
}
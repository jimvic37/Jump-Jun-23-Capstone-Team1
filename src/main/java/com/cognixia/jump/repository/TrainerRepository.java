package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

	public Optional<Trainer> findByUsername(String username);
}
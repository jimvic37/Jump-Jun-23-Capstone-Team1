package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.repository.TrainerRepository;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "trainer", description = "the API for managing trainers")
public class TrainerController {

	@Autowired
	TrainerRepository repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/trainer")
	@Operation(summary = "Gets all trainers", description = "Returns a list of all trainers")
	@ApiResponse(responseCode = "200", description = "Ok")
	public List<Trainer> getTrainers() {
		return repo.findAll();
	}
	
	@GetMapping("/trainer/{id}")
	@Operation(summary = "Gets trainer by ID", description = "Returns a trainer with the ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Ok"),
		@ApiResponse(responseCode = "404", description = "Trainer not found")
	})
	public ResponseEntity<?> getTrainerById(@PathVariable int id) {
		
		Optional<Trainer> trainer = repo.findById(id);
		
		if(trainer.isEmpty()) {
			return ResponseEntity.status(404).body("User not found");
		}
		else {
			return ResponseEntity.status(200).body(trainer.get());
		}
	}

	@PostMapping("/trainer")
	@Operation(summary = "Creates trainer", description = "Creates a trainer and returns the created trainer")
	@ApiResponse(responseCode = "201", description = "Ok")
	public ResponseEntity<?> createTrainer(@RequestBody Trainer trainer ) {
		
		trainer.setId(null);
		
		// will take the plain text password and encode it before it is saved to the db
		// security isn't going to encode our passwords on its own
		trainer.setPassword( encoder.encode( trainer.getPassword() ) );
				
		Trainer created = repo.save(trainer);
		
		return ResponseEntity.status(201).body(created);
	}
	
	@PutMapping("/trainer")
	@Operation(summary = "Updates trainer", description = "Updates a trainer and returns the updated trainer")
	@ApiResponse(responseCode = "200", description = "Ok")
	public ResponseEntity<?> updateTrainer(@RequestBody Trainer trainer) throws ResourceNotFoundException {
		if (repo.existsById(trainer.getId())) {
			Trainer updated = repo.save(trainer);
			return ResponseEntity.status(200).body(updated);
		}
		
		throw new ResourceNotFoundException("Trainer", trainer.getId());
	}
	
	@DeleteMapping("/trainer/{id}")
	@Operation(summary = "Deletes trainer by ID", description = "Deletes a trainer and returns the deleted trainer")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Ok"),
		@ApiResponse(responseCode = "404", description = "Trainer not found")
	})
	public ResponseEntity<Trainer> deleteTrainer(@PathVariable int id) throws ResourceNotFoundException {
		Optional<Trainer> found = repo.findById(id);
		
		if (found.isEmpty()) {
			throw new ResourceNotFoundException("Trainer", id);
		}
		
		repo.deleteById(id);
		
		return ResponseEntity.status(200).body(found.get());
	}
}
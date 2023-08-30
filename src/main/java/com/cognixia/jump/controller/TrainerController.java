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

@RestController
@RequestMapping("/api")
public class TrainerController {

	@Autowired
	TrainerRepository repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/trainer")
	public List<Trainer> getTrainers() {
		return repo.findAll();
	}
	
	@GetMapping("/trainer/{id}")
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
	public ResponseEntity<?> createTrainer( @RequestBody Trainer trainer ) {
		
		trainer.setId(null);
		
		// will take the plain text password and encode it before it is saved to the db
		// security isn't going to encode our passwords on its own
		trainer.setPassword( encoder.encode( trainer.getPassword() ) );
				
		Trainer created = repo.save(trainer);
		
		return ResponseEntity.status(201).body(created);
	}
	
	@PutMapping("/trainer")
	public ResponseEntity<?> updateTrainer(@RequestBody Trainer trainer) throws ResourceNotFoundException {
		if (repo.existsById(trainer.getId())) {
			Trainer updated = repo.save(trainer);
			return ResponseEntity.status(200).body(updated);
		}
		
		throw new ResourceNotFoundException("Trainer with id = " + trainer.getId() + " was not found");
	}
	
	@DeleteMapping("/trainer/{id}")
	public ResponseEntity<Trainer> deleteTrainer(@PathVariable int id) throws ResourceNotFoundException {
		Optional<Trainer> found = repo.findById(id);
		
		if (found.isEmpty()) {
			throw new ResourceNotFoundException("Product with id = " + id + " was not found");
		}
		
		repo.deleteById(id);
		
		return ResponseEntity.status(200).body(found.get());
	}
}
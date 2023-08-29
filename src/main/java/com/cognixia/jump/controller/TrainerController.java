package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public List<Trainer> getUsers() {
		return repo.findAll();
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUserById(@PathVariable int id) {
		
		Optional<Trainer> trainer = repo.findById(id);
		
		if(trainer.isEmpty()) {
			return ResponseEntity.status(404).body("User not found");
		}
		else {
			return ResponseEntity.status(200).body(trainer.get());
		}
	}

	@PostMapping("/user")
	public ResponseEntity<?> createUser( @RequestBody Trainer trainer ) {
		
		trainer.setId(null);
		
		// will take the plain text password and encode it before it is saved to the db
		// security isn't going to encode our passwords on its own
		trainer.setPassword( encoder.encode( trainer.getPassword() ) );
				
		Trainer created = repo.save(trainer);
		
		return ResponseEntity.status(201).body(created);
	}
}
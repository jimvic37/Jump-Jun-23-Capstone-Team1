package com.cognixia.jump.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.repository.TrainerRepository;

@Service
public class MyTrainerDetailsService implements UserDetailsService {

	@Autowired
	TrainerRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<Trainer> trainerFound = repo.findByUsername(username);
		
		if (trainerFound.isEmpty()) {
			throw new UsernameNotFoundException("Username of " + username + " not found");
		}
		
		return new MyTrainerDetails(trainerFound.get());
	}
} 
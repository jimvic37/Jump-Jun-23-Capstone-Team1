package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import javax.net.ssl.SSLEngineResult.Status;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.TeamOverflowException;
import com.cognixia.jump.exception.TeamUnderflowException;
import com.cognixia.jump.model.Pokemon;
import com.cognixia.jump.model.Team;
import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.repository.PokemonRepository;
import com.cognixia.jump.repository.TeamRepository;
import com.cognixia.jump.repository.TrainerRepository;

@RestController
@RequestMapping("/api")
public class TeamController {

	@Autowired
	TeamRepository repo;
	
	@Autowired
	TrainerRepository trainerRepo;
	
	@Autowired
	PokemonRepository pokemonRepo;
	
	
	// admin access
	@GetMapping("/team")
	public List<Team> getTeams() {
		return repo.findAll();
	}
	
	//admin access
	@GetMapping("/team/{id}")
    public ResponseEntity<?> getTeambyId(@PathVariable int id) throws ResourceNotFoundException {
         
        Optional<Team> found = repo.findById(id);
         
        if(found.isEmpty()) {
            throw new ResourceNotFoundException("Team");
        }
        
        return ResponseEntity.status(200).body( found );
    }
	
	//admin access
	@GetMapping("/team/add/{id}")
	public ResponseEntity<?> addPokemon(@PathVariable int id) throws TeamOverflowException {
		Optional<Team> found = repo.findById(id);
		
		
		return null;
	}
	
	//admin access
	@GetMapping("/team/remove/{id}")
	public ResponseEntity<?> removePokemon(@PathVariable int id) throws TeamUnderflowException {
		return ResponseEntity.status(200).body(Status.OK);
	}
	
	// use
	@GetMapping("/team/analyze")
	public ResponseEntity<?> analyzeTeam() {
		
		return ResponseEntity.status(200).body(Status.OK);
	}
	
	@PostMapping("/team")
	public ResponseEntity<?> addPokemon(@PathParam(value = "trainerId") int trainerId, @PathParam(value = "pokemonId") int pokemonId) throws ResourceNotFoundException {
		
		// locate the trainer and the pokemon
		Optional<Trainer> trainerFound = trainerRepo.findById(trainerId);
		Optional<Pokemon> pokemonFound = pokemonRepo.findById(pokemonId);
		
		System.out.println("test");
		
		if (trainerFound.isEmpty()) {
			throw new ResourceNotFoundException("Trainer", trainerId);
		}
		else if (pokemonFound.isEmpty()) {
			throw new ResourceNotFoundException("Pokemon", trainerId);
		}
		
		Optional<Team> teamMember = repo.addedToTeam(trainerId, pokemonId);
		
		if (teamMember.isPresent()) {
			return ResponseEntity.status(400).body("This Pokemon is already in the team");
		}
		
		Team newTeamMember = new Team(null, trainerFound.get(), pokemonFound.get());
		
		Team created = repo.save(newTeamMember);
		
		return ResponseEntity.status(201).body(created);
	}
}

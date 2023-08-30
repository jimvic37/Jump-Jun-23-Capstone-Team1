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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Team", description = "The API for managing teams")
public class TeamController {

	@Autowired
	TeamRepository repo;
	
	@Autowired
	TrainerRepository trainerRepo;
	
	@Autowired
	PokemonRepository pokemonRepo;
	
	
	// admin access
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="Database has teams logged"),
			@ApiResponse(responseCode="404",description="Database does not have any teams logged")}
			)
	@Operation( summary = "Gets all the teams in the database",
			description = "Gets all the teams from the team table of the pokémon database. "
					+ "A team object describes the relationship between a pokémon and its trainer;"
					+ "a pokémon that belongs to a trainer is on the trainer's team. "
					+ "A trainer can have no more than 6 pokémon on a team, and can only have 1 team.")
	@GetMapping("/team")
	public List<Team> getTeams() {
		return repo.findAll();
	}
	
	//admin access
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="Trainer has at least 1 pokémon on a team"),
			@ApiResponse(responseCode="404",description="Trainer has no pokémon on a team")}
			)
	@Operation( summary = "Gets a single team by trainer id",
			description = "Gets all pokémon in a single team by filtering by the designated trainer's id."
			+ " A trainer can have no more than 6 pokémon on a team, and can only have 1 team.")
	@GetMapping("/team/{id}")
    public ResponseEntity<?> getTeambyId(@PathVariable int id) throws ResourceNotFoundException {
         
        Optional<Team> found = repo.findById(id);
         
        if(found.isEmpty()) {
            throw new ResourceNotFoundException("Team", id);
        }
        
        return ResponseEntity.status(200).body( found );
    }
	
	//admin access
	@ApiResponses(value = {
			@ApiResponse(responseCode="201", description="Pokémon was added to a team successfully"),
			@ApiResponse(responseCode="404",description="The trainer or the pokémon could not be found"),
			@ApiResponse(responseCode="403",description="The trainer's team is already full")}
			)
	@Operation( summary = "Attempts to add a pokémon to a team",
			description = "Attempts to add a new entry to the team database using a given pokémon id and a given trainer id."
					+ "As a team cannot be larger than 6 members, a TeamOverflowException will be thrown if that value"
					+ " is attempted to be exceeded.")
	@PostMapping("/team")
    public ResponseEntity<?> addPokemon(@PathParam(value = "trainerId") int trainerId, @PathParam(value = "pokemonId") int pokemonId) throws ResourceNotFoundException, TeamOverflowException {
        
        // locate the trainer and the pokemon
        Optional<Trainer> trainerFound = trainerRepo.findById(trainerId);
        Optional<Pokemon> pokemonFound = pokemonRepo.findById(pokemonId);
        
        if(repo.memberCount(trainerId) >=6) {
        	throw new TeamOverflowException();
        }
        
        if (trainerFound.isEmpty()) {
            throw new ResourceNotFoundException("Trainer", trainerId);
        }
        else if (pokemonFound.isEmpty()) {
            throw new ResourceNotFoundException("Pokémon", pokemonId);
        }
        
        Optional<Team> teamMember = repo.inTeam(trainerId, pokemonId);
        
        if (teamMember.isPresent()) {
            return ResponseEntity.status(400).body("This Pokémon is already in the team");
        }
        
        Team newTeamMember = new Team(null, trainerFound.get(), pokemonFound.get());
        
        Team created = repo.save(newTeamMember);
        
        return ResponseEntity.status(201).body(created);
    }
	
	//admin access
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="Pokémon was removed from a team successfully"),
			@ApiResponse(responseCode="404",description="The trainer or the pokémon could not be found"),
			@ApiResponse(responseCode="403",description="The user's team is already empty")
			}
			)
	@Operation(summary = "Attempts to remove a pokémon from a team",
			description = "Attempts to remove an entry from the team database using a given pokémon id and a given trainer id."
					+ "As you cannot remove a pokémon from an empty team, "
					+ "a TeamUnderflowException will be thrown if that is attempted.")
	@GetMapping("/team/remove/{id}")
	public ResponseEntity<?> removePokemon(@PathParam(value = "trainerId") int trainerId, @PathParam(value = "pokemonId") int pokemonId) throws ResourceNotFoundException, TeamUnderflowException {	
		// locate the trainer and the pokemon
        Optional<Trainer> trainerFound = trainerRepo.findById(trainerId);
        Optional<Pokemon> pokemonFound = pokemonRepo.findById(pokemonId);
        
        if(repo.memberCount(trainerId) == 0) {
        	throw new TeamUnderflowException();
        }
        
        if (trainerFound.isEmpty()) {
            throw new ResourceNotFoundException("Trainer", trainerId);
        }
        else if (pokemonFound.isEmpty()) {
            throw new ResourceNotFoundException("Pokémon", pokemonId);
        }
        
        Optional<Team> teamMember = repo.inTeam(trainerId, pokemonId);
        
        if (teamMember.isEmpty()) {
            return ResponseEntity.status(400).body("This Pokémon is not in the team");
        }
        
        repo.delete(teamMember.get());
        
		return ResponseEntity.status(200).body(teamMember.get());
	}
	
	// use
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="Team was analyzed successfully."),
			@ApiResponse(responseCode="404",description="The user's team is empty")}
			)
	@Operation(summary = "Analyzes a team and reports the types it is strong and weak against",
			description = "Looks through each pokemon in a team and lists the collective types "
					+ "the current team is strong and weak against.")
	@GetMapping("/team/analyze/{id}")
	public ResponseEntity<?> analyzeTeam(@PathVariable int id) {
		
		return ResponseEntity.status(200).body(Status.OK);
	}
}

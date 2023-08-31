package com.cognixia.jump.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.TeamOverflowException;
import com.cognixia.jump.exception.TeamUnderflowException;
import com.cognixia.jump.model.Pokemon;
import com.cognixia.jump.model.Team;
import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.repository.PokemonRepository;
import com.cognixia.jump.repository.TeamRepository;
import com.cognixia.jump.repository.TrainerRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	
	private static String url = "https://pokeapi.co/api/v2";
	
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
	
	@ApiResponses(value = {
			@ApiResponse(responseCode="201", description="Pokémon was added to a team successfully"),
			@ApiResponse(responseCode="404",description="The trainer or the pokémon could not be found"),
			@ApiResponse(responseCode="400",description="The chosen pokémon is already on the chosen team"),
			@ApiResponse(responseCode="403",description="The trainer's team is already full")}
			)
	@Operation( summary = "Attempts to add a pokémon to a team",
			description = "Attempts to add a new entry to the team database using a given pokémon id and a given trainer id."
					+ "As a team cannot be larger than 6 members, a TeamOverflowException will be thrown if that value"
					+ " is attempted to be exceeded.")
	@PostMapping("/team/add/{trainerId}/{pokemonId}")
    public ResponseEntity<?> addPokemon(@PathVariable int trainerId, @PathVariable int pokemonId) throws ResourceNotFoundException, TeamOverflowException {
        
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
	@DeleteMapping("/team/remove/{trainerId}/{pokemonId}")
	public ResponseEntity<?> removePokemon(@PathVariable int trainerId, @PathVariable int pokemonId) throws ResourceNotFoundException, TeamUnderflowException {	

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
	
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="Team was analyzed successfully."),
			@ApiResponse(responseCode="404",description="The user's team is empty")}
			)
	@Operation(summary = "Analyzes a team and reports the types it is strong and weak against",
			description = "Looks through each pokemon in a team and lists the collective types "
					+ "the current team is strong and weak against.")
	@GetMapping("/team/analyze/{id}")
	public ResponseEntity<?> analyzeTeam(@PathVariable int id) throws ResourceNotFoundException {
		List<String> teamTypes = repo.getTeamTypes(id);
		List<String> teamStrengths = new ArrayList<String>();
		List<String> teamWeaknesses = new ArrayList<String>();
		
		if(teamTypes.size() == 0) {
			throw new ResourceNotFoundException("Team", id);
		}
		
		for(String x: teamTypes) {
			String[] split = x.split(",");
			for(String y: split) {
				if(y != "null") {
					teamStrengths.addAll(getStrength(y.toLowerCase()));
					teamWeaknesses.addAll(getWeakness(y.toLowerCase()));
				}
			}
		}
		
		ArrayList<String> strength = new ArrayList<>(new HashSet<>(teamStrengths));
		ArrayList<String> weakness = new ArrayList<>(new HashSet<>(teamWeaknesses));
		for(String s:weakness) {
			System.out.println(s);
		}
		Map<String, List<String>> result = new LinkedHashMap<>();
		
		result.put("Strengths", strength);
		result.put("Weaknesses", weakness);
		
		return ResponseEntity.status(200).body(result);
	}
	
	/*
	 * Top Level Helper Method to get strength directly from the Pokémon API
	 */
	protected List<String> getStrength(@PathVariable String type) {
		try {
            String endpoint = "/type/" + type;
            String jsonResponse = new RestTemplate().getForObject(url + endpoint, String.class);
            List<String> doubledamage = extractStrengthsFromJson(jsonResponse); 
	            return doubledamage;           
        } catch (Exception e) {
            return new ArrayList<String>();
        }
	}
	
	/*
	 * Top Level Helper Method to get weakness directly from the Pokémon API
	 */
	protected List<String> getWeakness(@PathVariable String type) {
		try {
            String endpoint = "/type/" + type;
            String jsonResponse = new RestTemplate().getForObject(url + endpoint, String.class);
            List<String> doubledamage = extractWeaknessesFromJson(jsonResponse); 
           
	            return doubledamage;           
        } catch (Exception e) {
            return new ArrayList<String>();
        }
	}
	
	/*
	 * Second Helper Method to resolve the strength types from the JSON data
	 */
	protected List<String> extractStrengthsFromJson(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject damageObject = jsonObject.getAsJsonObject("damage_relations");
        JsonArray doubleDamageTo = damageObject.getAsJsonArray("double_damage_to");


        List<String> types = new ArrayList<String>();

        for (JsonElement e:doubleDamageTo) {
        	
            JsonObject typeObject = e.getAsJsonObject();
            String type = typeObject.get("name").getAsString();
            types.add(type);
        }
        return types;    
    }
	
	/*
	 * Second Helper Method to resolve the weakness types from the JSON data
	 */
	protected List<String> extractWeaknessesFromJson(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject damageObject = jsonObject.getAsJsonObject("damage_relations");
        JsonArray doubleDamageFrom = damageObject.getAsJsonArray("double_damage_from");


        List<String> types = new ArrayList<String>();

        for (JsonElement e:doubleDamageFrom) {
        	
            JsonObject typeObject = e.getAsJsonObject();
            String type = typeObject.get("name").getAsString();
            types.add(type);
        }
        return types;    
    }
	
}

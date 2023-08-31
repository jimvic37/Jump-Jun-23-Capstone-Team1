package com.cognixia.jump.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.cognixia.jump.repository.PokemonRepository;
import com.cognixia.jump.service.PokemonService;
import com.cognixia.jump.model.Pokemon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Pokémon controller", description = "API for managing pokemon")
public class PokemonController {
	

	@Autowired
    PokemonService service;
	
	@Autowired
	PokemonRepository repo;
	
	private static String url = "https://pokeapi.co/api/v2/pokemon/";
	
	@Operation(summary = "Get pokémon infor from pokémon API", 
			   description = "Gets pokémon's unique number, types from pokémon API. If the pokémon is already stored in the database"
			   		+ ", it just displays the pokémon name, number and type but if the pokémon is not in the databse this api adds"
			   		+ "that pokémon data.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pokémon has been found", 
						 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class) ) ),
			@ApiResponse(responseCode = "404", description = "Pokémon was not found", 
			 			 content = @Content )
		}
	)
    @GetMapping("/{pokemon}")
    public ResponseEntity<String> getPokemonInfo(@PathVariable String pokemon) {
        try {
        	
            // Stores
            String jsonResponse = new RestTemplate().getForObject(url + pokemon, String.class);
            
            int pokemonNumber = service.extractPokemonNumFromJson(jsonResponse);
            String[] types = service.extractTypesFromJson(jsonResponse);
            System.out.println("null");
            String info = pokemon + "'s ID: " + pokemonNumber + ", Types: " + String.join(", ", types);
            
            //Checks if the pokémon already exists inside the database
            //Only add the pokémon when there is no match
            if (repo.pokemonExists(pokemonNumber) == null) {
	            //Save pokémon data to the database
	            Pokemon newPokemon = new Pokemon();
	            newPokemon.setName(pokemon);
	            newPokemon.setNumber(pokemonNumber);
	            //If there is only one type for the given pokémon
	            if (types.length == 1) {
	            	newPokemon.setTypeMain(types[0]);
	            //Else if there is two types for the given pokémon
	            } else if (types.length == 2) {
	            	newPokemon.setTypeMain(types[0]);
	            	newPokemon.setTypeSecondary(types[1]);
	            }
	
	            // Save the entity to the database
	            repo.save(newPokemon);
	            return ResponseEntity.status(200).body("Added new pokémon into databse\n" + info);
            }
            return ResponseEntity.status(200).body("Pokémon already inside the databse\n" + info);
            
        } catch (Exception e) {
            return ResponseEntity.status(404).body(String.format("%s was not found. Try again.", pokemon));
        }
    }
    
}







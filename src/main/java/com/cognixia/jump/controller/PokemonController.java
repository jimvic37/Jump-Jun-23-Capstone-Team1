package com.cognixia.jump.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.cognixia.jump.repository.PokemonRepository;
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


@RestController
public class PokemonController {
	
	@Autowired
	PokemonRepository pokemonRepository;
	
	private static String url = "https://pokeapi.co/api/v2";
	@Operation(summary = "Get pokem infor from pokemon API", 
			   description = "Gets pokemon's unique number, types from pokemon API. If the pokemon is already stored in the database"
			   		+ ", it just displays the pokemon name, number and type but if the pokemon is not in the databse this api adds"
			   		+ "that pokemon data.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pokemon has been found", 
						 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class) ) ),
			@ApiResponse(responseCode = "404", description = "Pokemon was not found", 
			 			 content = @Content )
		}
	)
    @GetMapping("/{pokemon}")
    public ResponseEntity<String> getPokemonInfo(@PathVariable String pokemon) {
        try {
            String endpoint = "/pokemon/" + pokemon;
            // Stores
            String jsonResponse = new RestTemplate().getForObject(url + endpoint, String.class);
            
            int pokemonNumber = extractPokemonNumFromJson(jsonResponse);
            String[] types = extractTypesFromJson(jsonResponse);
            
            String info = pokemon + "'s ID: " + pokemonNumber + ", Types: " + String.join(", ", types);
            
            //Checks if the Pokemon already exists inside the database
            //Only add the Pokemon when there is no match
            if (pokemonRepository.pokemonExists(pokemonNumber) == null) {
	            //Save Pokemon data to the database
	            Pokemon newPokemon = new Pokemon();
	            newPokemon.setName(pokemon);
	            newPokemon.setNumber(pokemonNumber);
	            //If there is only one type for the given Pokemon
	            if (types.length == 1) {
	            	newPokemon.setTypeMain(types[0]);
	            //Else if there is two types for the given Pokemon
	            } else if (types.length == 2) {
	            	newPokemon.setTypeMain(types[0]);
	            	newPokemon.setTypeSecondary(types[1]);
	            }
	
	            // Save the entity to the database
	            pokemonRepository.save(newPokemon);
	            return ResponseEntity.status(200).body("Added new pokemon into databse\n" + info);
            }
            return ResponseEntity.status(200).body("Pokemon already inside the databse\n" + info);
            
        } catch (Exception e) {
            return ResponseEntity.status(404).body(String.format("%s was not found. Try again.", pokemon));
        }
    }
    
	//Private method to extract Pokemon types from API JSON response
    private String[] extractTypesFromJson(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray typesArray = jsonObject.getAsJsonArray("types");
        
        // Limit to at most 2 types
        String[] types = new String[Math.min(typesArray.size(), 2)]; 
        
        //Loop to get types if there are multiple types
        for (int i = 0; i < types.length; i++) {
            JsonObject typeObject = typesArray.get(i).getAsJsonObject();
            JsonObject typeInfo = typeObject.getAsJsonObject("type");
            String typeName = typeInfo.get("name").getAsString();
            types[i] = typeName;
        }

        return types;
    
    }
    
  //Private method to extract Pokemon number from API JSON response
    private int extractPokemonNumFromJson(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        int pokemonId = jsonObject.get("id").getAsInt();

        return pokemonId;
    }
    
}







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


@RestController
public class PokemonController {
	
	@Autowired
	PokemonRepository pokemonRepository;
	
	String url = "https://pokeapi.co/api/v2";
	
    @GetMapping("/{pokemon}")
    public ResponseEntity<String> getPokemonInfo(@PathVariable String pokemon) {
        try {
            String endpoint = "/pokemon/" + pokemon;
            String jsonResponse = new RestTemplate().getForObject(url + endpoint, String.class);
            
            int pokemonNumber = extractPokemonIdFromJson(jsonResponse);
            String[] types = extractTypesFromJson(jsonResponse);
            
            String info = pokemon + "'s ID: " + pokemonNumber + ", Types: " + String.join(", ", types);
            
            if (pokemonRepository.pokemonExists(pokemonNumber) == null) {
	            // Save Pokemon data to the database
	            Pokemon newPokemon = new Pokemon();
	            newPokemon.setName(pokemon);
	            newPokemon.setNumber(pokemonNumber);
	            if (types.length == 1) {
	            	newPokemon.setTypeMain(types[0]);
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving Ditto's types");
        }
    }
    
    private String[] extractTypesFromJson(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray typesArray = jsonObject.getAsJsonArray("types");

        String[] types = new String[Math.min(typesArray.size(), 2)]; // Limit to at most 2 types

        for (int i = 0; i < types.length; i++) {
            JsonObject typeObject = typesArray.get(i).getAsJsonObject();
            JsonObject typeInfo = typeObject.getAsJsonObject("type");
            String typeName = typeInfo.get("name").getAsString();
            types[i] = typeName;
        }

        return types;
    
    }
    
    private int extractPokemonIdFromJson(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int pokemonId = jsonObject.get("id").getAsInt();

        return pokemonId;
    }
    
}
package com.cognixia.jump.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cognixia.jump.repository.PokemonRepository;
import com.cognixia.jump.model.Pokemon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;

    // Extract Pokemon types from API JSON response
    public String[] extractTypesFromJson(String jsonResponse) {
    	JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonArray typesArray = jsonObject.getAsJsonArray("types");
        
        // Determine the number of types in the array
        int numTypes = typesArray.size();
        
        // Limit to at most 2 types
        int maxTypes = Math.min(numTypes, 2);
        
        String[] types = new String[maxTypes];
        
        //Loop to get types if there are multiple types
        for (int i = 0; i < types.length; i++) {
            JsonObject typeObject = typesArray.get(i).getAsJsonObject();
            JsonObject typeInfo = typeObject.getAsJsonObject("type");
            String typeName = typeInfo.get("name").getAsString();
            types[i] = typeName;
        }

        return types;
    }
    
    // Extract Pokemon number from API JSON response
    public int extractPokemonNumFromJson(String jsonResponse) {
    	JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        int pokemonId = jsonObject.get("id").getAsInt();

        return pokemonId;

    }

}
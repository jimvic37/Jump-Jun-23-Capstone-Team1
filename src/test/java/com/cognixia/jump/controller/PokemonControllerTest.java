//package com.cognixia.jump.controller;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.cognixia.jump.repository.PokemonRepository;
//import com.cognixia.jump.service.MyTrainerDetailsService;
//import com.cognixia.jump.util.JwtUtil;
//
//
//@WebMvcTest(PokemonController.class)
//public class PokemonControllerTest {
//
//    private static final String STARTING_URI = "http://localhost:8080";
//    
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private PokemonRepository repo;
//    
//    @MockBean
//    private JwtUtil jwtUtil;
//    
//    @InjectMocks
//    private PokemonController controller;
//    
//    @MockBean
//    private JwtDecoder jwtDecoder;
//
//    @Test
//    public void testGetPokemon_SuccessfulAdd() throws Exception {
//    	
//    	String pokemon = "dratini";
//    	String uri = STARTING_URI + "/{pokemon}";
//        String types = "dragon";
//        int pokemonNumber = 147;
//        String info = pokemon + "'s ID: " + pokemonNumber + ", Types: " + String.join(", ", types);
//        
//        ResponseEntity<String> response = ResponseEntity.status(200).body("Pokemon already inside the databse\n" + info);
////        when(repo.(pokemon)).thenReturn(response);
//        
//        mvc.perform(get(uri)
//        		.with(SecurityMockMvcRequestPostProcessors.jwt()))
//        		.andDo(print())
//        		.andExpect(status().isOk())
//        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        		.andExpect( jsonPath( "$.types" ).value(types) )
//				.andExpect( jsonPath( "$.pokemonNumber" ).value(pokemonNumber) );
//        		
//        		
//
////        verify(pokemonRepository, times(1)).pokemonExists(132);
////        verify(controller, times(1)).extractPokemonNumFromJson("");
////        verify(controller, times(1)).extractTypesFromJson("");
//    }
//
////    @Test
////    public void testGetPokemon_AlreadyExists() throws Exception {
////        String pokemon = "pikachu";
////
////        when(controller.extractPokemonNumFromJson("")).thenReturn(25);
////        when(controller.extractTypesFromJson("")).thenReturn(new String[] { "electric" });
////
////        mvc.perform(MockMvcRequestBuilders.get("/pokemon/{pokemon}", pokemon))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.content().string(containsString("Pokemon already inside the database")));
////
////        verify(pokemonRepository, times(1)).pokemonExists(25);
////        verify(controller, times(1)).extractPokemonNumFromJson("");
////        verify(controller, times(1)).extractTypesFromJson("");
////    }
////
////    @Test
////    public void testGetPokemon_PokemonNotFound() throws Exception {
////        String pokemon = "nonexistentpokemon";
////
////        when(controller.extractPokemonNumFromJson("")).thenReturn(0); // Or any other number
////        when(controller.extractTypesFromJson("")).thenReturn(new String[] {});
////
////        mvc.perform(MockMvcRequestBuilders.get("/pokemon/{pokemon}", pokemon))
////                .andExpect(MockMvcResultMatchers.status().isNotFound())
////                .andExpect(MockMvcResultMatchers.content().string(containsString("nonexistentpokemon was not found. Try again.")));
////
////        verify(pokemonRepository, never()).pokemonExists(0);
////        verify(controller, never()).extractPokemonNumFromJson("");
////        verify(controller, never()).extractTypesFromJson("");
////    }
//}

//Austin's PokemonController Sample Test

package com.cognixia.jump.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.cognixia.jump.controller.PokemonController;
import com.cognixia.jump.repository.PokemonRepository;
import com.cognixia.jump.repository.TrainerRepository;
import com.cognixia.jump.service.MyTrainerDetailsService;
import com.cognixia.jump.util.JwtUtil;

@WebMvcTest(PokemonController.class)
public class PokemonControllerTest {
	
	private static final String STARTING_URI = "https://pokeapi.co/api/v2";
	
	@MockBean
	private MyTrainerDetailsService trainerDetailsService;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private TrainerRepository trainerRepo;
	
	@MockBean
	private PokemonRepository pokeRepo;
	
	@MockBean
	private RestTemplate pokeAPI;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void test() throws Exception {
		
		String uri = STARTING_URI + "/{pokemon}";
		
		String json = "json";
		when(pokeAPI.getForObject(Mockito.any(URI.class), Mockito.eq(String.class))).thenReturn(json);
		
		String pokemon = "pikachu";
		
		mvc.perform(get(uri, pokemon)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
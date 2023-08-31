package com.cognixia.jump.controller;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.client.RestTemplate;

import com.cognixia.jump.exception.ResourceNotFoundException;

import com.cognixia.jump.model.Pokemon;
import com.cognixia.jump.model.Team;
import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.repository.PokemonRepository;
import com.cognixia.jump.repository.TeamRepository;
import com.cognixia.jump.repository.TrainerRepository;
import com.cognixia.jump.service.MyTrainerDetailsService;
import com.cognixia.jump.util.JwtUtil;

@WebMvcTest(TeamController.class)
public class TeamControllerTest {
	
	private static final String STARTING_URI = "http://localhost:8080/api";

	@Autowired
	private MockMvc mvc;
	
	@InjectMocks
	private TeamController controller;
	
	@MockBean
	private TeamRepository repo;
	
	@MockBean
	private TrainerRepository trainerRepo;
	
	@MockBean
	private PokemonRepository pokemonRepo;
	
	@MockBean
	private MyTrainerDetailsService detailsService;
	
	@MockBean
	private JwtUtil util;
	
	@Test
	void testGetTeams() throws Exception {
		
		String uri = STARTING_URI + "/team";
		List<Team> allTeams = new ArrayList<Team>();
		allTeams.add(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		allTeams.add(new Team(1, new Trainer(2,"user","pw123",Trainer.Role.ROLE_USER,true,"g@mail.com",null),
				new Pokemon(2,"test2",2,"fire",null, null)));
		
		when( repo.findAll() ).thenReturn(allTeams);
		
		mvc.perform( get(uri)
		.with(SecurityMockMvcRequestPostProcessors.jwt()))
		.andDo( print() ) 
		.andExpect( status().isOk() )
		.andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) ) 
		.andExpect( jsonPath( "$.length()" ).value( allTeams.size() ) ) 
		.andExpect( jsonPath("$[0].id").value(allTeams.get(0).getId()) ) 
		.andExpect( jsonPath("$[0].trainer.id").value(allTeams.get(0).getTrainer().getId() ) )
		.andExpect( jsonPath("$[0].trainer.username").value(allTeams.get(0).getTrainer().getUsername() ) )
		.andExpect( jsonPath("$[0].trainer.password").value(allTeams.get(0).getTrainer().getPassword() ) )
		.andExpect( jsonPath("$[0].trainer.role").value(allTeams.get(0).getTrainer().getRole().toString() ) )
		.andExpect( jsonPath("$[0].trainer.enabled").value(allTeams.get(0).getTrainer().isEnabled() ) )
		.andExpect( jsonPath("$[0].trainer.email").value(allTeams.get(0).getTrainer().getEmail() ) )
		.andExpect( jsonPath("$[0].trainer.team").doesNotExist())
		.andExpect( jsonPath("$[0].pokemon.id").value(allTeams.get(0).getPokemon().getId() ) )
		.andExpect( jsonPath("$[0].pokemon.name").value(allTeams.get(0).getPokemon().getName() ) )
		.andExpect( jsonPath("$[0].pokemon.number").value(allTeams.get(0).getPokemon().getNumber() ) )
		.andExpect( jsonPath("$[0].pokemon.typeMain").value(allTeams.get(0).getPokemon().getTypeMain() ) )
		.andExpect( jsonPath("$[0].pokemon.typeSecondary").value(allTeams.get(0).getPokemon().getTypeSecondary() ) )
		.andExpect( jsonPath("$[0].pokemon.team").doesNotExist())
		.andExpect( jsonPath("$[1].id").value(allTeams.get(1).getId()) ) 
		.andExpect( jsonPath("$[1].trainer.id").value(allTeams.get(1).getTrainer().getId() ) )
		.andExpect( jsonPath("$[1].trainer.username").value(allTeams.get(1).getTrainer().getUsername() ) )
		.andExpect( jsonPath("$[1].trainer.password").value(allTeams.get(1).getTrainer().getPassword() ) )
		.andExpect( jsonPath("$[1].trainer.role").value(allTeams.get(1).getTrainer().getRole().toString() ) )
		.andExpect( jsonPath("$[1].trainer.enabled").value(allTeams.get(1).getTrainer().isEnabled() ) )
		.andExpect( jsonPath("$[1].trainer.email").value(allTeams.get(1).getTrainer().getEmail() ) )
		.andExpect( jsonPath("$[1].trainer.team").doesNotExist())
		.andExpect( jsonPath("$[1].pokemon.id").value(allTeams.get(1).getPokemon().getId() ) )
		.andExpect( jsonPath("$[1].pokemon.name").value(allTeams.get(1).getPokemon().getName() ) )
		.andExpect( jsonPath("$[1].pokemon.number").value(allTeams.get(1).getPokemon().getNumber() ) )
		.andExpect( jsonPath("$[1].pokemon.typeMain").value(allTeams.get(1).getPokemon().getTypeMain() ) )
		.andExpect( jsonPath("$[1].pokemon.typeSecondary").value(allTeams.get(1).getPokemon().getTypeSecondary() ) )
		.andExpect( jsonPath("$[1].pokemon.team").doesNotExist());
		
		verify( repo, times(1) ).findAll();
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	void testGetTeamsById() throws Exception {
		String uri = STARTING_URI + "/team/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		
		when( repo.findById(team.get().getId()) ).thenReturn(team);
		
		mvc.perform( get(uri)
		.with(SecurityMockMvcRequestPostProcessors.jwt()))
		.andDo( print() ) 
		.andExpect( status().isOk() )
		.andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) ) 
		.andExpect( jsonPath("$.id").value(team.get().getId()) ) 
		.andExpect( jsonPath("$.trainer.id").value(team.get().getTrainer().getId() ) )
		.andExpect( jsonPath("$.trainer.username").value(team.get().getTrainer().getUsername() ) )
		.andExpect( jsonPath("$.trainer.password").value(team.get().getTrainer().getPassword() ) )
		.andExpect( jsonPath("$.trainer.role").value(team.get().getTrainer().getRole().toString() ) )
		.andExpect( jsonPath("$.trainer.enabled").value(team.get().getTrainer().isEnabled() ) )
		.andExpect( jsonPath("$.trainer.email").value(team.get().getTrainer().getEmail() ) )
		.andExpect( jsonPath("$.trainer.team").doesNotExist())
		.andExpect( jsonPath("$.pokemon.id").value(team.get().getPokemon().getId() ) )
		.andExpect( jsonPath("$.pokemon.name").value(team.get().getPokemon().getName() ) )
		.andExpect( jsonPath("$.pokemon.number").value(team.get().getPokemon().getNumber() ) )
		.andExpect( jsonPath("$.pokemon.typeMain").value(team.get().getPokemon().getTypeMain() ) )
		.andExpect( jsonPath("$.pokemon.typeSecondary").value(team.get().getPokemon().getTypeSecondary() ) )
		.andExpect( jsonPath("$.pokemon.team").doesNotExist());
		
		verify( repo, times(1) ).findById(team.get().getId());
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	void testGetTeamsByIdNotFound() throws Exception {
		int id = 1;
		String uri = STARTING_URI + "/team/{id}";

		when( repo.findById(id)).thenReturn(Optional.empty());
		
		mvc.perform( get(uri, id)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo( print() )
			.andExpect( status().isNotFound() );
		
		verify( repo, times(1) ).findById(id);
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	void testAddPokemon() throws Exception {
		String uri = STARTING_URI + "/team/add/1/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(team.get().getPokemon().getId())).thenReturn(Optional.of(team.get().getPokemon()));
		when(repo.inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId()) ).thenReturn(Optional.empty());
		when(repo.memberCount(team.get().getId())).thenReturn(0);
		when(repo.save(Mockito.any(Team.class))).thenReturn(team.get());
		
		mvc.perform( post(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() ) 
				.andExpect( status().isCreated() )
				.andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) ) 
				.andExpect( jsonPath("$.id").value(team.get().getId()) ) 
				.andExpect( jsonPath("$.trainer.id").value(team.get().getTrainer().getId() ) )
				.andExpect( jsonPath("$.pokemon.id").value(team.get().getPokemon().getId() ) );
		
		verify( repo, times(1) ).inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId());
		verify( repo, times(1) ).save(Mockito.any(Team.class));
		verify( repo, times(1) ).memberCount(team.get().getId());
		verify( trainerRepo, times(1) ).findById(team.get().getTrainer().getId());
		verify( pokemonRepo, times(1) ).findById(team.get().getPokemon().getId());
		verifyNoMoreInteractions(repo);
		verifyNoMoreInteractions(trainerRepo);
		verifyNoMoreInteractions(pokemonRepo);
	}
	
	@Test
	void testAddPokemonOverflow() throws Exception {
		String uri = STARTING_URI + "/team/add/1/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(team.get().getPokemon().getId())).thenReturn(Optional.of(team.get().getPokemon()));
		when(repo.memberCount(team.get().getId())).thenReturn(6);
		
		mvc.perform( post(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isForbidden() );
		
		verify( trainerRepo, times(1) ).findById(team.get().getTrainer().getId());
		verify( pokemonRepo, times(1) ).findById(team.get().getPokemon().getId());
		verify( repo, times(1) ).memberCount(team.get().getId());
		verifyNoMoreInteractions(repo);
		verifyNoMoreInteractions(pokemonRepo);
		verifyNoMoreInteractions(trainerRepo);
	}
	
	@Test
	void testAddPokemonTrainerNotFound() throws Exception {
		String uri = STARTING_URI + "/team/add/1/1";
		int id = 1;
		when(trainerRepo.findById(id)).thenReturn(Optional.empty());
		
		mvc.perform( post(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isNotFound() );
		
		verify( trainerRepo, times(1) ).findById(id);
		verifyNoMoreInteractions(trainerRepo);
	}
	
	@Test
	void testAddPokemonNotFound() throws Exception {
		String uri = STARTING_URI + "/team/add/1/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		int id = 1;
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(id)).thenReturn(Optional.empty());
		
		mvc.perform( post(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isNotFound() );
		
		verify( pokemonRepo, times(1) ).findById(id);
		verifyNoMoreInteractions(pokemonRepo);
	}
	
	@Test
	void testAddPokemonPresent() throws Exception {
		String uri = STARTING_URI + "/team/add/1/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(team.get().getPokemon().getId())).thenReturn(Optional.of(team.get().getPokemon()));
		when(repo.memberCount(team.get().getId())).thenReturn(1);
		when(repo.inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId()) ).thenReturn(team);
		
		mvc.perform( post(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isBadRequest());
		
		verify( trainerRepo, times(1) ).findById(team.get().getTrainer().getId());
		verify( pokemonRepo, times(1) ).findById(team.get().getPokemon().getId());
		verify( repo, times(1) ).memberCount(team.get().getId());
		verify( repo, times(1) ).inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId());
		verifyNoMoreInteractions(repo);
		verifyNoMoreInteractions(pokemonRepo);
		verifyNoMoreInteractions(trainerRepo);
	}
	
	@Test
	void testRemovePokemon() throws Exception {
		String uri = STARTING_URI + "/team/remove/1/1";
		
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(team.get().getPokemon().getId())).thenReturn(Optional.of(team.get().getPokemon()));
		when(repo.inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId()) ).thenReturn(team);
		when(repo.memberCount(team.get().getId())).thenReturn(6);
		doNothing().when(repo).delete(Mockito.any(Team.class));
		
		mvc.perform( delete(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isOk());
		
		verify( repo, times(1) ).inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId());
		verify( repo, times(1) ).delete(Mockito.any(Team.class));
		verify( repo, times(1) ).memberCount(team.get().getId());
		verify( trainerRepo, times(1) ).findById(team.get().getTrainer().getId());
		verify( pokemonRepo, times(1) ).findById(team.get().getPokemon().getId());
		verifyNoMoreInteractions(repo);
		verifyNoMoreInteractions(trainerRepo);
		verifyNoMoreInteractions(pokemonRepo);
	}
	
	@Test
	void testRemovePokemonUnderflow() throws Exception {
		String uri = STARTING_URI + "/team/remove/1/1";
		
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(team.get().getPokemon().getId())).thenReturn(Optional.of(team.get().getPokemon()));
		when(repo.memberCount(team.get().getId())).thenReturn(0);
		
		mvc.perform( delete(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isForbidden());
		
		verify( repo, times(1) ).memberCount(team.get().getId());
		verify( trainerRepo, times(1) ).findById(team.get().getTrainer().getId());
		verify( pokemonRepo, times(1) ).findById(team.get().getPokemon().getId());
		verifyNoMoreInteractions(repo);
		verifyNoMoreInteractions(trainerRepo);
		verifyNoMoreInteractions(pokemonRepo);
	}
	
	@Test
	void testRemovePokemonTrainerNotFound() throws Exception {
		String uri = STARTING_URI + "/team/remove/1/1";
		int id = 1;
		when(repo.memberCount(id)).thenReturn(6);
		when(trainerRepo.findById(id)).thenReturn(Optional.empty());
		
		mvc.perform( delete(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isNotFound() );
		
		verify( trainerRepo, times(1) ).findById(id);
		verifyNoMoreInteractions(trainerRepo);
	}
	
	@Test
	void testRemovePokemonNotFound() throws Exception {
		String uri = STARTING_URI + "/team/remove/1/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		int id = 1;
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(repo.memberCount(id)).thenReturn(6);
		when(pokemonRepo.findById(id)).thenReturn(Optional.empty());
		
		mvc.perform( delete(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isNotFound() );
		
		verify( pokemonRepo, times(1) ).findById(id);
		verifyNoMoreInteractions(pokemonRepo);
	}
	
	@Test
	void testRemovePokemonAbsent() throws Exception {
		String uri = STARTING_URI + "/team/remove/1/1";
		Optional<Team> team = Optional.ofNullable(new Team(1, new Trainer(1,"admin","pw123",Trainer.Role.ROLE_ADMIN,true,"e@mail.com",null),
				new Pokemon(1,"test",1,"water",null, null)));
		when(trainerRepo.findById(team.get().getTrainer().getId())).thenReturn(Optional.of(team.get().getTrainer()));
		when(pokemonRepo.findById(team.get().getPokemon().getId())).thenReturn(Optional.of(team.get().getPokemon()));
		when(repo.inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId()) ).thenReturn(Optional.empty());
		when(repo.memberCount(team.get().getId())).thenReturn(6);
		
		mvc.perform( delete(uri)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isBadRequest() );
		
		verify( pokemonRepo, times(1) ).findById(team.get().getPokemon().getId());
		verify( trainerRepo, times(1) ).findById(team.get().getTrainer().getId());
		verify( repo, times(1) ).inTeam(team.get().getTrainer().getId(), team.get().getPokemon().getId());
		verify( repo, times(1) ).memberCount(team.get().getId());
		verifyNoMoreInteractions(trainerRepo);
		verifyNoMoreInteractions(pokemonRepo);
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	void testAnalyzeTeam() throws Exception {
		int id = 1;
		String uri = STARTING_URI + "/team/analyze/{id}";
		List<String> type = new ArrayList<String>();
		List<String> strength = new ArrayList<String>();
		List<String> weakness = new ArrayList<String>();
		type.add("water");
		strength.add("rock");
		strength.add("fire");
		strength.add("ground");

		weakness.add("grass");
		weakness.add("electric");
		
		Map<String, List<String>> result = new LinkedHashMap<>();
		result.put("Strengths", strength);
		result.put("Weaknesses", weakness);

		
		RestTemplate strengthMock = mock(RestTemplate.class);
		RestTemplate weaknessMock = mock(RestTemplate.class);
		
		when(repo.getTeamTypes(id)).thenReturn(type);
		when(strengthMock.getForObject("https://pokeapi.co/api/v2/type/water", String.class)).thenReturn("{\r\n"
				+ "  \"damage_relations\": {\r\n"
				+ "    \"double_damage_to\": [\r\n"
				+ "      {\r\n"
				+ "        \"name\": \"ground\",\r\n"
				+ "        \"url\": \"https://pokeapi.co/api/v2/type/5/\"\r\n"
				+ "      },\r\n"
				+ "      {\r\n"
				+ "        \"name\": \"rock\",\r\n"
				+ "        \"url\": \"https://pokeapi.co/api/v2/type/6/\"\r\n"
				+ "      },\r\n"
				+ "      {\r\n"
				+ "        \"name\": \"fire\",\r\n"
				+ "        \"url\": \"https://pokeapi.co/api/v2/type/10/\"\r\n"
				+ "      }\r\n"
				+ "    ]}");
		
		when(weaknessMock.getForObject("https://pokeapi.co/api/v2/type/water", String.class)).thenReturn("{\r\n"
				+ "  \"damage_relations\": {\r\n"
				+ "    \"double_damage_from\": [\r\n"
				+ "      {\r\n"
				+ "        \"name\": \"grass\",\r\n"
				+ "        \"url\": \"https://pokeapi.co/api/v2/type/12/\"\r\n"
				+ "      },\r\n"
				+ "      {\r\n"
				+ "        \"name\": \"electric\",\r\n"
				+ "        \"url\": \"https://pokeapi.co/api/v2/type/13/\"\r\n"
				+ "      }\r\n"
				+ "    ]}");
		
		mvc.perform( get(uri,1)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isOk() )
				.andExpect( jsonPath("$").value(result) );
	}
	
	@Test
	void testAnalyzeEmptyTeam() throws Exception {
		int id = 1;
		String uri = STARTING_URI + "/team/analyze/{id}";
		List<String> type = new ArrayList<String>();
		when(repo.getTeamTypes(id)).thenReturn(type);
		
		mvc.perform( get(uri,1)
				.with(SecurityMockMvcRequestPostProcessors.jwt()))
				.andDo( print() )
				.andExpect( status().isNotFound() );
	}
	
	
}

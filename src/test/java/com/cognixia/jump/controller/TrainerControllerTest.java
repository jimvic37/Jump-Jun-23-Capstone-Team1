package com.cognixia.jump.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.repository.TrainerRepository;
import com.cognixia.jump.service.MyTrainerDetailsService;
import com.cognixia.jump.util.JwtUtil;

@WebMvcTest(TrainerController.class)
public class TrainerControllerTest {
	
	private static final String STARTING_URI = "http://localhost:8080/api";
	
	@MockBean
	private PasswordEncoder encoder;
	
	@MockBean
	private TrainerRepository repo;
	
	@InjectMocks
	private TrainerController controller;
	
	@MockBean
	private MyTrainerDetailsService service;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JwtDecoder jwtDecoder;
	
	@Test
	public void testGetTrainers() throws Exception {
		
		String uri = STARTING_URI + "/trainer";
		
		List<Trainer> trainers = new ArrayList<>();
		
		trainers.add(new Trainer(null, "Ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null));
		trainers.add(new Trainer(null, "Brock", "pw123", Trainer.Role.ROLE_USER, true, "b.harrison@email.com", null));
		
		when(repo.findAll()).thenReturn(trainers);
		
		mvc.perform(get(uri)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.length()").value(trainers.size()))
			.andExpect(jsonPath("$[0].id").value(trainers.get(0).getId()))
			.andExpect(jsonPath("$[0].username").value(trainers.get(0).getUsername()))
			.andExpect(jsonPath("$[0].password").value(trainers.get(0).getPassword()))
			.andExpect(jsonPath("$[0].enabled").value(trainers.get(0).isEnabled()))
			.andExpect(jsonPath("$[0].email").value(trainers.get(0).getEmail()))
			.andExpect(jsonPath("$[1].id").value(trainers.get(1).getId()))
			.andExpect(jsonPath("$[1].username").value(trainers.get(1).getUsername()))
			.andExpect(jsonPath("$[1].password").value(trainers.get(1).getPassword()))
			.andExpect(jsonPath("$[1].enabled").value(trainers.get(1).isEnabled()))
			.andExpect(jsonPath("$[1].email").value(trainers.get(1).getEmail()));
		
		verify(repo, times(1)).findAll();
		verifyNoMoreInteractions(repo);
	}

	@Test
	public void testGetTrainerById() throws Exception {
		
		int id = 1;
		String uri = STARTING_URI + "/trainer/{id}";
		
		Optional<Trainer> trainer = Optional.of(new Trainer(id, "Ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null));
		
		when(repo.findById(id)).thenReturn(trainer);
		
		System.out.println(trainer.get().getUsername());
		
		mvc.perform(get(uri, id)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.id").value(trainer.get().getId()))
			.andExpect(jsonPath("$.username").value(trainer.get().getUsername()))
			.andExpect(jsonPath("$.password").value(trainer.get().getPassword()))
			.andExpect(jsonPath("$.enabled").value(trainer.get().isEnabled()))
			.andExpect(jsonPath("$.email").value(trainer.get().getEmail()));
		
		verify(repo, times(1)).findById(id);
		verifyNoMoreInteractions(repo);
	}

	@Test
	public void testGetTrainerByIdTrainerNotFound() throws Exception {
		
		int id = 1;
		String uri = STARTING_URI + "/trainer/{id}";
		Optional<Trainer> empty = Optional.empty();
		
		when(repo.findById(id)).thenReturn(empty);
		
		mvc.perform(get(uri, id)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isNotFound());
		
  		verify(repo, times(1)).findById(id);
  		verifyNoMoreInteractions(repo);
  	}
	
  	@Test
  	public void testCreateTrainer() throws Exception {
  		
  		String uri = STARTING_URI + "/trainer";
  		
  		Trainer trainer = new Trainer(1, "Ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null);
  		
  		when(encoder.encode(trainer.getPassword())).thenReturn(trainer.getPassword());
  		when(repo.save(Mockito.any(Trainer.class))).thenReturn(trainer);
  		
  		mvc.perform(post(uri)
  			.content(trainer.toJson())
  			.contentType(MediaType.APPLICATION_JSON_VALUE)
  			.with(SecurityMockMvcRequestPostProcessors.jwt()))
  			.andDo(print())
			.andExpect(status().isCreated())
  			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.id").value(trainer.getId()))
  			.andExpect(jsonPath("$.username").value(trainer.getUsername()))
			.andExpect(jsonPath("$.password").value(trainer.getPassword()))
  			.andExpect(jsonPath("$.enabled").value(trainer.isEnabled()))
			.andExpect(jsonPath("$.email").value(trainer.getEmail()));
  		
  		verify(encoder, times(1)).encode(Mockito.any(String.class));
		verify(repo, times(1)).save(Mockito.any(Trainer.class));
	}
	
	@Test
	public void testUpdateTrainer() throws Exception {
		
		String uri = STARTING_URI + "/trainer";
		
		Trainer trainer = new Trainer(1, "Ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null);
		
		when(repo.existsById(Mockito.any(Integer.class))).thenReturn(true);
		when(repo.save(Mockito.any(Trainer.class))).thenReturn(trainer);
		
		mvc.perform(put(uri)
			.content(trainer.toJson())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.id").value(trainer.getId()))
			.andExpect(jsonPath("$.username").value(trainer.getUsername()))
			.andExpect(jsonPath("$.password").value(trainer.getPassword()))
			.andExpect(jsonPath("$.enabled").value(trainer.isEnabled()))
			.andExpect(jsonPath("$.email").value(trainer.getEmail()));
		
		verify(repo, times(1)).existsById(Mockito.any(Integer.class));
		verify(repo, times(1)).save(Mockito.any(Trainer.class));
	}
	
	@Test
	public void testUpdateTrainerTrainerNotFound() throws Exception {
		
		String uri = STARTING_URI + "/trainer";
		
		Trainer trainer = new Trainer(1, "Ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null);
		
		when(repo.existsById(Mockito.any(Integer.class))).thenReturn(false);
		
		mvc.perform(put(uri)
			.content(trainer.toJson())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
		
		verify(repo, times(1)).existsById(Mockito.any(Integer.class));
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	public void testDeleteTrainer() throws Exception {
		
		String uri = STARTING_URI + "/trainer/{id}";
		int id = 1;
		Optional<Trainer> trainer = Optional.of(new Trainer(id, "Ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null));
		
		when(repo.findById(id)).thenReturn(trainer);
		
		mvc.perform(delete(uri, id)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isOk());
		
		verify(repo, times(1)).findById(Mockito.any(Integer.class));
		verify(repo, times(1)).deleteById(Mockito.any(Integer.class));
	}
	
	@Test
	public void testDeleteTrainerTrainerNotFound() throws Exception {
		
		String uri = STARTING_URI + "/trainer/{id}";
		int id = 1;
		Optional<Trainer> empty = Optional.empty();
		
		when(repo.findById(id)).thenReturn(empty);
		
		mvc.perform(delete(uri, id)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isNotFound());
		
		verify(repo, times(1)).findById(Mockito.any(Integer.class));
		verifyNoMoreInteractions(repo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
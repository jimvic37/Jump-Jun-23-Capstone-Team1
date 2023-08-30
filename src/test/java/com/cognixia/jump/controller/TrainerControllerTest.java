package com.cognixia.jump.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.config.SecurityConfiguration;
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
	
	@Test
	public void testGetTrainers() throws Exception {
		
		String uri = STARTING_URI + "/trainer";
		
		List<Trainer> trainers = new ArrayList<>();
		
		trainers.add(new Trainer(null, "Ash", "pw123", null, true, "a.ketchum@email.com", null));
		trainers.add(new Trainer(null, "Brock", "pw123", null, true, "b.harrison@email.com", null));
		
		when(repo.findAll()).thenReturn(trainers);
		//when(jwtUtil.createToken("testuser")).thenReturn("generated_token");
		
		mvc.perform(get(uri))
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
}

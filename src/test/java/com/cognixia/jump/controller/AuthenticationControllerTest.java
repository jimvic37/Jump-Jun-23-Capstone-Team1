package com.cognixia.jump.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.model.Trainer;
import com.cognixia.jump.service.MyTrainerDetails;
import com.cognixia.jump.service.MyTrainerDetailsService;
import com.cognixia.jump.util.JwtUtil;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

	private static final String STARTING_URI = "http://localhost:8080";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private AuthenticationController controller;
	
	@MockBean
	private MyTrainerDetailsService trainerDetailsService;
	
	@Test
	public void testCreateJwtToken() throws Exception {
		
		String uri = STARTING_URI + "/authenticate";
		
		Trainer trainer = new Trainer(null, "ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null);
		
		String json = "{\"username\" : \"" + trainer.getUsername()
			+ "\", \"password\" : \"" + trainer.getPassword() + "\"}";
		
		MyTrainerDetails trainerDetails = new MyTrainerDetails(trainer);
		
		when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(null);
		when(trainerDetailsService.loadUserByUsername(trainer.getUsername())).thenReturn(trainerDetails);
		when(jwtUtil.generateTokens(trainerDetails)).thenReturn("jwt");
		
		mvc.perform(post(uri)
			.content(json)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.with(SecurityMockMvcRequestPostProcessors.jwt()))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void testCreateJwtTokenBadCredentials() throws Exception {
		
		String uri = STARTING_URI + "/authenticate";
		
		Trainer trainer = new Trainer(null, "ash", "pw123", Trainer.Role.ROLE_USER, true, "a.ketchum@email.com", null);
		
		String json = "{\"username\" : \"" + trainer.getUsername()
			+ "\", \"password\" : \"" + trainer.getPassword() + "\"}";
		
		MyTrainerDetails trainerDetails = new MyTrainerDetails(trainer);
		
		when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(new BadCredentialsException("msg"));
		when(trainerDetailsService.loadUserByUsername(trainer.getUsername())).thenReturn(trainerDetails);
		when(jwtUtil.generateTokens(trainerDetails)).thenReturn("jwt");
		
		mvc.perform(post(uri)
			.content(json)
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isForbidden());
	}
}

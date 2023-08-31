package com.cognixia.jump.controller;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

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
                new Pokemon(1,"test",1,"water",null,null)));
        allTeams.add(new Team(1, new Trainer(2,"user","pw123",Trainer.Role.ROLE_USER,true,"g@mail.com",null),
                new Pokemon(2,"test2",2,"fire",null,null)));

        System.out.println(allTeams.get(0).toJson() + "," + allTeams.get(1).toJson());

        when( repo.findAll() ).thenReturn(allTeams);

        mvc.perform( get(uri)
        .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andDo( print() ) 
        .andExpect( status().isOk() )
        .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) ) 
        .andExpect( jsonPath( "$.length()" ).value( allTeams.size() ) ) 
        .andExpect( jsonPath("$[0].id").value(allTeams.get(0).getId()) ) 
        .andExpect( jsonPath("$[0].trainer.id").value(allTeams.get(0).getTrainer().getId() ) )
        .andExpect( jsonPath("$[0].pokemon.id").value(allTeams.get(0).getPokemon().getId() ) )
        .andExpect( jsonPath("$[1].id").value(allTeams.get(1).getId()) ) 
        .andExpect( jsonPath("$[1].trainer.id").value(allTeams.get(1).getTrainer().getId() ) )
        .andExpect( jsonPath("$[1].pokemon.id").value(allTeams.get(1).getPokemon().getId() ) );
    }
}
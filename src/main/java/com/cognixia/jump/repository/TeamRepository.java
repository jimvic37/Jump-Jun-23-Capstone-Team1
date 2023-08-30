package com.cognixia.jump.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
	
	@Query("select t from Team t where t.trainer.id = ?1 and t.pokemon.id = ?2")
	public Optional<Team> inTeam(int trainerId, int pokemonId);
	
	@Query ("select count(t) from Team t where t.trainer.id = ?1")
	public int memberCount(int trainerId);

}
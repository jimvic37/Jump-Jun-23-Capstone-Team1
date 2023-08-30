package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository 
public interface PokemonRepository extends JpaRepository<Pokemon, Integer>{
	
	@Query("select p.number from Pokemon p where p.number = ?1")
	public Integer pokemonExists(int number);
	
}


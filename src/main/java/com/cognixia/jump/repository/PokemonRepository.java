package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Integer>{

}
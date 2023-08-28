package com.cognixia.jump.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "trainer_id", referencedColumnName = "id")
	private Trainer trainer;
	
	@ManyToOne
	@JoinColumn(name = "pokemon_id", referencedColumnName = "id")
	private Pokemon pokemon;

	public Team() { }

	public Team(Integer id, Trainer trainer, Pokemon pokemon) {
		super();
		this.id = id;
		this.trainer = trainer;
		this.pokemon = pokemon;
	}

	public Integer getId() { return id; } 
	public void setId(Integer id) { this.id = id; } 

	public Trainer getTrainer() { return trainer; } 
	public void setTrainer(Trainer trainer) { this.trainer = trainer; } 

	public Pokemon getPokemon() { return pokemon; } 
	public void setPokemon(Pokemon pokemon) { this.pokemon = pokemon; }

	@Override
	public String toString() {
		return "Team [id=" + id + ", trainer=" + trainer + ", pokemon=" + pokemon + "]";
	}
}
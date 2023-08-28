package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Pokemon implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private int number;
	
	@NotBlank
	private Type type;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL)
	private List<Team> team;

	public Pokemon() { }

	public Pokemon(Integer id, @NotBlank String name, @NotBlank int number, @NotBlank Type type, List<Team> team) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.type = type;
		this.team = team;
	}

	public Integer getId() { return id; } 
	public void setId(Integer id) { this.id = id; } 

	public String getName() { return name; } 
	public void setName(String name) { this.name = name; } 

	public int getNumber() { return number; } 
	public void setNumber(int number) { this.number = number; } 

	public Type getType() { return type; } 
	public void setType(Type type) { this.type = type; } 

	public List<Team> getTeam() { return team; } 
	public void setTeam(List<Team> team) { this.team = team; } 

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", name=" + name + ", number=" + number + ", type=" + type + ", team=" + team
				+ "]";
	}
}
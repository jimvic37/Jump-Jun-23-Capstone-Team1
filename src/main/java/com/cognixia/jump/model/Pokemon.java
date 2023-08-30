package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Pokemon implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int number;
	
	@Column(name = "type_main", nullable = false)
	private String typeMain;
	
	@Column(name = "type_secondary", nullable = true)
	private String typeSecondary;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL)
	private List<Team> team;

	public Pokemon() { }

	public Pokemon(Integer id, String name, int number, String typeMain, String typeSecondary, List<Team> team) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.typeMain = typeMain;
		this.typeSecondary = typeSecondary;
		this.team = team;
	}

	public Integer getId() { return id; } 
	public void setId(Integer id) { this.id = id; } 

	public String getName() { return name; } 
	public void setName(String name) { this.name = name; } 

	public int getNumber() { return number; } 
	public void setNumber(int number) { this.number = number; } 
	
	public String getTypeMain() {return typeMain;}
	public void setTypeMain(String typeMain) {this.typeMain = typeMain;}

	public String getTypeSecondary() {return typeSecondary;}

	public void setTypeSecondary(String typeSecondary) {this.typeSecondary = typeSecondary;}

	public List<Team> getTeam() { return team; } 
	public void setTeam(List<Team> team) { this.team = team; }

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", name=" + name + ", number=" + number + ", typeMain=" + typeMain
				+ ", typeSecondary=" + typeSecondary + ", team=" + team + "]";
	}
	

}
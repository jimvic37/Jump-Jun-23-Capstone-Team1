package com.cognixia.jump.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cognixia.jump.model.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {

}
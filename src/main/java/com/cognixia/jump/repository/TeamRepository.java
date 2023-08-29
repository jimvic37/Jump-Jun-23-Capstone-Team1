package com.cognixia.jump.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

}
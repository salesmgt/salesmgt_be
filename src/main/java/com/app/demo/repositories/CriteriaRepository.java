package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Criteria;

@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {
	Criteria findByName(String name);
}

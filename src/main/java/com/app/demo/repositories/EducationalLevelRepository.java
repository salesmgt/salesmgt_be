package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.EducationalLevel;

@Repository
public interface EducationalLevelRepository extends JpaRepository<EducationalLevel, Integer> {
	EducationalLevel findByName(String name);
}

package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.app.demo.models.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer>, JpaSpecificationExecutor<School> {
	boolean existsByPhone(String phone);
	boolean existsByEmail(String email);
}
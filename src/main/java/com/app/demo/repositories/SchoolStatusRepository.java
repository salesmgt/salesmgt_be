package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.SchoolStatus;

@Repository
public interface SchoolStatusRepository extends JpaRepository<SchoolStatus, String> {
	SchoolStatus findByName(String name);
}

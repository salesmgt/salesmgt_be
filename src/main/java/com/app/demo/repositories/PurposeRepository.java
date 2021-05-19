package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Purpose;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, String> {
	Purpose findByName(String name);
}

package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.TargetPurpose;

@Repository
public interface TargetPurposeRepository extends JpaRepository<TargetPurpose, String> {
	TargetPurpose findByName(String name);
}

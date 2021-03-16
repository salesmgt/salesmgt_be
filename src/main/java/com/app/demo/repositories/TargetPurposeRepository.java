package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.demo.models.TargetPurpose;

public interface TargetPurposeRepository extends JpaRepository<TargetPurpose, String> {

}

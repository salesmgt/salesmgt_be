package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.app.demo.models.TargetSchool;

@Repository
public interface TargetRepository extends JpaRepository<TargetSchool, Integer>,JpaSpecificationExecutor<TargetSchool> {

}

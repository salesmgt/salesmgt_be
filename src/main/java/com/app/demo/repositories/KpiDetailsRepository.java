package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.KpiDetails;

@Repository
public interface KpiDetailsRepository extends JpaRepository<KpiDetails, Integer>{

}

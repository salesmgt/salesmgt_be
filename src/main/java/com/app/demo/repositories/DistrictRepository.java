package com.app.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.demo.models.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer>{
	District findByName(String name);
	
}

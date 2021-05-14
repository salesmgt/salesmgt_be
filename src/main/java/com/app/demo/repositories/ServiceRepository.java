package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Service;
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>,JpaSpecificationExecutor<Service> {

}

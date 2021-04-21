package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.demo.models.Contract;

public interface ContractRepository extends JpaRepository<Contract, String> {

}

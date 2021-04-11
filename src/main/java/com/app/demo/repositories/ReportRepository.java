package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.app.demo.models.Report;

public interface ReportRepository extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report>{

}

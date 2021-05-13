package com.app.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report>{
	@Query(value = "SELECT * FROM report u WHERE DATE(u.date) = :date and u.target_school_id = :target_school_id", 
			  nativeQuery = true)
	List<Report> findReportByDateAndTarget(
			  @Param("date") String date, @Param("target_school_id") int id);
}

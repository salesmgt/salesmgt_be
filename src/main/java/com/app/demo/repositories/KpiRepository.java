package com.app.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Kpi;

@Repository
public interface KpiRepository extends JpaRepository<Kpi, Integer>{
	@Query(value = "SELECT * FROM kpi u WHERE DATE(u.date) = :date and u.user_id = :user_id", 
			  nativeQuery = true)
	Kpi findKpiByDateAndUsername(
			  @Param("date") String date, @Param("user_id") String user_id);
}

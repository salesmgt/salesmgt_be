package com.app.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report>{
	@Query(value = "SELECT * FROM report u WHERE DATE(u.date) = :date and u.task_id = :task_id", 
			  nativeQuery = true)
	List<Report> findReportByDateAndTarget(
			  @Param("date") String date, @Param("task_id") int id);
	
	@Query(value = "SELECT count(*) FROM report u join task t on u.task_id = t.id WHERE DATE(u.date) = :date and t.user_id = :user_id", 
			  nativeQuery = true)
	int countReportByDateAndUsername(
			  @Param("date") String date, @Param("user_id") String user_id);
	
	@Query(value = "SELECT count(*) FROM report u join task t on u.task_id = t.id WHERE DATE(u.date) = :date and t.user_id = :user_id and u.is_success = :is_success", 
			  nativeQuery = true)
	int countReportByDateAndUsernameAndResult(
			  @Param("date") String date, @Param("user_id") String user_id,@Param("is_success") boolean is_success);
}

package com.app.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.demo.models.Service;
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>,JpaSpecificationExecutor<Service> {

	List<Service> findByIsExpired(boolean isExpired);
	@Query(value= "select * from service s inner join task t on s.task_id = t.id where t.user_id = :user_id and t.school_year = :school_year and s.status = 'approved'", nativeQuery = true)
	List<Service> findByUsernameAndSchoolYear(@Param("user_id")String userId,@Param("school_year")String schoolYear);
}

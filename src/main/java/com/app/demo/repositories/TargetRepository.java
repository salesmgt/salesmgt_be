package com.app.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.demo.models.TargetSchool;

@Repository
public interface TargetRepository extends JpaRepository<TargetSchool, Integer>,JpaSpecificationExecutor<TargetSchool> {

	@Transactional
	@Modifying
	@Query(value= "update target_school u set u.user_id = :username where u.target_school_id = :id", nativeQuery = true)
	void assign(int id,  String username);	
	
	@Query(value= "select distinct u.school_year from target_school u order by u.school_year desc", nativeQuery = true)
	List<String> getSchoolYears();
	
	@Query(value= "select u.school_year from target_school u where u.school_id= ?1 order by u.school_year desc", nativeQuery = true)
	List<String> getSchoolYearsBySchoolId(int id);
	
	@Query(value= "SELECT COUNT(*) FROM target_school u INNER JOIN target_school_purpose p on u.target_school_purpose_id = p.target_school_purpose_id"
			+ "	 WHERE u.school_year = ?1 and p.target_school_purpose_name=?2", nativeQuery = true)
	long countBySchoolYearAndPurpose(String year,String purpose);
	
	@Query(value= "select t.school_id from target_school t where t.school_year=?1", nativeQuery = true)
	List<Integer> findSchoolIdByYear(String year);
	
	List<TargetSchool> findBySchoolYear(String schoolYear);
	
	@Query(value= "select * from target_school t where t.school_id=?1", nativeQuery = true)
	List<TargetSchool> findBySchoolId(String schoolId);
}

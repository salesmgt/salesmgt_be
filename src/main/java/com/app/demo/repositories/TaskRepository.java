package com.app.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.demo.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>,JpaSpecificationExecutor<Task> {

	@Transactional
	@Modifying
	@Query(value= "update task u set u.user_id = :username where u.id = :id", nativeQuery = true)
	void assign(int id,  String username);	
	
	@Query(value= "select distinct u.school_year from task u order by u.school_year desc", nativeQuery = true)
	List<String> getSchoolYears();
	
	@Query(value= "select u.school_year from task u where u.school_id= ?1 order by u.school_year desc", nativeQuery = true)
	List<String> getSchoolYearsBySchoolId(int id);
	
//	@Query(value= "SELECT COUNT(*) FROM task u INNER JOIN purpose p on u.purpose_id = p.id"
//			+ "	 WHERE u.school_year = ?1 and p.name=?2", nativeQuery = true)
//	long countBySchoolYearAndPurpose(String year,String purpose);
	
	@Query(value= "SELECT COUNT(*) FROM task u"
			+ "	 WHERE u.school_year = ?1 and u.user_id=?2", nativeQuery = true)
	int countByUsernameAndSchoolYear(String year,String username);
	
	@Query(value= "select t.school_id from task t where t.school_year=?1", nativeQuery = true)
	List<Integer> findSchoolIdByYear(String year);
	
	List<Task> findBySchoolYear(String schoolYear);
	
	@Query(value= "select * from task t where t.school_id=?1", nativeQuery = true)
	List<Task> findBySchoolId(String schoolId);
	
	@Query(value= "select * from task t join purpose p on t.purpose_id = p.id where t.school_year=:school_year and t.user_id IS NOT NULL and t.result =:result and p.name = :name", nativeQuery = true)
	List<Task> findBySchoolYearAndResult(@Param("school_year")String schoolYear,@Param("result")String result,@Param("name")String purpose);

	@Query(value= "select * from task t join purpose p on t.purpose_id = p.id where t.school_year=:school_year and t.user_id IS NOT NULL and t.result =:result and not p.name = :name", nativeQuery = true)
	List<Task> findBySchoolYearAndResultNotSalesMoi(@Param("school_year")String schoolYear,@Param("result")String result,@Param("name")String purpose);
}

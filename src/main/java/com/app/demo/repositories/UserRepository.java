package com.app.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User>{
	User findByUsernameAndActive(String username,boolean active);
	User findByUsername(String username);
	User findByUsernameAndActiveAndPrivateToken(String username,boolean active, String privateToken);
	@Transactional
	@Modifying
	@Query(value= "update user u set u.user_email = :value where u.username = :username", nativeQuery = true)
	void updateEmail(String username,  String value);
	@Transactional
	@Modifying
	@Query(value= "update user u set u.user_phone = :value where u.username = :username", nativeQuery = true)
	void updatePhone(String username,  String value);
	
	@Transactional
	@Modifying
	@Query(value= "update user u set u.user_avatar = :value where u.username = :username", nativeQuery = true)
	void updateAvatar(String username,  String value);
	
	@Query(value = "SELECT u FROM user u WHERE u.password_hash = :password and u.username = :username", 
			  nativeQuery = true)
	User findUserByHashPasswordAndUsername(@Param("password") String password, @Param("username") String username);
	
	@Query(value ="SELECT * FROM user u INNER JOIN role r on u.role_id = r.role_id WHERE u.address LIKE %:keyword% and r.name= :role and u.is_active = :is_active",
			nativeQuery = true)
	List<User> findByAddressContains(@Param("keyword")String keyword, @Param("role")String role,@Param("is_active")boolean active);
	
	@Query(value ="SELECT * FROM user u INNER JOIN role r on u.role_id = r.role_id WHERE r.name= :role and u.is_active = :is_active",
			nativeQuery = true)
	List<User> findAllSaleman(@Param("role")String role,@Param("is_active")boolean active);
	
	@Query(value ="SELECT DISTINCT u.username FROM user u INNER JOIN role r on u.role_id = r.role_id INNER JOIN task t on u.username = t.user_id WHERE r.name= :role and u.is_active = :is_active and t.school_year = :school_year",
			nativeQuery = true)
	List<String> findAllUsername(@Param("role")String role,@Param("is_active")boolean active,@Param("school_year")String school_year);
	}

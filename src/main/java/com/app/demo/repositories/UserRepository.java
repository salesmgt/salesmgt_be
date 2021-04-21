package com.app.demo.repositories;

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
	User findByUsername(String username);
	
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
	@Query(value= "update user u set u.address = :value where u.username = :username", nativeQuery = true)
	void updateAddress(String username,  String value);
	@Transactional
	@Modifying
	@Query(value= "update user u set u.user_avatar = :value where u.username = :username", nativeQuery = true)
	void updateAvatar(String username,  String value);
	
	@Query(value = "SELECT u FROM user u WHERE u.password_hash = :password and u.username = :username", 
			  nativeQuery = true)
	User findUserByHashPasswordAndUsername(
			  @Param("password") String password, @Param("username") String username);
	}

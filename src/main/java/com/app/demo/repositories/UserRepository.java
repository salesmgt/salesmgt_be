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
	@Query(value= "update user u set u.address = :value, u.longitude = :longitude, u.latitude =:latitude where u.username = :username", nativeQuery = true)
	void updateAddress(String username,  String value,double longitude,double latitude);
	@Transactional
	@Modifying
	@Query(value= "update user u set u.user_avatar = :value where u.username = :username", nativeQuery = true)
	void updateAvatar(String username,  String value);
	
	@Query(value = "SELECT u FROM user u WHERE u.password_hash = :password and u.username = :username", 
			  nativeQuery = true)
	User findUserByHashPasswordAndUsername(
			  @Param("password") String password, @Param("username") String username);
	}

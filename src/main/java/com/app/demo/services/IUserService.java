package com.app.demo.services;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.RecoverRequest;
import com.app.demo.dtos.RequestPasswordDTO;
import com.app.demo.dtos.UserDTO;

public interface IUserService {
	void insert(UserDTO dto) throws SQLIntegrityConstraintViolationException;
	void delete(String id);
	List<UserDTO> getUsers(String key);
	void updatePassword(String username, RequestPasswordDTO dto) throws SQLIntegrityConstraintViolationException;
    void update(String username, UserDTO dto) throws SQLIntegrityConstraintViolationException;
	UserDTO getOne(String username);
	void generateToken(String username) throws Exception;
	boolean validateToken(RecoverRequest user);
	void updateProfile(String username,String attribute, double longitude, double latitude,String value);
	void updatePasswordRecover(RecoverRequest request);
	Paging<UserDTO> getUserByFilter(String key,int page, int limit, String column, String direction, Boolean isActive, String role,String fullName);
}

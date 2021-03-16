package com.app.demo.services;

import com.app.demo.dtos.UserDTO;
import com.app.demo.pagination.Paging;

public interface IUserService {
	void insert(UserDTO dto);
	void delete(String id);
	UserDTO getOne(String username);
	Paging<UserDTO> getUserByFilter(String key,int page, int limit, String column, String direction, boolean isActive, String role);
}

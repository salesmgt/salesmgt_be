package com.app.demo.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.Role;
import com.app.demo.repositories.RoleRepository;
import com.app.demo.services.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {
	
	@Autowired
	private RoleRepository repo;
	
	@Override
	public List<String> getAll(){
		List<String> roles = repo.findAll().stream().map(Role::getName).collect(Collectors.toList());
		return roles;
	}
}

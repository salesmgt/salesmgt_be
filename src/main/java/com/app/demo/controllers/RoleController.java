package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.IRoleService;

@CrossOrigin
@RestController
@RequestMapping(path = "/roles")
public class RoleController {
	
	@Autowired
	private IRoleService service;
	
	@GetMapping
	public List<?> getAll(){
		return service.getAll();
	}
}

package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.IServiceTypeService;

@CrossOrigin
@RestController
@RequestMapping(path = "/serviceTypes")
public class ServiceTypeController {
	@Autowired
	private IServiceTypeService service;
	
	@GetMapping
	public List<?> getAll(){
		return service.getList();
	}

}

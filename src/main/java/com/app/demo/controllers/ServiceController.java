package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.ServiceDTO;
import com.app.demo.services.IServiceService;

@CrossOrigin
@RestController
@RequestMapping(path = "/services")
public class ServiceController {
	@Autowired
	private IServiceService service;
	
	@PostMapping
	public String insert(@RequestBody ServiceDTO dto) {
		service.insert(dto);
		return "Inserted";
	}
	@PutMapping("/{serviceId}")
	public String update(@PathVariable int serviceId, @RequestBody ServiceDTO dto) {
		service.update(serviceId, dto);
		return "Updated";
	}
}

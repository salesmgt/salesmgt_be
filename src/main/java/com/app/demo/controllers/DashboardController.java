package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.DashboardDTO;
import com.app.demo.services.ITaskSchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {
	@Autowired 
	private ITaskSchoolService service;
	
	@GetMapping
	private DashboardDTO getDashboard(){	
		return service.getDataset();
	}

}

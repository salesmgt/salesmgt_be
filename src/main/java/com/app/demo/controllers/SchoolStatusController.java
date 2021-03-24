package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.ISchoolStatusService;

@CrossOrigin
@RestController
@RequestMapping(path = "/school-status")
public class SchoolStatusController {
	@Autowired
	private ISchoolStatusService service;
	@GetMapping
	private List<String> getStatus(){
		return service.getSchoolStatus();
	}
}

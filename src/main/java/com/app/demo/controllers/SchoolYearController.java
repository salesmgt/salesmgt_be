package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.ITaskSchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/school-years")
public class SchoolYearController {
	@Autowired
	private ITaskSchoolService service;

	@GetMapping
	public List<String> getSchoolYears(){
		return service.getSchoolYear();
	}
}

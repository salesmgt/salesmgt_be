package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.IEducationalLevelService;

@CrossOrigin
@RestController
@RequestMapping(path = "/levels")
public class LevelController {

	@Autowired
	private IEducationalLevelService service;
	@GetMapping
	public List<?> getLevels(){
	return service.getAll();
}
}

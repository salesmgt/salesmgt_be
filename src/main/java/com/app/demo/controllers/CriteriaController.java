package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.ICriteriaService;

@CrossOrigin
@RestController
@RequestMapping(path = "/criteria")
public class CriteriaController {

	@Autowired
	private ICriteriaService service;
	@GetMapping
	public List<?>getList(){
		return service.getAll();
	}
}

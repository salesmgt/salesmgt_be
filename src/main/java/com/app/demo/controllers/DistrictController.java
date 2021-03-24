package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.IDistrictService;

@CrossOrigin
@RestController
@RequestMapping(path = "/districts")
public class DistrictController {
	@Autowired 
	private IDistrictService service;
	
	@GetMapping
	private List<String> getDistricts(){
		List<String> list = service.getDistricts();
		return list;
	}
}

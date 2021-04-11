package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.LocationCard;
import com.app.demo.services.ITargetSchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/locations")
public class LocationController {
	@Autowired
	private ITargetSchoolService service;
	@GetMapping
	public List<LocationCard> getNameAndDistrict(@RequestParam String username, @RequestParam(required = false) String key){
		return service.getTargetSchoolName(username, key);
	}
}

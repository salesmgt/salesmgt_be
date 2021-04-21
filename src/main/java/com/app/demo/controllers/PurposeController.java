
package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.services.IPurposeService;

@CrossOrigin
@RestController
@RequestMapping(path = "/purposes")
public class PurposeController {
	@Autowired
	private IPurposeService service;
	
	@GetMapping
	public List<?> getPurpose(){
		return service.getPurposes();
	}
}

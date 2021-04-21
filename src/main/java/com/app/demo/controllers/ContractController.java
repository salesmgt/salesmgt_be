package com.app.demo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.ContractDTO;

@CrossOrigin
@RestController
@RequestMapping(path = "/contracts")
public class ContractController {
	
	@PostMapping
	public String insert(@RequestBody ContractDTO request) {
		
		return "Inserted";
	}
}

package com.app.demo.controllers;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.RecoverRequest;
import com.app.demo.services.IUserService;

@CrossOrigin
@RestController
@RequestMapping(path = "/recover-password")
public class RecoverPasswordController {

	@Autowired
	private IUserService service;
	
	@PostMapping
	public boolean validateToken(@RequestBody RecoverRequest request) {
		boolean result = service.validateToken(request);
		return result;
	}
	@GetMapping
	public String generateToken(@RequestParam String username) throws Exception {
		service.generateToken(username);
		
		return "Done";
	}
	@PatchMapping("/{username}")
	public String updatePassword(@PathVariable String username, @RequestBody RecoverRequest request) throws SQLIntegrityConstraintViolationException {
		service.updatePasswordRecover(request);
		return "Updating is successfull";
	}

}

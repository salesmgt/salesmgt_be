package com.app.demo.controllers;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.RequestPasswordDTO;
import com.app.demo.dtos.RequestProfileDTO;
import com.app.demo.dtos.UserDTO;
import com.app.demo.services.IUserService;

@CrossOrigin
@RestController
@RequestMapping(path = "/users")
public class UserController {
	@Autowired
	private IUserService service;
	
	@PostMapping
	public String insert(@RequestBody @Valid UserDTO dto, BindingResult bindingResult) throws SQLIntegrityConstraintViolationException {
			service.insert(dto);
			return "User inserting is done";
		
	}

	@PutMapping("/{username}")
	public String update(@PathVariable String username,
			@RequestBody @Valid UserDTO dto, BindingResult bindingResult) throws SQLIntegrityConstraintViolationException {
		if (ObjectUtils.isEmpty(service.getOne(username))) {
			throw new IndexOutOfBoundsException("Could not find the username");
		} else {
			service.update(username,dto);
			return "User updating is done";
		}
	}

	@DeleteMapping("/{username}")
	public String remove(@PathVariable String username) {
		if (ObjectUtils.isEmpty(service.getOne(username))) {
			throw new IndexOutOfBoundsException("Could not find the username");
		} else {
			service.delete(username);
			return "User removing is done";
		}
	}

	@GetMapping("/{username}")
	public UserDTO getOne(@PathVariable String username) {
		UserDTO dto = service.getOne(username);
		if (ObjectUtils.isEmpty(dto)) {
			throw new IndexOutOfBoundsException("Could not find the username");
		} else {
			return dto;
		}
	}

	@GetMapping
	public Paging<UserDTO> getByFilter(@RequestParam(required = false) String key,
										@RequestParam(defaultValue = "0")int page,
										@RequestParam(defaultValue = "10") int limit, 
										@RequestParam(defaultValue = "username")String column,
										@RequestParam(defaultValue = "ASC")String direction,
										@RequestParam(required = false) boolean active, 
										@RequestParam(required = false) String fullName, 
										@RequestParam(required = false) String role) {
		Paging<UserDTO> userPage = service.getUserByFilter(key, page,
				limit, column, direction,active,role,fullName);
		return userPage;
	}

	@PatchMapping("/{username}")
	public String updateProfile(@PathVariable String username, @RequestBody RequestProfileDTO dto) {
		service.updateProfile(username, dto.getAttribute(), dto.getValue());
		return "Updating is successfull";
	}
	@PostMapping("/{username}")
	public String updateProfile(@PathVariable String username, @RequestBody @Valid RequestPasswordDTO dto,BindingResult bindingResult) throws SQLIntegrityConstraintViolationException {
		service.updatePassword(username, dto);
		return "Updating is successfull";
	}

}

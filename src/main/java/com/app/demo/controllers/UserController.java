package com.app.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.UserDTO;
import com.app.demo.pagination.Paging;
import com.app.demo.services.IUserService;

@CrossOrigin
@RestController
@RequestMapping(path = "/users")
public class UserController {
	@Autowired
	private IUserService service;

	@PostMapping
	public String insert(@RequestBody @Valid UserDTO dto, BindingResult bindingResult) {
		if (!ObjectUtils.isEmpty(service.getOne(dto.getUsername()))) {
			throw new DataIntegrityViolationException("Dupplicate username");
		} else {
			service.insert(dto);
			return "User inserting is done";
		}
	}

	@PutMapping
	public String update(@RequestBody @Valid UserDTO dto, BindingResult bindingResult) {
		if (ObjectUtils.isEmpty(service.getOne(dto.getUsername()))) {
			throw new IndexOutOfBoundsException("Could not find the username");
		} else {
			service.insert(dto);
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

	@GetMapping("/{id}")
	public UserDTO getOne(@PathVariable String id) {
		UserDTO dto = service.getOne(id);
		if (ObjectUtils.isEmpty(dto)) {
			throw new IndexOutOfBoundsException("Could not find the username");
		} else {
			return dto;
		}
	}

	@GetMapping
	public Paging<UserDTO> getByFilter(@RequestParam(required = false) String key,
										@RequestParam(defaultValue = "0")int page,
										@RequestParam(defaultValue = "5") int limit, 
										@RequestParam(defaultValue = "username")String column,
										@RequestParam(defaultValue = "ASC")String direction,
										@RequestParam(required = false) boolean active, 
										@RequestParam(required = false) String role) {
		Paging<UserDTO> userPage = service.getUserByFilter(key, page,
				limit, column, direction,active,role);
		return userPage;
	}
}

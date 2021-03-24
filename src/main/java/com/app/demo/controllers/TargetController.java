package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.TargetDTO;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;
import com.app.demo.models.TargetSchool_;
import com.app.demo.pagination.Paging;
import com.app.demo.services.ITargetSchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/targets")
public class TargetController {
	
	@Autowired
	ITargetSchoolService service;
	
	@GetMapping
	public Paging<TargetDTO> getTargetByFilter(@RequestParam(required = false)String key,
			@RequestParam(required = false) String fullName,
			//@RequestParam(required = false) String username,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String schoolYear,
			@RequestParam(required = false) Scale scale,
			@RequestParam(required = false) Level level,
			@RequestParam(required = false) SchoolType type,
			@RequestParam(required = false) String purpose,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int limit, 
			@RequestParam(defaultValue = TargetSchool_.ID) String column,
			@RequestParam(defaultValue = "DES") String direction){
		Paging<TargetDTO> targets = service.getTargetByFilter(key,purpose,type,level,scale, fullName,district, schoolYear, 
				page, limit, column, direction);
		return targets;
		
	}
}

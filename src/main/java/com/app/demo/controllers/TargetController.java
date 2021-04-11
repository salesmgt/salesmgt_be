package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.RequestAsignObject;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;
import com.app.demo.models.TargetSchool_;
import com.app.demo.services.ITargetSchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/targets")
public class TargetController {
	
	@Autowired
	ITargetSchoolService service;
	
	@GetMapping
	public Paging<TargetDTO> getTargetByFilter(@RequestParam(required = false) String key,
			@RequestParam(required = false) String fullName,
			//@RequestParam(required = false) String username,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String schoolYear,
			@RequestParam(required = false) Scale scale,
			@RequestParam(required = false) String level,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String purpose,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, 
			@RequestParam(defaultValue = TargetSchool_.ID) String column,
			@RequestParam(defaultValue = "DESC") String direction){
		Paging<TargetDTO> targets = service.getTargetByFilter(key,purpose,SchoolType.valueOfLabel(type),Level.valueOfLabel(level),scale, fullName,district, schoolYear, 
				page, limit, column, direction);
		return targets;	
	}
	@PatchMapping
	public String assign(@RequestBody RequestAsignObject request) {
		service.assign(request.getTargetId(), request.getUsername());
		return "Assigned";
	}
	@GetMapping("/school-years")
	public List<String> getSchoolYearBySchoolId(@RequestParam int id){
		return service.getSchoolYearBySchoolId(id);
	}

}

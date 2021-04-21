package com.app.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.AddTargetRequest;
import com.app.demo.dtos.MutiAssignRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.RequestAsignObject;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.dtos.TargetFilterRequest;
import com.app.demo.dtos.TargetRequest;
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
	@PatchMapping("/{targetId}")
	public String assign(@RequestParam int targetId,@RequestBody RequestAsignObject request) {
		service.assign(targetId, request.getUsername());
		return "Assigned";
	}
	/*@PostMapping()
	public String insert(@RequestBody TargetRequest request) {
		service.insert(request);
		return "Inserted";
	}*/
	@GetMapping("/school-years")
	public List<String> getSchoolYearBySchoolId(@RequestParam int id){
		return service.getSchoolYearBySchoolId(id);
	}
	@PostMapping("/mutiple-assign")
	public String assignMutiple(@RequestBody @Valid MutiAssignRequest request, BindingResult bindingResult) {
		service.assignMutiple(request);
		return "Assigned";
	}
	@DeleteMapping("/{targetId}")
	public String delete(@PathVariable int targetId) {
		service.delete(targetId);
		return "Deleted";
	}
	@GetMapping("/{targetId}")
	public TargetDTO getOne(@PathVariable int targetId) {
		return service.getOne(targetId);
	}
	@PostMapping
	public String createByFilter(@RequestBody AddTargetRequest request) {
		int result = service.insert(request);
		return "Inserting "+result;
	}
}

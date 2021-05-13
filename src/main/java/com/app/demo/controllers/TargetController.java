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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.NotePurposeRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.dtos.TargetDetails;
import com.app.demo.dtos.TargetTimelineItem;
import com.app.demo.dtos.TargetUpdateRequest;
import com.app.demo.models.SchoolType;
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
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String schoolYear,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String level,
			@RequestParam(required = false) Boolean isAssigned,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String purpose,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, 
			@RequestParam(defaultValue = "id") String column,
			@RequestParam(defaultValue = "DESC") String direction){
		Paging<TargetDTO> targets = service.getTargetByFilter(isAssigned,status,username,key,purpose,SchoolType.valueOfLabel(type),level,fullName,district, schoolYear, 
				page, limit, column, direction);
		return targets;	
	}
//	@PutMapping("/assign/{targetId}")
//	public String assign(@PathVariable int targetId,@RequestBody TargetDTO request) {
//		service.assign(targetId, request.getUsername());
//		return "Assigned";
//	}
	@PutMapping("/unassign/{targetId}")
	public String unassign(@PathVariable int targetId) {
		service.unassign(targetId);
		return "Unassigned";
	}
	@PutMapping("/{targetId}")
	public String update(@PathVariable int targetId, @RequestBody TargetUpdateRequest request) {
		service.updateTarget(targetId, request);
		return "Inserted";
	}
	@GetMapping("/school-years")
	public List<String> getSchoolYearBySchoolId(@RequestParam int id){
		return service.getSchoolYearBySchoolId(id);
	}
	@PutMapping("/mutiple-assign")
	public String assignMutiple(@RequestBody @Valid List<TargetDTO> request, BindingResult bindingResult) {
		service.assignMutiple(request);
		return "Assigned";
	}
	@DeleteMapping("/{targetId}")
	public String delete(@PathVariable int targetId) {
				return service.delete(targetId);
		
	}
	@GetMapping("/{targetId}")
	public TargetDetails getOne(@PathVariable int targetId) {
		return service.getOne(targetId);
	}
	@GetMapping("/timeline/{targetId}")
	public List<TargetTimelineItem> getTimeline(@PathVariable int targetId){
		return service.getTimeline(targetId);
	}
	@PostMapping
	public String createTarget(@RequestBody List<TargetDTO> request) {
		int result = service.insert(request);
		return "Inserting "+result;
	}
	@PatchMapping("/{targetId}")
	public String updatePurposeNote(@PathVariable int targetId,@RequestBody NotePurposeRequest request ) {
		service.updatePurposeNote(targetId, request);
		return "Updated";
	}
}

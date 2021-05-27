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
import com.app.demo.dtos.TaskDTO;
import com.app.demo.dtos.TaskDetails;
import com.app.demo.dtos.TaskTimelineItem;
import com.app.demo.dtos.TargetUpdateRequest;
import com.app.demo.models.SchoolType;
import com.app.demo.services.ITaskSchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/tasks")
public class TaskController {
	
	@Autowired
	ITaskSchoolService service;
	
	@GetMapping
	public Paging<TaskDTO> getTargetByFilter(@RequestParam(required = false) String key,
			@RequestParam(required = false) String fullName,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String schoolYear,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String level,
			@RequestParam(required = false) Boolean isAssigned,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String result,
			@RequestParam(required = false) String purpose,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, 
			@RequestParam(defaultValue = "id") String column,
			@RequestParam(defaultValue = "DESC") String direction){
		Paging<TaskDTO> targets = service.getTargetByFilter(result,isAssigned,status,username,key,purpose,SchoolType.valueOfLabel(type),level,fullName,district, schoolYear, 
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
	@PutMapping("/{taskId}")
	public String update(@PathVariable int taskId, @RequestBody TargetUpdateRequest request) {
		service.updateTarget(taskId, request);
		return "Inserted";
	}
	@GetMapping("/school-years")
	public List<String> getSchoolYearBySchoolId(@RequestParam int id){
		return service.getSchoolYearBySchoolId(id);
	}
	@PutMapping("/mutiple-assign")
	public String assignMutiple(@RequestBody @Valid List<TaskDTO> request, BindingResult bindingResult) {
		service.assignMutiple(request);
		return "Assigned";
	}
	@DeleteMapping("/{taskId}")
	public String delete(@PathVariable int taskId) {
				return service.delete(taskId);
		
	}
	@GetMapping("/{taskId}")
	public TaskDetails getOne(@PathVariable int taskId) {
		return service.getOne(taskId);
	}
	@GetMapping("/timeline/{taskId}")
	public List<TaskTimelineItem> getTimeline(@PathVariable int taskId){
		return service.getTimeline(taskId);
	}
	@PostMapping
	public String createTarget(@RequestBody List<TaskDTO> request) {
		int result = service.insert(request);
		return "Inserting "+result;
	}
	@PatchMapping("/{taskId}")
	public String updatePurposeNote(@PathVariable int taskId,@RequestBody NotePurposeRequest request ) {
		service.updatePurposeNote(taskId, request);
		return "Updated";
	}
	@PutMapping("/completed/{taskId}")
	public String completeTask(@PathVariable int taskId) {
		service.completeTask(taskId);
		return "Completed";
	}
	@PutMapping("/failed/{taskId}")
	public String faildTask(@PathVariable int taskId) {
		service.failTask(taskId);
		return "Completed";
	}
}

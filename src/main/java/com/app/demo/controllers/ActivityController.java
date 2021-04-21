package com.app.demo.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.app.demo.dtos.ActivityDTO;
import com.app.demo.dtos.ActivityViewRequest;
import com.app.demo.dtos.DeleteArrayObject;
import com.app.demo.services.IActivityService;

@CrossOrigin
@RestController
@RequestMapping(path = "/activity")
public class ActivityController {
	@Autowired
	private IActivityService service;
	
	@GetMapping
	public List<ActivityDTO> getList(@RequestParam(defaultValue= "navigating") String name, @RequestParam(defaultValue = "Week") String currentView,
			@RequestParam String username, @RequestParam String currentDate){
		ActivityViewRequest request = new ActivityViewRequest();
		request.setUsername(username);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(currentDate, formatter);   
		request.setCurrentDate(date);
		request.setCurrentView(currentView);
		request.setName(name);
		List<ActivityDTO> dtos = null;
		try{
		 dtos = service.getActivities(request);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dtos;
	}
	@PostMapping()
	public void insert (@RequestBody ActivityDTO dto ) {
		service.insert(dto);
	}
	@DeleteMapping()
	public void remove (@RequestBody DeleteArrayObject object) {
		List<Integer> data = object.getItems();
		service.delete(data);
	}
	@PutMapping("/{id}")
	public void update(@PathVariable int id, @RequestBody ActivityDTO dto) {
		if(dto.getId() ==-1) {
			String ex = dto.getRecurrenceException(); 
			dto.setRecurrenceException(null);
			dto.setRecurrenceRule(null);		
			service.updatePatch(id, ex);
			service.insert(dto);		
			return;
		}
		if(dto.getId() == -2) {
			service.updatePatch(id, dto.getRecurrenceException());
			return;
		}
		service.update(dto, id);
	}
}

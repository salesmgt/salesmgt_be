package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.KpiDetailsDTO;
import com.app.demo.dtos.KpiInsertObject;
import com.app.demo.dtos.KpiUserDetails;
import com.app.demo.services.IKpiService;

import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping(path = "/kpis")
public class KpiController {
	@Autowired
	private IKpiService service;
	
	@PostMapping
	public String insert(@RequestBody KpiInsertObject request) {
		service.insert(request);
		return "Inserting";
	}
	@GetMapping("/{id}")
	public KpiUserDetails getOne(@PathVariable int id) {
		return service.getByKpiId(id);
	}
	@PutMapping("/{id}")
	public String updateKPIManual(@PathVariable int id,
	@ApiParam(value = "This is actual value", required = true) 
	@RequestParam double value) {
		 service.updateKPIManual(id, value);
		return "Completed";
	}
}

package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.KpiGroupDTO;
import com.app.demo.dtos.KpiGroupDetails;
import com.app.demo.services.IKpiService;

@CrossOrigin
@RestController
@RequestMapping(path = "/kpi-groups")
public class KpiGroupController {
	@Autowired
	private IKpiService service;
	@GetMapping
	public List<KpiGroupDTO> getAll(@RequestParam(required = false) String status,@RequestParam(required = false) String key){
		return service.getAll(status, key);
	}
	@GetMapping("/{groupId}")
	public KpiGroupDetails getAll(@PathVariable int groupId){
		return service.getOneKpiGroup(groupId);
	}
}

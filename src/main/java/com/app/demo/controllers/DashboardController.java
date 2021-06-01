package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.DashboardDTO;
import com.app.demo.services.ISchoolService;
import com.app.demo.services.IServiceService;
import com.app.demo.services.ITaskSchoolService;

import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {
	@Autowired 
	private IServiceService service;
	
	@Autowired 
	private ISchoolService schoolService;
	
	/**
	 * 
	 * @param type - pie/column. Là dạng chart cần biểu diễn
	 * @param name - pie(school-status/level/district/school-type) - column(DS/SL)
	 * @return
	 */
	@GetMapping
	private List<?> getDashboard(
			@ApiParam(value = "pie hoặc column. Là dạng chart cần biểu diễn", required = true)
			@RequestParam String type,
			  @ApiParam(value = " pie(school-status/level/district/school-type) - column(DS/SL)", required = true)
			@RequestParam String name){	
		if(type.equalsIgnoreCase("pie"))
		return schoolService.getSchoolType(name);
		else if (type.equalsIgnoreCase("column"))
			return service.getServiceForDashBoard(name);
		else return null;
	}

}

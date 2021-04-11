package com.app.demo.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ReportDTO;
import com.app.demo.services.IReportService;

@CrossOrigin
@RestController
@RequestMapping(path = "/reports")
public class ReportController {
	@Autowired
	private IReportService service;
	
	@GetMapping
	public Paging<ReportDTO> getListByFilter(@RequestParam(required = false) String key,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String purpose,
			@RequestParam(required = false) String fullName,
			@RequestParam(required = false) String schoolYear,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "id") String column,
			@RequestParam(defaultValue = "ASC") String direction){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		Date convertedFromDate = null;
		Date convertedToDate =  null;
		try {
			convertedFromDate = sdf.parse(fromDate);
			convertedToDate = sdf.parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return service.getReportByFilter(key, district, purpose, fullName, schoolYear, convertedFromDate, convertedToDate, page, limit, column, direction);
		
	}
}

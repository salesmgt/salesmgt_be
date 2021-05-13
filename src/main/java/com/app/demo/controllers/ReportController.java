package com.app.demo.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.demo.dtos.CommentRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ReportDTO;
import com.app.demo.dtos.ReportDetails;
import com.app.demo.models.Report;
import com.app.demo.services.IReportService;

@CrossOrigin
@RestController
@RequestMapping(path = "/reports")
public class ReportController {
	@Autowired
	private IReportService service;
	
	@PostMapping
	public String insert(@RequestBody List<ReportDTO> request) throws SQLIntegrityConstraintViolationException {
		 String result =  service.insert(request);
		return result;
	}
	@DeleteMapping("/{reportId}")
	public String remove(@PathVariable int reportId) throws SQLIntegrityConstraintViolationException {
		Report result  =service.delete(reportId);
		if(ObjectUtils.isEmpty(result))
			throw new SQLIntegrityConstraintViolationException("Lỗi không xác định");
		return "Removed";
	}
	@PutMapping("/{reportId}")
	public String update(@PathVariable int reportId,@RequestBody ReportDetails report) throws SQLIntegrityConstraintViolationException {
		Report result  = service.update(reportId, report);
		if(ObjectUtils.isEmpty(result))
			throw new SQLIntegrityConstraintViolationException("Lỗi không xác định");
		return "updated";
	}
	
	@GetMapping
	public Paging<ReportDTO> getListByFilter(
			@RequestParam(required = false,defaultValue = "0") int targetId,
			@RequestParam(required = false) String key,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String purpose,
			@RequestParam(required = false) String fullName,
			@RequestParam(required = false) String schoolYear,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "id") String column,
			@RequestParam(defaultValue = "ASC") String direction) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		Date convertedFromDate = null;
		Date convertedToDate =  null;
		if(fromDate!=null && toDate!=null) {	
			convertedFromDate = sdf.parse(fromDate);
			convertedToDate = sdf.parse(toDate);
		}
		return service.getReportByFilter(targetId,key, district, purpose, fullName, schoolYear, convertedFromDate, convertedToDate, page, limit, column, direction);	
	}
	@GetMapping("/{reportId}")
	public ReportDTO getOne(@PathVariable int reportId) {
		return service.getOne(reportId);
	}
	@PatchMapping("/{reportId}")
	public String updateComment(@PathVariable int reportId,@RequestBody CommentRequest request) {
		service.updateComment(reportId, request);
		return "Updated";
	}
}

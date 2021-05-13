package com.app.demo.services;

import java.util.Date;
import java.util.List;

import com.app.demo.dtos.CommentRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ReportDTO;
import com.app.demo.dtos.ReportDetails;
import com.app.demo.models.Report;

public interface IReportService {
	Paging<ReportDTO> getReportByFilter(int targetId,String key,String district, String purpose, String fullName, String schoolYear,
			Date fromDate, Date toDate, int page, int limit, String column, String direction);
	String insert(List<ReportDTO> request);
	Report update(int id, ReportDetails dto);
	Report delete(int id);
	ReportDTO getOne(int id);
	void updateComment(int id,CommentRequest dto);
}

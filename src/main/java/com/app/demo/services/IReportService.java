package com.app.demo.services;

import java.util.Date;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ReportDTO;

public interface IReportService {
	Paging<ReportDTO> getReportByFilter(String key,String district, String purpose, String fullName, String schoolYear,
			Date fromDate, Date toDate, int page, int limit, String column, String direction);
}

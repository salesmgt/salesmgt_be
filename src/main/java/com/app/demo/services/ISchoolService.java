package com.app.demo.services;

import java.util.List;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.Principle;
import com.app.demo.dtos.SchoolDTO;
import com.app.demo.dtos.SchoolStatusRequest;
import com.app.demo.dtos.SchoolTimelineItem;
import com.app.demo.dtos.SuggestionSalesman;
import com.app.demo.models.SchoolType;

public interface ISchoolService {
	 Paging<SchoolDTO> getSchoolByFilter(Boolean active,String district,String status, SchoolType type, String level,String key,String schoolYear,int page, int limit, String column, String direction);
	 boolean insert(SchoolDTO dto);
	 void update(String id,SchoolDTO dto);
	 void delete(String id);
	 SchoolDTO getOne(String id);
	 List<SchoolTimelineItem> getTimeline(String schoolId);
	 String saveAll(List<SchoolDTO> dtos);
	 void updateStatus(String id, SchoolStatusRequest request);
	 void updatePrinciple(String id,Principle request);
	 Paging<SchoolDTO> getSchoolForTarget(String district, String status, String type, String level,
				String schoolYear,int page, int limit, String column, String direction);
	 List<SuggestionSalesman> getSuggestion(List<String> schoolId);
}

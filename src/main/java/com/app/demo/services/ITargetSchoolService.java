package com.app.demo.services;

import java.util.List;
import com.app.demo.dtos.DashboardDTO;
import com.app.demo.dtos.LocationCard;
import com.app.demo.dtos.NotePurposeRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.dtos.TargetDetails;
import com.app.demo.dtos.TargetTimelineItem;
import com.app.demo.dtos.TargetUpdateRequest;
import com.app.demo.models.SchoolType;

public interface ITargetSchoolService {
	Paging<TargetDTO> getTargetByFilter(Boolean assign,String status,String username,String key,String purpose,SchoolType type, String educationalLevel,String fullName, String district, String schoolYear, int page, int limit, String column, String direction);
	//void assign(int targetId, String username);
	List<String> getSchoolYear();
	List<LocationCard> getTargetSchoolName(String username, String key);
	List<String> getSchoolYearBySchoolId(int id);
	void assignMutiple(List<TargetDTO> request);
	int insert(List<TargetDTO> dto);
	String delete(int targetId);
	TargetDetails getOne(int id);
	void updateTarget(int id,TargetUpdateRequest request);
	void updatePurposeNote(int id, NotePurposeRequest request);
	void unassign(int targetId);
	DashboardDTO getDataset();
	List<TargetTimelineItem> getTimeline(int targetId);
}

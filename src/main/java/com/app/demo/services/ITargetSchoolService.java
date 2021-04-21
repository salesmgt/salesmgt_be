package com.app.demo.services;

import java.util.List;

import com.app.demo.dtos.AddTargetRequest;
import com.app.demo.dtos.LocationCard;
import com.app.demo.dtos.MutiAssignRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;

public interface ITargetSchoolService {
	Paging<TargetDTO> getTargetByFilter(String key,String purpose,SchoolType type, Level educationalLevel,Scale scale,String fullName, String district, String schoolYear, int page, int limit, String column, String direction);
	void assign(int targetId, String username);
	List<String> getSchoolYear();
	List<LocationCard> getTargetSchoolName(String username, String key);
	List<String> getSchoolYearBySchoolId(int id);
	void assignMutiple(MutiAssignRequest request);
	int insert(AddTargetRequest dto);
	void delete(int targetId);
	TargetDTO getOne(int id);
}

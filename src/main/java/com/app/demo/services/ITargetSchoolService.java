package com.app.demo.services;

import com.app.demo.dtos.TargetDTO;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;
import com.app.demo.pagination.Paging;

public interface ITargetSchoolService {
	Paging<TargetDTO> getTargetByFilter(String key,String purpose,SchoolType type, Level educationalLevel,Scale scale,String fullName,String reprName, String district, String schoolYear, int page, int limit, String column, String direction);
}

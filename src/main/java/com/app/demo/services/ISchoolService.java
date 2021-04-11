package com.app.demo.services;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.SchoolDTO;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;

public interface ISchoolService {
	 Paging<SchoolDTO> getSchoolByFilter(String district,String status, SchoolType type, Level level,Scale scale,String key,int page, int limit, String column, String direction);
	 void insert(SchoolDTO dto);
	 void update(SchoolDTO dto);
	 void delete(int id);
}

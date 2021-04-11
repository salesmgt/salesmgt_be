package com.app.demo.services;

import java.util.List;

import com.app.demo.dtos.ActivityDTO;
import com.app.demo.dtos.ActivityViewRequest;

public interface IActivityService {
	List<ActivityDTO> getActivities(ActivityViewRequest request );
	void insert(ActivityDTO dto);
	void delete(List<Integer> ids);
	void update(ActivityDTO dto, int id);
	void updatePatch(int id, String ex);

}

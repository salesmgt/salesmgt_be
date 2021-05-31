package com.app.demo.services;

import java.util.List;

import com.app.demo.dtos.KpiGroupDTO;
import com.app.demo.dtos.KpiGroupDetails;
import com.app.demo.dtos.KpiInsertObject;
import com.app.demo.dtos.KpiUserDetails;

public interface IKpiService {
	public void insert(KpiInsertObject request);
	List<KpiGroupDTO> getAll(String status,String key);
	KpiGroupDetails getOneKpiGroup(int groupId);
	KpiUserDetails getByKpiId(int kpiId);
	void setDisbale(int id);
	List<KpiGroupDTO> getGroupByUsername(String username,String status);
}

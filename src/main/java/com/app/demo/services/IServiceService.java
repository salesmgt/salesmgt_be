package com.app.demo.services;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ServiceDTO;

public interface IServiceService {
	void insert(ServiceDTO dto);
	void update(int id, ServiceDTO dto);
	Paging<ServiceDTO> getServiceByFilter(String status,String schoolName,String serviceType, String username,String key,
			String schoolYear,int page, int limit, String column, String direction);
}

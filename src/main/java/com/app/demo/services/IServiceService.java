package com.app.demo.services;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ServiceDTO;

public interface IServiceService {
	void insert(ServiceDTO dto);
	void update(int id, ServiceDTO dto);
	Paging<ServiceDTO> getServiceByFilter(Boolean isExpired,String status,String username,String serviceType,String key,
			String schoolYear,int page, int limit, String column, String direction);
	void approve(int id);
	void reject(int id,String content);
	ServiceDTO getOne(int id);
}

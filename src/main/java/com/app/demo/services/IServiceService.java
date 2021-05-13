package com.app.demo.services;

import com.app.demo.dtos.ServiceDTO;

public interface IServiceService {
	void insert(ServiceDTO dto);
	void update(int id, ServiceDTO dto);
}

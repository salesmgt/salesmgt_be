package com.app.demo.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.ServiceType;
import com.app.demo.repositories.ServiceTypeRepository;
import com.app.demo.services.IServiceTypeService;

@Service
public class ServiceTypeServiceImpl implements IServiceTypeService {

	@Autowired
	private ServiceTypeRepository repo;
	
	public List<String> getList(){
		List<ServiceType> entities = repo.findAll();
		List<String> names = entities.stream().map(ServiceType::getName).collect(Collectors.toList());
		return names;
	}
}

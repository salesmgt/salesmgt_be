package com.app.demo.services.impls;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.District;
import com.app.demo.repositories.DistrictRepository;
import com.app.demo.services.IDistrictService;
@Service
public class DistrictServiceImpl implements IDistrictService{
	@Autowired
	private DistrictRepository repo;
	
	public List<String> getDistricts(){
		List<District> entities = repo.findAll();
		List<String> names = entities.stream().map(District::getName).collect(Collectors.toList());
		return names;
	}

}

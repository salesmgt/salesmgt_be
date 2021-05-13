package com.app.demo.services.impls;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.demo.dtos.ServiceDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.TargetSchool;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.ServiceTypeRepository;
import com.app.demo.repositories.TargetRepository;
import com.app.demo.services.IServiceService;

@Service
public class ServiceImpl implements IServiceService {

	@Autowired
	ServiceRepository repo; 
	@Autowired
	TargetRepository targetRepo;
	@Autowired
	ServiceTypeRepository typeRepo;
	
	@Override
	public void update(int id, ServiceDTO dto) {
		com.app.demo.models.Service entity = repo.getOne(id);
		entity.setApproveDate(dto.getApproveDate());
		entity.setClassNumber(dto.getClassNumber());
		entity.setEndDate(dto.getEndDate());
		entity.setStartDate(dto.getStartDate());
		entity.setNote(dto.getNote());
		entity.setServiceType(typeRepo.findByName(dto.getServiceType()));
		entity.setId(id);
		repo.save(entity);
	}
	@Override
	public void insert(ServiceDTO dto) {
		com.app.demo.models.Service entity = Mapper.getMapper().map(dto, com.app.demo.models.Service.class);
		TargetSchool target = targetRepo.getOne(dto.getTargetSchoolId());
		entity.setTargetSchool(target);
		repo.save(entity);
	}
	public void approve(int id) {
		Optional<com.app.demo.models.Service> entity = repo.findById(id);
		
	}
	
}

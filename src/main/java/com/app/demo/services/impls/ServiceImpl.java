package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ServiceDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.School;
import com.app.demo.models.School_;
import com.app.demo.models.ServiceType;
import com.app.demo.models.ServiceType_;
import com.app.demo.models.Service_;
import com.app.demo.models.Task;
import com.app.demo.models.Task_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.ServiceTypeRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.services.IServiceService;

@Service
public class ServiceImpl implements IServiceService {

	@Autowired
	ServiceRepository repo; 
	@Autowired
	TaskRepository targetRepo;
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
		Task target = targetRepo.getOne(dto.getTargetSchoolId());
		entity.setTask(target);
		repo.save(entity);
	}
	public void approve(int id) {
		Optional<com.app.demo.models.Service> entity = repo.findById(id);
		
	}
	
	private Pageable paging(int page, int limit, String column, String direction) {
		Pageable paging;
		if (direction.equalsIgnoreCase("DESC")) {
			paging = PageRequest.of(page, limit, Sort.by(column).descending());
		}
		else {
			paging = PageRequest.of(page, limit, Sort.by(column).ascending());
		}
		return paging;
	}
	
	@Override
	public Paging<ServiceDTO> getServiceByFilter(String status,String schoolName,String serviceType, String username,String key,
												String schoolYear,int page, int limit, String column, String direction){
	Page<com.app.demo.models.Service> pageEntities = (Page<com.app.demo.models.Service>)repo.findAll((Specification<com.app.demo.models.Service>) (root, query,criteriaBuilder) -> {
		Join<com.app.demo.models.Service,Task> service_target = root.join(Service_.TASK);
		Join<Task,School> service_target_school = service_target.join(Task_.SCHOOL);
		Join<Task,User> service_target_user = service_target.join(Task_.USER);
		Join<com.app.demo.models.Service,ServiceType> service_type = root.join(Service_.SERVICE_TYPE);
		Predicate p = criteriaBuilder.conjunction();
		if (!ObjectUtils.isEmpty(key)) {
			Predicate school =  criteriaBuilder.like(service_target_school.get(School_.NAME), "%" + key + "%");
			Predicate fullName =  criteriaBuilder.like(service_target_user.get(User_.FULL_NAME), "%" + key + "%");
			p = criteriaBuilder.or(school,fullName);
		}
		if (!ObjectUtils.isEmpty(status)) {
			p = criteriaBuilder.and(p,criteriaBuilder.equal(root.get(Service_.STATUS), status));
		}
		if (!ObjectUtils.isEmpty(schoolYear)) {
			p = criteriaBuilder.and(p,criteriaBuilder.equal(service_target.get(Task_.SCHOOL_YEAR), schoolYear));
		}
		if (!ObjectUtils.isEmpty(serviceType)) {
			p = criteriaBuilder.and(p,criteriaBuilder.equal(service_type.get(ServiceType_.NAME), serviceType));
		}
		return p;
	}, paging(page, limit, column, direction));
	List<ServiceDTO> results = new ArrayList<ServiceDTO>();
	Paging<ServiceDTO> servicePage = new Paging<ServiceDTO>();
	if (pageEntities.hasContent()) {
		pageEntities.getContent().forEach(service -> {
			ServiceDTO dto = Mapper.getMapper().map(service, ServiceDTO.class);
			
			results.add(dto);
			});
		servicePage.setList(results);
		servicePage.setTotalElements(pageEntities.getTotalElements());
		servicePage.setTotalPage(pageEntities.getTotalPages());
	}
	return servicePage;	
	}	
}

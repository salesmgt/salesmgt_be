package com.app.demo.services.impls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.DashboardColumnObject;
import com.app.demo.dtos.DashboardDTO;
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
import com.app.demo.repositories.SchoolRepository;
import com.app.demo.repositories.SchoolStatusRepository;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.ServiceTypeRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.services.IServiceService;

@Service
public class ServiceImpl implements IServiceService {

	@Autowired
	private ServiceRepository repo;
	@Autowired
	private SchoolStatusRepository statusRepo;
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private TaskRepository targetRepo;
	@Autowired
	private ServiceTypeRepository typeRepo;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void update(int id, ServiceDTO dto) {
		try {
			SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			sdff.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
			com.app.demo.models.Service entity = repo.getOne(id);
			entity.setPricePerSlot(dto.getPricePerSlot());
			entity.setSlotNumber(dto.getSlotNumber());
			entity.setStudentNumber(dto.getStudentNumber());
			entity.setClassNumber(dto.getClassNumber());
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
			entity.setStartDate(sdff.parse(dto.getStartDate().substring(0, 10)));
			Date end = sdff.parse(dto.getEndDate().substring(0, 10));
			entity.setEndDate(end);
			entity.setNote(dto.getNote());
			String strNowDate = sdf.format(new Date());
			Date now = sdf.parse(strNowDate);
			if (now.compareTo(end) > 0)
				entity.setExpired(true);
			else
				entity.setExpired(false);
			entity.setSubmitDate(now);
			entity.setServiceType(typeRepo.findByName(dto.getServiceType()));
			entity.setId(id);
			repo.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insert(ServiceDTO dto) {
		com.app.demo.models.Service entity = Mapper.getMapper().map(dto, com.app.demo.models.Service.class);
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		//sdff.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		entity.setApproveDate(null);
		try {
			entity.setStartDate(sdff.parse(dto.getStartDate().substring(0, 10)));
			Date end = sdff.parse(dto.getEndDate().substring(0, 10));
			entity.setEndDate(end);
			String strNowDate = sdff.format(new Date());
			Date now = sdff.parse(strNowDate);
			Date submit = sdf.parse(dto.getSubmitDate());
			entity.setSubmitDate(submit);
			if (now.compareTo(end) > 0)
				entity.setExpired(true);
			else
				entity.setExpired(false);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ServiceType type = typeRepo.findByName(dto.getServiceType());
		entity.setServiceType(type);
		Task target = targetRepo.getOne(dto.getTaskId());
		entity.setTask(target);
		entity.setStatus("pending");
		repo.save(entity);
	}

	public void approve(int id) {
		com.app.demo.models.Service entity = repo.getOne(id);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		String strNowDate = sdf.format(new Date());
		try {
			entity.setApproveDate(sdf.parse(strNowDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Task task = entity.getTask();
		entity.setStatus("approved");
		repo.save(entity);
		task.setResult("successful");
		task.setCompletedDate(new Date());
		targetRepo.save(task);
		School school = task.getSchool();
		school.setSchoolStatus(statusRepo.findByName("Đang hợp tác"));
		schoolRepo.save(school);
		
	}

	public void reject(int id, String content) {
		com.app.demo.models.Service entity = repo.getOne(id);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		String strNowDate = sdf.format(new Date());
		try {
			entity.setApproveDate(sdf.parse(strNowDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		entity.setStatus("rejected");
		entity.setRejectedReason(content);
		repo.save(entity);
	}

	private Pageable paging(int page, int limit, String column, String direction) {
		Pageable paging;
		if (direction.equalsIgnoreCase("DESC")) {
			paging = PageRequest.of(page, limit, Sort.by(column).descending());
		} else {
			paging = PageRequest.of(page, limit, Sort.by(column).ascending());
		}
		return paging;
	}

	@Override
	public Paging<ServiceDTO> getServiceByFilter(Boolean isExpired, String status, String username, String serviceType,
			String key, String schoolYear, int page, int limit, String column, String direction) {
		Page<com.app.demo.models.Service> pageEntities = (Page<com.app.demo.models.Service>) repo
				.findAll((Specification<com.app.demo.models.Service>) (root, query, criteriaBuilder) -> {
					Join<com.app.demo.models.Service, Task> service_target = root.join(Service_.TASK);
					Join<Task, School> service_target_school = service_target.join(Task_.SCHOOL);
					Join<Task, User> service_target_user = service_target.join(Task_.USER);
					Join<com.app.demo.models.Service, ServiceType> service_type = root.join(Service_.SERVICE_TYPE);
					Predicate p = criteriaBuilder.conjunction();
					if (!ObjectUtils.isEmpty(key)) {
						Predicate school = criteriaBuilder.like(service_target_school.get(School_.NAME),
								"%" + key + "%");
						Predicate name = criteriaBuilder.like(service_target_user.get(User_.FULL_NAME),
								"%" + key + "%");
						Predicate username1 = criteriaBuilder.like(service_target_user.get(User_.USERNAME),
								"%" + key + "%");
						p = criteriaBuilder.or(school, name, username1);
					}
					if (!ObjectUtils.isEmpty(status)) {
						p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get(Service_.STATUS), status));
					}

					if (!ObjectUtils.isEmpty(schoolYear)) {
						p = criteriaBuilder.and(p,
								criteriaBuilder.equal(service_target.get(Task_.SCHOOL_YEAR), schoolYear));
					}
					if (!ObjectUtils.isEmpty(serviceType)) {
						p = criteriaBuilder.and(p,
								criteriaBuilder.equal(service_type.get(ServiceType_.NAME), serviceType));
					}
					if (!ObjectUtils.isEmpty(isExpired)) {
						if (isExpired)
							p = criteriaBuilder.and(p, criteriaBuilder.isTrue(root.get(Service_.IS_EXPIRED)));
						else
							p = criteriaBuilder.and(p, criteriaBuilder.isFalse(root.get(Service_.IS_EXPIRED)));
					}
					if (!ObjectUtils.isEmpty(username)) {
						p = criteriaBuilder.and(p,
								criteriaBuilder.equal(service_target_user.get(User_.USERNAME), username));
					}
					return p;
				}, paging(page, limit, column, direction));
		List<ServiceDTO> results = new ArrayList<ServiceDTO>();
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		
		Paging<ServiceDTO> servicePage = new Paging<ServiceDTO>();
		if (pageEntities.hasContent()) {
			pageEntities.getContent().forEach(service -> {
				ServiceDTO dto = Mapper.getMapper().map(service, ServiceDTO.class);
				dto.setEndDate(sdf.format(service.getEndDate()));
				dto.setStartDate(sdf.format(service.getStartDate()));
				dto.setServiceType(service.getServiceType().getName());
				dto.setAvatar(service.getTask().getUser().getAvatar());
				dto.setFullName(service.getTask().getUser().getFullName());
				dto.setUsername(service.getTask().getUser().getUsername());
//			dto.setUsername(service.getTask().getUser().getUsername());)
				dto.setSchoolName(service.getTask().getSchool().getName());
				dto.setEducationLevel(service.getTask().getSchool().getEducationalLevel().getName());
				results.add(dto);
			});
			servicePage.setList(results);
			servicePage.setTotalElements(pageEntities.getTotalElements());
			servicePage.setTotalPage(pageEntities.getTotalPages());
		}
		return servicePage;
	}

	@Override
	public ServiceDTO getOne(int id) {
		com.app.demo.models.Service entity = repo.getOne(id);
		ServiceDTO dto = Mapper.getMapper().map(entity, ServiceDTO.class);
		dto.setEndDate(sdf.format(entity.getEndDate()));
		dto.setStartDate(sdf.format(entity.getStartDate()));
		dto.setUsername(entity.getTask().getUser().getUsername());
		dto.setFullName(entity.getTask().getUser().getFullName());
		dto.setAvatar(entity.getTask().getUser().getAvatar());
		dto.setServiceType(entity.getServiceType().getName());
		dto.setSchoolName(entity.getTask().getSchool().getName());
		dto.setEducationLevel(entity.getTask().getSchool().getEducationalLevel().getName());
		dto.setAddress(entity.getTask().getSchool().getAddress());
		return dto;
	}
	@Override
	public List<?> getServiceForDashBoard(String name){
		List<String> types =  getType();
		List<String> years = getSchoolYear();
		List<DashboardColumnObject> result = new ArrayList<>();
		switch (name) {
		case "SL":
			for (String type : types) {
				DashboardColumnObject dto = calculateSoLuong(type, years);
				result.add(dto);
			}
			break;
		case "DS":
			for (String type : types) {
				DashboardColumnObject dto = calculateDoanhSo(type, years);
				result.add(dto);
			}
			break;

		default:
			break;
		}
		return result;
	}
	public List<String> getSchoolYear(){
		return targetRepo.getSchoolYears();
}
	private List<String> getType(){
		List<ServiceType> entities = typeRepo.findAll();
		List<String> names = entities.stream().map(ServiceType::getName).collect(Collectors.toList());
		return names;
	}
	private DashboardColumnObject calculateSoLuong(String type, List<String> years) {
		List<Double> list = new ArrayList<>();
		for (String year : years) {
			List<com.app.demo.models.Service> services =  repo.findAll((root, query, criteriaBuilder) -> {
				Join<com.app.demo.models.Service, Task> service_task = root.join(Service_.TASK);
				Join<com.app.demo.models.Service, ServiceType> service_type = root.join(Service_.SERVICE_TYPE);
				Predicate p = criteriaBuilder.conjunction();
				p = criteriaBuilder.and(p,
						criteriaBuilder.equal(service_type.get(ServiceType_.NAME), type));
				p = criteriaBuilder.and(p,
						criteriaBuilder.equal(service_task.get(Task_.SCHOOL_YEAR), year));
				p = criteriaBuilder.and(p,
						criteriaBuilder.equal(root.get(Service_.STATUS), "approved"));
				return p;
		});
			double value = services.size();
			list.add(value);
		}
		 DashboardColumnObject result = new DashboardColumnObject(type,list);
		return result;
	}
	private DashboardColumnObject calculateDoanhSo(String type, List<String> years) {
		List<Double> list = new ArrayList<>();
		for (String year : years) {
			List<com.app.demo.models.Service> services =  repo.findAll((root, query, criteriaBuilder) -> {
				Join<com.app.demo.models.Service, Task> service_task = root.join(Service_.TASK);
				Join<com.app.demo.models.Service, ServiceType> service_type = root.join(Service_.SERVICE_TYPE);
				Predicate p = criteriaBuilder.conjunction();
				p = criteriaBuilder.and(p,
						criteriaBuilder.equal(service_type.get(ServiceType_.NAME), type));
				p = criteriaBuilder.and(p,
						criteriaBuilder.equal(service_task.get(Task_.SCHOOL_YEAR), year));
				p = criteriaBuilder.and(p,
						criteriaBuilder.equal(root.get(Service_.STATUS), "approved"));
				return p;
		});
			double value = 0;
			if(services.size()>0)
			for (com.app.demo.models.Service service : services) {
				 int days = Days.daysBetween(new DateTime(service.getStartDate()),
						 new DateTime(service.getEndDate())).getDays();
				 double week = Math.ceil(days/7);
				value = value +service.getPricePerSlot()*service.getSlotNumber() * service.getClassNumber()*week;	
			}
			list.add(value);
	}
		 DashboardColumnObject result = new DashboardColumnObject(type,list);
			return result;
	}
}

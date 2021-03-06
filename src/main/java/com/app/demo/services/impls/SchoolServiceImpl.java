package com.app.demo.services.impls;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.DashboardDTO;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.Principle;
import com.app.demo.dtos.SchoolDTO;
import com.app.demo.dtos.SchoolStatusRequest;
import com.app.demo.dtos.SchoolTimelineItem;
import com.app.demo.dtos.SuggestionSalesman;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.SchoolStatus;
import com.app.demo.models.SchoolStatus_;
import com.app.demo.models.School;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.EducationalLevel;
import com.app.demo.models.EducationalLevel_;
import com.app.demo.models.SchoolType;
import com.app.demo.models.School_;
import com.app.demo.models.Task;
import com.app.demo.models.Task_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.DistrictRepository;
import com.app.demo.repositories.EducationalLevelRepository;
import com.app.demo.repositories.SchoolRepository;
import com.app.demo.repositories.SchoolStatusRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.ISchoolService;
import com.app.demo.utils.DistanceCalculateUtils;
import com.app.demo.utils.VNCharacterUtils;

@Service
public class SchoolServiceImpl implements ISchoolService {

	@Autowired
	private SchoolRepository repo;

	@Autowired
	private EducationalLevelRepository eduRepo;

	@Autowired
	private TaskRepository targetRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DistrictRepository districtRepo;

	@Autowired
	private SchoolStatusRepository statusRepo;

	/**
	 * Method c?? ch???c n??ng t???o ?????i t?????ng Pageable b???ng c??ch x??c ?????nh chi???u sort c???a
	 * Page (DES/ASC).
	 * 
	 * @param page: v??? tr?? page, limit: s??? item tr??n 1 page, column: thu???c t??nh ?????
	 *              sort, direction: chi???u sort.
	 * @return ?????i t?????ng Pageable b???ng ph????ng th???c PageRequest.of(). Pageable ???????c
	 *         k??? th???a t??? l???p Page.
	 * 
	 * @author Nguyen Hoang Gia
	 * @version 1.0
	 * @since 3/7/2021
	 */
	private Pageable paging(int page, int limit, String column, String direction) {
		Pageable paging;
		if (direction.equalsIgnoreCase("DESC")) {
			paging = PageRequest.of(page, limit, Sort.by(column).descending());
		} else {
			paging = PageRequest.of(page, limit, Sort.by(column).ascending());
		}
		return paging;
	}

	/**
	 * Method ph???c h???p d??ng ????? x??? l?? logic filter v?? ph??n trang.
	 * 
	 * @param district: "qu???n 1", status: "??ang h???p t??c", type="CONG_LAP",
	 *                  educationLevel: "THCS",scale: "Nh???".
	 * @param key:      keyword text ????? search g???n gi???ng, c?? th??? thu???c c??c field
	 *                  (phone,name,address,description).
	 * @param page:     v??? tr?? page, limit: s??? item tr??n 1 page, column: thu???c t??nh
	 *                  ????? sort, direction: chi???u sort.
	 * @return ?????i t?????ng Paging ???? ???????c filter v?? ph??n trang.
	 * @implSpec imlplements interface Specification g???m 3 param (root,
	 *           criteriaQuery, criteriaBuilder) ????? custom ph????ng th???c getAll() c???a
	 *           HQL/JPA b???ng Predicate - m???nh ????? ??i???u ki???n.
	 * @implSpec School_, District_ l?? nh???ng class ???????c generate auto l??c build
	 *           th??ng qua hibernate-jpamodelgen (xem pom.xml). detail in
	 *           "target/generated-source/"
	 * 
	 * @author Nguy???n Ho??ng Gia
	 * @version 1.0
	 * @since 3/7/2021
	 */
	@Override
	public Paging<SchoolDTO> getSchoolByFilter(Boolean active, String district, String status, SchoolType type,
			String educationalLevel, String key, String schoolYear, int page, int limit, String column,
			String direction) {
		Page<School> pageEntities = (Page<School>) repo
				.findAll((Specification<School>) (root, query, criteriaBuilder) -> {
					Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
					Join<School, District> school = root.join(School_.DISTRICT);
					Join<School, EducationalLevel> edu = root.join(School_.EDUCATIONAL_LEVEL);

					Predicate p = criteriaBuilder.conjunction();
					if (!ObjectUtils.isEmpty(key)) {
						Predicate phone = criteriaBuilder.like(root.get(School_.PHONE), "%" + key + "%");
						Predicate name = criteriaBuilder.like(root.get(School_.NAME), "%" + key + "%");
						Predicate address = criteriaBuilder.like(root.get(School_.ADDRESS), "%" + key + "%");
						Predicate reprName = criteriaBuilder.like(root.get(School_.REPR_NAME), "%" + key + "%");
						Predicate reprPhone = criteriaBuilder.like(root.get(School_.REPR_PHONE), "%" + key + "%");
						p = criteriaBuilder.or(phone, name, address, reprName, reprPhone);
					}
					if (!ObjectUtils.isEmpty(educationalLevel)) {
						p = criteriaBuilder.and(p,criteriaBuilder.like(edu.get(EducationalLevel_.NAME), "%" + educationalLevel + "%"));
					}
					if (!ObjectUtils.isEmpty(type)) {
						p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get(School_.TYPE), type));
					}

					if (!ObjectUtils.isEmpty(status)) {
					
						p = criteriaBuilder.and(p,
								criteriaBuilder.equal(school_status.get(SchoolStatus_.NAME), status));
					}
					if (!ObjectUtils.isEmpty(district)) {
						p = criteriaBuilder.and(p, criteriaBuilder.equal(school.get(District_.NAME), district));
					}
					if (!ObjectUtils.isEmpty(active)) {
						if (active)
							p = criteriaBuilder.and(p, criteriaBuilder.isTrue(root.get(School_.IS_ACTIVE)));
						else
							p = criteriaBuilder.and(p, criteriaBuilder.isFalse(root.get(School_.IS_ACTIVE)));
					}
					query.distinct(true);
					return p;
				}, paging(page, limit, column, direction));
		List<SchoolDTO> results = new ArrayList<SchoolDTO>();
		Paging<SchoolDTO> schoolPage = new Paging<SchoolDTO>();
		if (pageEntities.hasContent()) {
			pageEntities.getContent().forEach(school -> {
				SchoolDTO dto = Mapper.getMapper().map(school, SchoolDTO.class);
				dto.setDistrict(school.getDistrict().getName());
				dto.setStatus(school.getSchoolStatus().getName());
				dto.setEducationalLevel(school.getEducationalLevel().getName());
				dto.setType(school.getType().getValues());
				dto.setSchoolId(school.getSchoolId());
				results.add(dto);
			});
			schoolPage.setList(results);
			schoolPage.setTotalElements(pageEntities.getTotalElements());
			schoolPage.setTotalPage(pageEntities.getTotalPages());
		}
		return schoolPage;
	}

	/**
	 * This method is used to insert school data into database.
	 * 
	 * @param dto: SchoolDTO object.
	 * @return boolean (true - success/ false - exception)
	 * @throws SQLIntegrityConstraintViolationException
	 */
	@Override
	public boolean insert(SchoolDTO dto) {
		School entity = Mapper.getMapper().map(dto, School.class);
		entity.setType(SchoolType.valueOfLabel(dto.getType()));
		EducationalLevel level = eduRepo.findByName(dto.getEducationalLevel());
		entity.setEducationalLevel(level);
		District district = districtRepo.findByName(dto.getDistrict());
		entity.setDistrict(district);
		entity.setSchoolStatus(statusRepo.findByName("Ch??a h???p t??c"));
		entity.setActive(true);
		String id = VNCharacterUtils.removeAccent(dto.getName()).toUpperCase().trim().replaceAll("\\s+", "")
				+ district.getId() + level.getId();
		System.out.println(id);
		if (!ObjectUtils.isEmpty(repo.findById(id)))
			return false;

		entity.setSchoolId(id);
		repo.save(entity);
		return true;
	}

	@Override
	public void update(String id, SchoolDTO dto) {
		School entity = repo.getOne(id);
		if (!ObjectUtils.isEmpty(entity)) {
			entity = Mapper.getMapper().map(dto, School.class);
			entity.setType(SchoolType.valueOfLabel(dto.getType()));
			entity.setSchoolId(id);
			entity.setEducationalLevel(eduRepo.findByName(dto.getEducationalLevel()));
			entity.setDistrict(districtRepo.findByName(dto.getDistrict()));
			entity.setSchoolStatus(statusRepo.findByName(dto.getStatus()));
			repo.save(entity);
		}
	}

	/**
	 * This method is used to delete a school by id - update isActive(false) column
	 * in database.
	 * 
	 * @param int id: id of school.
	 * @exception if fail, throw the exception at controller to process.
	 */
	@Override
	public void delete(String id) {
		School entity = repo.getOne(id);
		entity.setActive(false);
		repo.save(entity);
	}

	@Override
	public SchoolDTO getOne(String id) {
		School school = repo.getOne(id);
		SchoolDTO dto = Mapper.getMapper().map(school, SchoolDTO.class);
		dto.setDistrict(school.getDistrict().getName());
		dto.setStatus(school.getSchoolStatus().getName());
		dto.setEducationalLevel(school.getEducationalLevel().getName());
		dto.setType(school.getType().getValues());
		return dto;
	}

	@Override
	public String saveAll(List<SchoolDTO> dtos) {
		List<School> entities = new ArrayList<>();
		for (SchoolDTO dto : dtos) {

			School entity = Mapper.getMapper().map(dto, School.class);
			entity.setType(SchoolType.valueOfLabel(dto.getType()));
			EducationalLevel level = eduRepo.findByName(dto.getEducationalLevel());
			entity.setEducationalLevel(level);
			District district = districtRepo.findByName(dto.getDistrict());
			entity.setDistrict(district);
			entity.setSchoolStatus(statusRepo.findByName("Ch??a h???p t??c"));
			entity.setActive(true);
			String id = VNCharacterUtils.removeAccent(dto.getName()).toUpperCase().trim().replaceAll("\\s+", "")
					+ district.getId() + level.getId();
			Optional<School> dupplicate = repo.findById(id);
			if (dupplicate.isPresent()) {
				return "This school [" + dupplicate.get().getName() + "] have already exsit";
			}
			entity.setSchoolId(id);
			entities.add(entity);
		}
		;
		return "Added " + repo.saveAll(entities).size() + " schools";
	}

	@Override
	public Paging<SchoolDTO> getSchoolForTarget(String key,String district, String status, String type, String level,
			String schoolYear, int page, int limit, String column, String direction) {
		Page<School> pageEntities = (Page<School>) repo.findAll((Specification<School>) (root, query, builder) -> {
			Join<School, Task> target_school = root.join(School_.TASKS, JoinType.LEFT);
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Join<School, District> school = root.join(School_.DISTRICT);
			Join<School, EducationalLevel> school_level = root.join(School_.EDUCATIONAL_LEVEL);
			Predicate p = builder.conjunction();
			ArrayList<School> list = new ArrayList<School>();
			for (Task element : targetRepo.findBySchoolYear(schoolYear)) {
				list.add(element.getSchool());
			}
			Predicate listNot = builder.or(target_school.get(Task_.SCHOOL).in(list).not());
			Predicate year = builder.isNull(target_school.get(Task_.SCHOOL_YEAR));
			p = builder.or(listNot, year);
			if (!ObjectUtils.isEmpty(key)) {
				Predicate name = builder.like(root.get(School_.NAME), "%" + key + "%");
				Predicate address = builder.like(root.get(School_.ADDRESS), "%" + key + "%");
				Predicate reprName = builder.like(root.get(School_.REPR_NAME), "%" + key + "%");
				p = builder.and(p, builder.or( name, address, reprName));
			}
			if (!ObjectUtils.isEmpty(district))
				p = builder.and(p, builder.equal(school.get(District_.NAME), district));
			if (!ObjectUtils.isEmpty(status)) {
					if(status.equalsIgnoreCase("Ch??a h???p t??c")) {
						Predicate chuaHopTac = builder.equal(school_status.get(SchoolStatus_.NAME), status);
						Predicate tiemNang = builder.equal(school_status.get(SchoolStatus_.NAME), "Ti???m n??ng");
						p = builder.and(p, builder.or(chuaHopTac,tiemNang));
					}
					else
					p = builder.and(p,
							builder.equal(school_status.get(SchoolStatus_.NAME), status));
				}
			if (!ObjectUtils.isEmpty(level))
				p = builder.and(p, builder.like(school_level.get(EducationalLevel_.NAME), "%" + level + "%"));
			if (!ObjectUtils.isEmpty(type))
				p = builder.and(p, builder.equal(root.get(School_.TYPE), SchoolType.valueOfLabel(type)));
			p = builder.and(p, builder.isTrue(root.get(Task_.IS_ACTIVE)));
			query.distinct(true);
			return p;
		}, paging(page, limit, column, direction));
		List<SchoolDTO> results = new ArrayList<SchoolDTO>();
		Paging<SchoolDTO> schoolPage = new Paging<SchoolDTO>();
		if (pageEntities.hasContent()) {
			pageEntities.getContent().forEach(school -> {
				SchoolDTO dto = Mapper.getMapper().map(school, SchoolDTO.class);
				dto.setDistrict(school.getDistrict().getName());
				dto.setStatus(school.getSchoolStatus().getName());
				dto.setEducationalLevel(school.getEducationalLevel().getName());
				dto.setType(school.getType().getValues());
				results.add(dto);
			});
			schoolPage.setList(results);
			schoolPage.setTotalElements(pageEntities.getTotalElements());
			schoolPage.setTotalPage(pageEntities.getTotalPages());
		}
		return schoolPage;
	}

	@Override
	public void updateStatus(String id, SchoolStatusRequest request) {
		School school = repo.getOne(id);
		if (!school.getSchoolStatus().getName().equalsIgnoreCase(request.getSchoolStatus())) {
			school.setSchoolStatus(statusRepo.findByName(request.getSchoolStatus()));
			repo.save(school);
		}
	}

	@Override
	public void updatePrinciple(String id, Principle request) {
		School school = repo.getOne(id);
		if (!ObjectUtils.isEmpty(request.getReprName())) {
			school.setReprPhone(request.getReprPhone());
			school.setReprEmail(request.getReprEmail());
			school.setReprIsMale(request.getReprIsMale());
			school.setReprName(request.getReprName());
			repo.save(school);
		}
	}

	@Override
	public List<SchoolTimelineItem> getTimeline(String schoolId) {
		List<Task> targets = targetRepo.findBySchoolId(schoolId);
		List<SchoolTimelineItem> list = new ArrayList<>();
		for (Task item : targets) {
			SchoolTimelineItem timeline = new SchoolTimelineItem();
			timeline.setTaskId(item.getId());
			timeline.setSchoolYear(item.getSchoolYear());
			timeline.setPurpose(item.getPurpose().getName());
			timeline.setResult(item.getResult());
			if (!ObjectUtils.isEmpty(item.getUser())) {
				timeline.setFullName(item.getUser().getFullName());
				timeline.setAvatar(item.getUser().getAvatar());
			}
			if (!ObjectUtils.isEmpty(item.getServices())) {
				String serviceName = null;
				for (com.app.demo.models.Service service : item.getServices()) {
					if (service.getStatus() == "approved")
						if (serviceName == null)
							serviceName = service.getServiceType().getName();
						else
							serviceName = serviceName + service.getServiceType().getName();
				}
				timeline.setServices(serviceName);
			}
			list.add(timeline);
		}
		Collections.sort(list);
		return list;
	}

	@Override
	public List<SuggestionSalesman> getSuggestion(List<String> schoolIds) {
		List<SuggestionSalesman> salesmen = null;

		if (schoolIds.size() == 1) {
			School school = repo.getOne(schoolIds.get(0));
			salesmen = getSuggestionOne(school, salesmen);
		}	else
			salesmen = suggestMutipleSchool(schoolIds);
		return  salesmen.size()>=5 ? salesmen.subList(0, 5) : salesmen ;
	}

	private double setPoint(double point, int number) {
		double finalPoint = 0;
		if (number <= 5)
			finalPoint = point + 3;
		else if (number <= 20)
			finalPoint = point + 1;
		else if (number <= 49)
			finalPoint = point + .5;
		return finalPoint;
	}

	private String getCurrentYear() {
		int year = Year.now().getValue();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String yearStr;
		if (month > 4)
			yearStr = String.valueOf(year) + "-" + String.valueOf(year + 1);
		else
			yearStr = String.valueOf(year - 1) + "-" + String.valueOf(year);
		return yearStr;
	}

	private List<SuggestionSalesman> getExpSalesman(School school) {
		List<Task> tasks = targetRepo.findAll((Specification<Task>) (root, query, builder) -> {
			Join<Task, User> task_user = root.join(Task_.USER);
			Join<Task, School> task_school = root.join(Task_.SCHOOL);
			Predicate p = builder.conjunction();
			p = builder.and(p, builder.isTrue(task_user.get(User_.ACTIVE)));
			p = builder.and(p, builder.equal(task_school.get(School_.SCHOOL_ID), school.getSchoolId()));
			query.distinct(true);
			return p;
		});
		List<SuggestionSalesman> list = new ArrayList<>();
		for (Task task : tasks) {
			boolean dup = false;
			for (SuggestionSalesman item : list) {
				if (item.getUsername().equalsIgnoreCase(task.getUser().getUsername())) {
					item.setContent(item.getContent() + " + " + task.getSchoolYear());
					item.setPoint(item.getPoint()+1);
					dup = true;
					break;
				}
			}
			if (!dup) {
				User user = task.getUser();
				SuggestionSalesman saleman = new SuggestionSalesman(task.getUser().getUsername(),
						task.getUser().getAvatar(), task.getUser().getAddress(), task.getUser().getFullName(), 0,
						"Experience at "+task.getSchool().getName()+" "+task.getSchoolYear(), 0, 4);
				saleman.setDistance(DistanceCalculateUtils.calculate(school.getLatitude(), school.getLongitude(),
						user.getLatitude(), user.getLongitude()));
				int number = targetRepo.countByUsernameAndSchoolYear(getCurrentYear(), user.getUsername());
				saleman.setPoint(setPoint(saleman.getPoint(), number));
				saleman.setNumberOfTask(number);
				list.add(saleman);
			}
		}
		return list;
	}
	
	private List<SuggestionSalesman> getSuggestionOne(School school,List<SuggestionSalesman> salesmen){
	List<SuggestionSalesman> exp = getExpSalesman(school);
	if (!ObjectUtils.isEmpty(exp))
		salesmen = exp;
	else
		salesmen = new ArrayList<SuggestionSalesman>();
	String address = school.getAddress();
	String ward = address.substring(address.indexOf(", ") + 2);
	List<User> list = userRepo.findByAddressContains(ward, "SALESMAN", true);
	if (!ObjectUtils.isEmpty(list)) {
		for (User user : list) {
			boolean dup = false;
			for (SuggestionSalesman user2 : salesmen) {
				if (user2.getUsername().equalsIgnoreCase(user.getUsername())) {
					user2.setContent(user2.getContent() + ", " + "In Ward");
					user2.setPoint(user2.getPoint() + 3);
					dup = true;
					break;
				}
			}
			if (!dup) {
				SuggestionSalesman saleman = new SuggestionSalesman(user.getUsername(), user.getAvatar(),
						user.getAddress(), user.getFullName(), 0, "In Ward", 0, 3);
				saleman.setDistance(DistanceCalculateUtils.calculate(school.getLatitude(),
						school.getLongitude(), user.getLatitude(), user.getLongitude()));
				int number = targetRepo.countByUsernameAndSchoolYear(getCurrentYear(), user.getUsername());
				saleman.setPoint(setPoint(saleman.getPoint(), number));
				saleman.setNumberOfTask(number);
				salesmen.add(saleman);
			}
		}
	}
	if (salesmen.size() < 5) {
		String district = ward.substring(ward.indexOf(", ") + 2);
		list = userRepo.findByAddressContains(district+",", "SALESMAN", true);
		if (!ObjectUtils.isEmpty(list)) {
			for (User user : list) {
				if (salesmen.size() > 5)
					break;
				boolean dup = false;
				for (SuggestionSalesman user2 : salesmen) {
					if (user2.getUsername().equalsIgnoreCase(user.getUsername())) {
						if (!user2.getContent().contains("In Ward")) {
							user2.setContent(user2.getContent() + ", " + "In District");
							user2.setPoint(user2.getPoint() + 2);
						}
						dup = true;
						break;
					}
				}
				if (dup == false) {
					SuggestionSalesman saleman = new SuggestionSalesman(user.getUsername(), user.getAvatar(),
							user.getAddress(), user.getFullName(), 0, "In District", 0, 2);
					saleman.setDistance(DistanceCalculateUtils.calculate(school.getLatitude(),
							school.getLongitude(), user.getLatitude(), user.getLongitude()));
					int number = targetRepo.countByUsernameAndSchoolYear(getCurrentYear(), user.getUsername());
					saleman.setPoint(setPoint(saleman.getPoint(), number));
					saleman.setNumberOfTask(number);
					salesmen.add(saleman);
				}
			}
		}
	}
	if (salesmen.size() < 5) {
		list = userRepo.findAllSaleman("SALESMAN", true);
		if (!ObjectUtils.isEmpty(list)) {
			List<SuggestionSalesman> temp = new ArrayList<>();
			for (User user : list) {
				boolean dup2 = false;
				for (SuggestionSalesman user2 : salesmen) {
					if (user2.getUsername().equalsIgnoreCase(user.getUsername())) {
						if (!user2.getContent().contains("In District")
								&& !user2.getContent().contains("In Ward")) {
							user2.setContent(user2.getContent() + ", " + "Nearly");
							user2.setPoint(user2.getPoint() + 1.5);
						}
						dup2 = true;
						break;
					}
				}
				if (!dup2) {
					SuggestionSalesman saleman = new SuggestionSalesman(user.getUsername(), user.getAvatar(),
							user.getAddress(), user.getFullName(), 0, "Nearly", 0, 1.5);
					saleman.setDistance(DistanceCalculateUtils.calculate(school.getLatitude(),
							school.getLongitude(), user.getLatitude(), user.getLongitude()));
					int number = targetRepo.countByUsernameAndSchoolYear(getCurrentYear(), user.getUsername());
					saleman.setPoint(setPoint(saleman.getPoint(), number));
					saleman.setNumberOfTask(number);
					temp.add(saleman);
				}
			}
			if(temp.size()>0) {
				
				temp.sort(Comparator.comparing(SuggestionSalesman::getDistance));
				temp.forEach(item -> System.out.println(item.getDistance()));
				salesmen.addAll(temp);
			}
		}
	}
	 Collections.sort(salesmen);
	 return salesmen;
}
	private List<SuggestionSalesman> suggestMutipleSchool(List<String> ids) {
		List<School> list = repo.findAllById(ids);
		List<SuggestionSalesman> exps = new ArrayList<>();
		List<SuggestionSalesman> temp = null;
		boolean difference = false;
		String district = list.get(0).getDistrict().getName();
		double lat = list.stream().mapToDouble(item ->item.getLatitude()).sum()/ids.size();
		double lon = list.stream().mapToDouble(item ->item.getLongitude()).sum()/ids.size();
		for (School school : list) {
			school.setLatitude(lat);
			school.setLongitude(lon);
			temp = getExpSalesman(school);
			for (SuggestionSalesman suggestionSalesman : temp) {
				boolean dup =false;
				for (SuggestionSalesman salesman : exps) {
					if(salesman.getUsername().equalsIgnoreCase(suggestionSalesman.getUsername())) {
						salesman.setContent(salesman.getContent()+", " +suggestionSalesman.getContent());
						salesman.setPoint(salesman.getPoint()+4);
						dup = true;
					}	
				}
				if(!dup) {
					exps.add(suggestionSalesman);
				}
			}
			
			if(!school.getDistrict().getName().equalsIgnoreCase(district)) {
				difference = true;
			}	
			
		}
		if(difference)
			return exps;
		else {
		List<User> users = userRepo.findByAddressContains(district+",", "SALESMAN", true);
		if (!ObjectUtils.isEmpty(list)) {
			for (User user : users) {
				if (exps.size() > 5)
					break;
				boolean dup = false;
				for (SuggestionSalesman user2 : exps) {
					if (user2.getUsername().equalsIgnoreCase(user.getUsername())) {
							user2.setContent(user2.getContent() + ", " + "In District");
							user2.setPoint(user2.getPoint() + 2);
							dup = true;
					}
				}
				if (dup == false) {
					SuggestionSalesman saleman = new SuggestionSalesman(user.getUsername(), user.getAvatar(),
							user.getAddress(), user.getFullName(), 0, "In District", 0, 2);
					saleman.setDistance(DistanceCalculateUtils.calculate(lat,
							lon, user.getLatitude(), user.getLongitude()));
					int number = targetRepo.countByUsernameAndSchoolYear(getCurrentYear(), user.getUsername());
					saleman.setPoint(setPoint(saleman.getPoint(), number));
					saleman.setNumberOfTask(number);
					exps.add(saleman);
				}
			}
		}
		if (exps.size() < 5) {
			users = userRepo.findAllSaleman("SALESMAN", true);
			if (!ObjectUtils.isEmpty(list)) {
				List<SuggestionSalesman> tempp = new ArrayList<>();
				for (User user : users) {
					boolean dup2 = false;
					for (SuggestionSalesman user2 : exps) {
						if (user2.getUsername().equalsIgnoreCase(user.getUsername())) {
							if (!user2.getContent().contains("In District")
									&& !user2.getContent().contains("In Ward")) {
								user2.setContent(user2.getContent() + ", " + "Nearly");
								user2.setPoint(user2.getPoint() + 1.5);
							}
							dup2 = true;
						}
					}
					if (!dup2) {
						SuggestionSalesman saleman = new SuggestionSalesman(user.getUsername(), user.getAvatar(),
								user.getAddress(), user.getFullName(), 0, "Nearly", 0, 1.5);
						saleman.setDistance(DistanceCalculateUtils.calculate(lat,
								lon, user.getLatitude(), user.getLongitude()));
						int number = targetRepo.countByUsernameAndSchoolYear(getCurrentYear(), user.getUsername());
						saleman.setPoint(setPoint(saleman.getPoint(), number));
						saleman.setNumberOfTask(number);
						tempp.add(saleman);
					}
				}
				if(tempp.size()>0) {
					
					tempp.sort(Comparator.comparing(SuggestionSalesman::getDistance));
					exps.addAll(tempp);
				}
			}
		}
		}
		Collections.sort(exps);
		return exps;
	}
	public List<DashboardDTO> getSchoolType(String type){
		List<DashboardDTO> result = new ArrayList<DashboardDTO>();
		List<String> list =null;
		switch (type) {
		case "school-status":
			list = getSchoolStatus();
			for (String item : list) {
				DashboardDTO dto = new DashboardDTO(item,calculateSchoolStatus(item));
				result.add(dto);
			}
			break;
		case "level":
			list = getLevel();
			for (String item : list) {
				DashboardDTO dto = new DashboardDTO(item,calculateSchoolLevel(item));
				result.add(dto);
			}
			break;
		case "district":
			list = getDistrict();
			for (String item : list) {
				DashboardDTO dto = new DashboardDTO(item,calculateSchoolDistrict(item));
				result.add(dto);
			}
			break;
		case "school-type":
			list = getType();
			for (String item : list) {
				DashboardDTO dto = new DashboardDTO(item,calculateSchoolType(item));
				result.add(dto);
			}
			break;
		default:
			break;
		}
		return result;
	}
	private List<String> getSchoolStatus() {
		List<SchoolStatus> entities = statusRepo.findAll();
		List<String> names = entities.stream().map(SchoolStatus::getName).collect(Collectors.toList());
		return names;
	}
	private double calculateSchoolStatus(String type) {
		List<School> list = repo.findAll((Specification<School>) (root, query, builder) -> {
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Predicate p = builder.conjunction();
		p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME), type));
		p = builder.and(p, builder.isTrue(root.get(School_.IS_ACTIVE)));
		return p;
		
	});
		return list.size();
	}
	private List<String> getLevel(){
		 List<EducationalLevel> entities = eduRepo.findAll();
			List<String> names = entities.stream().map(EducationalLevel::getName).collect(Collectors.toList());
			return names;
		}
	private double calculateSchoolLevel(String type) {
		List<School> list = repo.findAll((root, query, builder) -> {
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Join<School, EducationalLevel> school_level = root.join(School_.EDUCATIONAL_LEVEL);
			Predicate p = builder.conjunction();
		p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME), "??ang h???p t??c"));
		p = builder.and(p, builder.isTrue(root.get(School_.IS_ACTIVE)));
		p = builder.and(p, builder.equal(school_level.get(EducationalLevel_.NAME), type));
		return p;
		
	});
		return list.size();
	}
	private List<String> getDistrict(){
		List<District> entities = districtRepo.findAll();
		List<String> names = entities.stream().map(District::getName).collect(Collectors.toList());
		return names;
	}
	private double calculateSchoolDistrict(String type) {
		List<School> list = repo.findAll((root, query, builder) -> {
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Join<School, District> school_district = root.join(School_.DISTRICT);
			Predicate p = builder.conjunction();
			p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME), "??ang h???p t??c"));
			p = builder.and(p, builder.isTrue(root.get(School_.IS_ACTIVE)));
			p = builder.and(p, builder.equal(school_district.get(District_.NAME), type));
			return p;
		}); 
		return list.size();
	}
	public List<String> getType(){
		return  Stream.of(SchoolType.values())
                .map(SchoolType::getValues) // map using 'getValue'
                .collect(Collectors.toList());
	}
	
	private double calculateSchoolType(String type) {
		List<School> list = repo.findAll((root, query, builder) -> {
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Predicate p = builder.conjunction();
			p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME), "??ang h???p t??c"));
			p = builder.and(p, builder.isTrue(root.get(School_.IS_ACTIVE)));
			p = builder.and(p, builder.equal(root.get(School_.TYPE),SchoolType.valueOfLabel(type)));
			return p;
		}); 
		return list.size();
	}
}


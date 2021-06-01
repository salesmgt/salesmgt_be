package com.app.demo.services.impls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.DashboardDTO;
import com.app.demo.dtos.LocationCard;
import com.app.demo.dtos.NotePurposeRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ServiceDTO;
import com.app.demo.dtos.TaskDTO;
import com.app.demo.dtos.TaskDetails;
import com.app.demo.dtos.TaskTimelineItem;
import com.app.demo.dtos.TargetUpdateRequest;
import com.app.demo.dtos.TotalSchoolDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.EducationalLevel;
import com.app.demo.models.EducationalLevel_;
import com.app.demo.models.Report;
import com.app.demo.models.School;
import com.app.demo.models.SchoolStatus;
import com.app.demo.models.SchoolStatus_;
import com.app.demo.models.SchoolType;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.models.Purpose;
import com.app.demo.models.Purpose_;
import com.app.demo.models.School_;
import com.app.demo.models.Task;
import com.app.demo.models.Task_;
import com.app.demo.repositories.SchoolRepository;
import com.app.demo.repositories.PurposeRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.ITaskSchoolService;

@Service
public class TaskServiceImpl implements ITaskSchoolService {
	@Autowired
	private TaskRepository repo;
	
	@Autowired
	private SchoolRepository schoolRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PurposeRepository purposeRepo;
	/**
	 * Method có chức năng tạo đối tượng Pageable bằng cách xác định chiều sort của
	 * Page (DES/ASC).
	 * 
	 * @param page: vị trí page, limit: số item trên 1 page, column: thuộc tính để
	 *              sort, direction: chiều sort.
	 * @return Đối tượng Pageable bằng phương thức PageRequest.of(). Pageable được
	 *         kế thừa từ lớp Page.
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
	
	@Override
	public Paging<TaskDTO> getTargetByFilter(String result,Boolean assign,String status,String username, String key,String purpose,SchoolType type,String educationalLevel,String fullName, String district, String schoolYear, int page, int limit, String column, String direction) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		Page<Task> entities = (Page<Task>) repo.findAll((Specification<Task>) (root, query, builder) -> {
			Join<Task, User> target_user = root.join(Task_.USER,JoinType.LEFT);
			Join<Task, School> target_school = root.join(Task_.SCHOOL);			
			Join<Task, Purpose> target_purpose = root.join(Task_.PURPOSE);
			Join<School,District> school_district = target_school.join(School_.DISTRICT);
			Join<School,SchoolStatus> school_status = target_school.join(School_.SCHOOL_STATUS);
			Join<School,EducationalLevel> school_level = target_school.join(School_.EDUCATIONAL_LEVEL);
			if(!ObjectUtils.isEmpty(assign)) {
				if(assign)
				 target_user = root.join(Task_.USER);
			
			}
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				Predicate schoolName = builder.like(target_school.get(School_.NAME), "%" + key + "%");	
				Predicate repr = builder.like(target_school.get(School_.REPR_NAME), "%" + key + "%");
				Predicate year = builder.like(root.get(Task_.SCHOOL_YEAR), "%" + key + "%");	
				Predicate user = builder.like(target_user.get(User_.USERNAME), "%" + key + "%");
				Predicate full = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				p = builder.or(schoolName,repr,year,user,full);
			}			
			if(!ObjectUtils.isEmpty(type)) {
				p = builder.and(p, builder.equal(target_school.get(School_.TYPE), type));
			}
			if(!ObjectUtils.isEmpty(educationalLevel)) {
				p = builder.and(p, builder.like(school_level.get(EducationalLevel_.NAME), "%" +educationalLevel+ "%"));
			}
			if(!ObjectUtils.isEmpty(schoolYear)) {
				p = builder.and(p, builder.equal(root.get(Task_.SCHOOL_YEAR), schoolYear));
			}
			if(!ObjectUtils.isEmpty(status)) {
				p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME), status));
			}
			if(!ObjectUtils.isEmpty(assign)) {
				if(!assign)
				p = builder.and(p, builder.isNull(target_user.get(User_.USERNAME)));
			}
			if(!ObjectUtils.isEmpty(district)) {
				p = builder.and(p, builder.equal(school_district.get(District_.NAME),district));
			}
			if(!ObjectUtils.isEmpty(fullName)) {
				p = builder.and(p, builder.equal(target_user.get(User_.FULL_NAME), fullName));
			}
			if(!ObjectUtils.isEmpty(result)) {
				if(result.equalsIgnoreCase("Ongoing")) {
				p = builder.and(p,builder.equal(root.get(Task_.RESULT), "TBD"));
//				p = builder.and(p,builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), new Date()));
//				}
//				else if (result.equalsIgnoreCase("failed")) {
//				p = builder.and(p,builder.equal(root.get(Task_.RESULT), "TBD"));	
//				p = builder.and(p,builder.lessThanOrEqualTo(root.get(Task_.END_DATE), new Date()));
				}else {
				p = builder.and(p,builder.equal(root.get(Task_.RESULT), result));	
				}
			}
			if(!ObjectUtils.isEmpty(username)) {
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			}
			
			if(!ObjectUtils.isEmpty(purpose)) {
				p = builder.and(p, builder.equal(target_purpose.get(Purpose_.NAME),purpose));
			}
				p = builder.and(p, builder.isTrue(root.get(Task_.IS_ACTIVE)));
			
			return p;
		}, paging(page, limit, column, direction));
		List<TaskDTO> results = new ArrayList<TaskDTO>();
		Paging<TaskDTO> targetPage = new Paging<TaskDTO>();
		if (entities.hasContent()) {
			entities.getContent().forEach(item -> {
				TaskDTO dto = Mapper.getMapper().map(item, TaskDTO.class);
				dto.setEndDate(null);
				if(!ObjectUtils.isEmpty(item.getEndDate()))
				dto.setEndDate(sdf.format(item.getEndDate()));
				dto.setAssignDate(null);
				if(!ObjectUtils.isEmpty(item.getAssignDate()))
				dto.setAssignDate(sdf.format(item.getAssignDate()));	
				dto.setPurpose(item.getPurpose().getName());
				dto.setSchoolName(item.getSchool().getName());
				dto.setDistrict(item.getSchool().getDistrict().getName());
				dto.setReprIsMale(item.getSchool().isReprIsMale());
				dto.setReprName(item.getSchool().getReprName());
				dto.setReprEmail(item.getSchool().getReprEmail());
				dto.setReprPhone(item.getSchool().getReprPhone());
				if(!ObjectUtils.isEmpty(item.getUser())){
				dto.setUserPhone(item.getUser().getPhone());
				dto.setUserEmail(item.getUser().getEmail());
				dto.setAvatar(item.getUser().getAvatar());
				dto.setResult(item.getResult());
				dto.setFullName(item.getUser().getFullName());
				dto.setUsername(item.getUser().getUsername());
				}else {
					dto.setUserPhone(null);
					dto.setUserEmail(null);
					dto.setAvatar(null);
					dto.setFullName(null);
					dto.setUsername(null);
				}
				dto.setSchoolStatus(item.getSchool().getSchoolStatus().getName());
				dto.setLevel(item.getSchool().getEducationalLevel().getName());
				dto.setSchoolAddress(item.getSchool().getAddress());
				dto.setSchoolType(item.getSchool().getType().getValues());
				dto.setSchoolId(item.getSchool().getSchoolId());
				dto.setPurpose(item.getPurpose().getName());
				String cmt = item.getNote();
				if(!ObjectUtils.isEmpty(cmt)) {
					String supervisor = cmt.substring(1,cmt.indexOf("]"));
					dto.setNoteBy(supervisor);
					dto.setNote(cmt.substring(cmt.indexOf("]")+2, cmt.length()));
					}
				results.add(dto);
			});
			targetPage.setList(results);
			targetPage.setTotalElements(entities.getTotalElements());
			targetPage.setTotalPage(entities.getTotalPages());
		}
		return targetPage;
	}
	@Override
	public String delete(int targetId){
		Task target = repo.getOne(targetId);
		
			if(!ObjectUtils.isEmpty(target.getServices())) {
				return "Service is existed";
			}
			else if(!ObjectUtils.isEmpty(target.getReports())) {
				return "Reports is existed";
			}else {
				target.setActive(false);
				repo.save(target);	
				return "Deleted";
			}
		
		}
//	@Override
//	public void assign(int targetId, String username) {
//		TargetSchool target = repo.getOne(targetId);
//			User user = null;
//			if(!ObjectUtils.isEmpty(username))
//			 user = userRepo.getOne(username);
//			target.setUser(user);
//			target.setAssignDate(null);
//			repo.save(target);
//			return;
//		}
	@Override
	public int insert(List<TaskDTO> dtos) {
		Purpose purpose = purposeRepo.findByName(dtos.get(0).getPurpose());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		List<Task> targets = new ArrayList<>();
		dtos.forEach(item ->{		
			Task target = new Task();
			target.setActive(true);
			target.setSchoolYear(item.getSchoolYear());
			target.setSchool(schoolRepo.getOne(item.getSchoolId()));
			target.setPurpose(purpose);
			try {
				target.setEndDate(sdf.parse(item.getEndDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			target.setResult("TBD");
			targets.add(target);
		});
		return repo.saveAll(targets).size();
		
	}
	@Override
	public List<String> getSchoolYear(){
			return repo.getSchoolYears();
	}
	@Override
	public List<LocationCard> getTargetSchoolName(String username, String key){
		Page<Task> entities = (Page<Task>) repo.findAll((Specification<Task>) (root, query, builder) -> {
			Join<Task, School> target_school = root.join(Task_.SCHOOL);
			Join<Task, User> target_user = root.join(Task_.USER);
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				p = builder.like(target_school.get(School_.NAME), "%" + key + "%");
				query.distinct(true);
			}
			if(!ObjectUtils.isEmpty(username)) {
				p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			}
			return p;
		}, paging(0, 3, "id", "DESC"));
		List<LocationCard> results = new ArrayList<LocationCard>();
		User user = userRepo.findByUsername(username);
		results.add(new LocationCard(-1,"Home (default)",user.getAddress()));
		results.add(new LocationCard(0,"Company (default)","Major Education"));
		if (entities.hasContent()) {
			entities.getContent().forEach(item -> {
				LocationCard dto = new LocationCard();	
				dto.setId(item.getId());
				dto.setSchoolName(item.getSchool().getName());
				dto.setDistrict(item.getSchool().getDistrict().getName());
				results.add(dto);
			});
			}
			return results;
	}
	@Override
	public List<String> getSchoolYearBySchoolId(int id) {
		return repo.getSchoolYearsBySchoolId(id);
	}
	
	@Transactional
	@Override
	public void assignMutiple(List<TaskDTO> request) {
		User user = userRepo.findByUsername(request.get(0).getUsername());
		List<Task> targets = new ArrayList<>(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		for (TaskDTO taskDTO : request) {
			Task target = repo.getOne(taskDTO.getId());
			target.setUser(user);
			try {
				target.setAssignDate(sdf.parse(taskDTO.getAssignDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(taskDTO.getNote()!= null)
			target.setNote(taskDTO.toString());
			targets.add(target);
		}
		repo.saveAll(targets);
	}
	public TaskDetails getOne(int id) {
		Task item = repo.getOne(id);
		TaskDetails dto = new TaskDetails();
		dto.setId(item.getId());
		dto.setSchoolYear(item.getSchoolYear());
		dto.setPurpose(item.getPurpose().getName());
		dto.setSchoolName(item.getSchool().getName());
		dto.setDistrict(item.getSchool().getDistrict().getName());
		dto.setReprIsMale(item.getSchool().isReprIsMale());
		dto.setReprName(item.getSchool().getReprName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		String assign = null;
		String end = null;		
		try {
			if(!ObjectUtils.isEmpty(item.getAssignDate())) {
				assign = sdf.format(item.getAssignDate());
				dto.setAssignDate(sdf.parse(assign));
			}
			if(!ObjectUtils.isEmpty(item.getAssignDate())) {
				end = sdf.format(item.getEndDate());
				dto.setEndDate(sdf.parse(end));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		dto.setReprEmail(item.getSchool().getReprEmail());
		dto.setReprPhone(item.getSchool().getReprPhone());
		
		dto.setSchoolStatus(item.getSchool().getSchoolStatus().getName());
		if(!ObjectUtils.isEmpty(item.getUser())){
		dto.setUserPhone(item.getUser().getPhone());
		dto.setUserEmail(item.getUser().getEmail());
		dto.setAvatar(item.getUser().getAvatar());
		dto.setFullName(item.getUser().getFullName());
		dto.setResult(item.getResult());
		dto.setUsername(item.getUser().getUsername());
		}else {
			dto.setUserPhone(null);
			dto.setUserEmail(null);
			dto.setAvatar(null);
			dto.setFullName(null);
			dto.setUsername(null);
		}
		dto.setLevel(item.getSchool().getEducationalLevel().getName());
		dto.setSchoolAddress(item.getSchool().getAddress());
		dto.setSchoolType(item.getSchool().getType().getValues());
		dto.setSchoolId(item.getSchool().getSchoolId());
		dto.setPurpose(item.getPurpose().getName());
		String cmt = item.getNote();
		if(!ObjectUtils.isEmpty(cmt)) {
			String supervisor = cmt.substring(1,cmt.indexOf("]"));
			dto.setNoteBy(supervisor);
			dto.setNote(cmt.substring(cmt.indexOf("]")+2, cmt.length()));
			}
		List<ServiceDTO> memo = new ArrayList<>();
			if(!ObjectUtils.isEmpty(item.getServices())){
			for ( com.app.demo.models.Service entity : item.getServices()) {
				ServiceDTO memoDTO = Mapper.getMapper().map(entity,ServiceDTO.class);
				memo.add(memoDTO);
			}		
		}
		if(!ObjectUtils.isEmpty(memo)) {
			dto.setServices(memo);
		}
		return dto;
	}
	
	public void updateTarget(int id,TargetUpdateRequest request) {
		Task target = repo.getOne(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		if(target !=null && !ObjectUtils.isEmpty(request.getReprEmail())&&!ObjectUtils.isEmpty(request.getReprName())
				&&!ObjectUtils.isEmpty(request.getReprPhone())&&!ObjectUtils.isEmpty(request.getReprIsMale())) {
			School school = target.getSchool();
			school.setReprEmail(request.getReprEmail());
			school.setReprName(request.getReprName());
			school.setReprPhone(request.getReprPhone());
			school.setReprIsMale(request.getReprIsMale());
			schoolRepo.save(school);
		}
		target.setSchoolYear(request.getSchoolYear());
		target.setNote(request.getNote());
		try {
			target.setEndDate(sdf.parse(request.getDealine()));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		if(!request.getPurpose().equalsIgnoreCase(target.getPurpose().getName()))
		target.setPurpose(purposeRepo.findByName(request.getPurpose()));
		repo.save(target);
	}
	@Override
	public void updatePurposeNote(int id, NotePurposeRequest request) {
		if(!ObjectUtils.isEmpty(request.getNote())||!ObjectUtils.isEmpty(request.getPurpose())) {
		Task target = repo.getOne(id);
		if(!target.getPurpose().getName().equalsIgnoreCase(request.getPurpose()))
			target.setPurpose(purposeRepo.findByName(request.getPurpose()));
		
		target.setNote(request.toString());
		
		repo.save(target);
		}
	}
	@Override
	public void unassign(int targetId) {
		Task target = repo.getOne(targetId);
		if(!ObjectUtils.isEmpty(target.getUser())) {
			target.setUser(null);
			target.setAssignDate(null);
			repo.save(target);
		}
	}
	@Override
	public DashboardDTO getDataset() {
		List<String> years =repo.getSchoolYears();
		TreeMap<String, List<TotalSchoolDTO>> map2 = new TreeMap<String, List<TotalSchoolDTO>>();
		TreeMap<String, List<TotalSchoolDTO>> map3 = new TreeMap<String, List<TotalSchoolDTO>>();
		TreeMap<String, List<TotalSchoolDTO>> map4 = new TreeMap<String, List<TotalSchoolDTO>>();
		TreeMap<String, List<TotalSchoolDTO>> map5 = new TreeMap<String, List<TotalSchoolDTO>>();
		TreeMap<String, List<TotalSchoolDTO>> map6 = new TreeMap<String, List<TotalSchoolDTO>>();
		for (String year : years) {
			
			ArrayList<TotalSchoolDTO> list2 = new ArrayList<>();
			list2.add(new TotalSchoolDTO(repo.countBySchoolYearAndPurpose(year, "Ký mới hợp đồng")));
			map2.put(year,list2);
		
			ArrayList<TotalSchoolDTO> list3 = new ArrayList<>();
			list3.add(new TotalSchoolDTO(repo.countBySchoolYearAndPurpose(year, "Theo dõi")));
			map3.put(year,list3);
			
			ArrayList<TotalSchoolDTO> list4 = new ArrayList<>();
			list4.add(new TotalSchoolDTO(repo.countBySchoolYearAndPurpose(year, "Chăm sóc")));
			map4.put(year,list4);
			
			ArrayList<TotalSchoolDTO> list5 = new ArrayList<>();
			list5.add(new TotalSchoolDTO(repo.countBySchoolYearAndPurpose(year, "Tái ký hợp đồng")));
			map5.put(year,list5);
			
			ArrayList<TotalSchoolDTO> list6 = new ArrayList<>();
			list6.add(new TotalSchoolDTO(repo.countBySchoolYearAndPurpose(year, "Sales mới")));
			map6.put(year,list6);
		}
		return new DashboardDTO(map6, map3, map5, map2, map4);
	}
	
	@Override
	public List<TaskTimelineItem> getTimeline(int targetId){
		Optional<Task> target = repo.findById(targetId);
		List<TaskTimelineItem> list = new ArrayList<TaskTimelineItem>();
		List<Report> reports = target.get().getReports();
		if(!ObjectUtils.isEmpty(reports))
			for (Report item : reports) {
				TaskTimelineItem timeline = new TaskTimelineItem(item.getId(), "report", item.getDate(),item.getDescription(), null, null, item.isSuccess(), null, null, 0,null);
				list.add(timeline);
			}
		List<com.app.demo.models.Service> services = target.get().getServices();
		if(!ObjectUtils.isEmpty(services))
			for (com.app.demo.models.Service item : services) {
				if(item.getStatus().equalsIgnoreCase("approved")) {
				TaskTimelineItem timeline = new TaskTimelineItem(item.getId(), "service", item.getSubmitDate(), null, item.getStatus(),item.getServiceType().getName() , false, item.getStartDate(), item.getEndDate(), item.getClassNumber(),item.getApproveDate());
				list.add(timeline);
				}
				}
		 Collections.sort(list);
			
		return list;
	}
	@Override
	public void completeTask(int id) {
		Task task = repo.getOne(id);
		task.setResult("successful");
		task.setCompletedDate(new Date());
		repo.save(task);
	}
	@Override
	public void failTask(int id) {
		Task task = repo.getOne(id);
		task.setResult("failed");
		task.setCompletedDate(new Date());
		repo.save(task);
	}
}

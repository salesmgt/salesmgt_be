package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import com.app.demo.dtos.TargetDTO;
import com.app.demo.dtos.TargetDetails;
import com.app.demo.dtos.TargetTimelineItem;
import com.app.demo.dtos.TargetUpdateRequest;
import com.app.demo.dtos.TotalSchoolDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.Report;
import com.app.demo.models.School;
import com.app.demo.models.SchoolStatus;
import com.app.demo.models.SchoolStatus_;
import com.app.demo.models.SchoolType;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.models.TargetPurpose;
import com.app.demo.models.TargetPurpose_;
import com.app.demo.models.School_;
import com.app.demo.models.TargetSchool;
import com.app.demo.models.TargetSchool_;
import com.app.demo.repositories.SchoolRepository;
import com.app.demo.repositories.TargetPurposeRepository;
import com.app.demo.repositories.TargetRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.ITargetSchoolService;

@Service
public class TargetServiceImpl implements ITargetSchoolService {
	@Autowired
	private TargetRepository repo;
	
	@Autowired
	private SchoolRepository schoolRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TargetPurposeRepository purposeRepo;
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
	public Paging<TargetDTO> getTargetByFilter(Boolean assign,String status,String username, String key,String purpose,SchoolType type,String educationalLevel,String fullName, String district, String schoolYear, int page, int limit, String column, String direction) {
		Page<TargetSchool> entities = (Page<TargetSchool>) repo.findAll((Specification<TargetSchool>) (root, query, builder) -> {
			Join<TargetSchool, User> target_user = root.join(TargetSchool_.USER,JoinType.LEFT);
			Join<TargetSchool, School> target_school = root.join(TargetSchool_.SCHOOL);			
			Join<TargetSchool, TargetPurpose> target_purpose = root.join(TargetSchool_.TARGET_PURPOSE);
			Join<School,District> school_district = target_school.join(School_.DISTRICT);
			Join<School,SchoolStatus> school_status = target_school.join(School_.SCHOOL_STATUS);
			if(!ObjectUtils.isEmpty(assign)) {
				if(assign)
				 target_user = root.join(TargetSchool_.USER);
			
			}
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				Predicate schoolName = builder.like(target_school.get(School_.NAME), "%" + key + "%");	
				Predicate repr = builder.like(target_school.get(School_.REPR_NAME), "%" + key + "%");
				Predicate year = builder.like(root.get(TargetSchool_.SCHOOL_YEAR), "%" + key + "%");	
				Predicate user = builder.like(target_user.get(User_.USERNAME), "%" + key + "%");
				Predicate full = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				p = builder.or(schoolName,repr,year,user,full);
			}			
			if(!ObjectUtils.isEmpty(type)) {
				p = builder.and(p, builder.equal(target_school.get(School_.TYPE), type));
			}
			if(!ObjectUtils.isEmpty(educationalLevel)) {
				p = builder.and(p, builder.like(target_school.get(School_.EDUCATIONAL_LEVEL), "%" +educationalLevel+ "%"));
			}
			if(!ObjectUtils.isEmpty(schoolYear)) {
				p = builder.and(p, builder.equal(root.get(TargetSchool_.SCHOOL_YEAR), schoolYear));
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
			if(!ObjectUtils.isEmpty(username)) {
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			}
			if(!ObjectUtils.isEmpty(purpose)) {
				p = builder.and(p, builder.equal(target_purpose.get(TargetPurpose_.NAME),purpose));
			}
				p = builder.and(p, builder.isTrue(root.get(TargetSchool_.IS_ACTIVE)));
			
			return p;
		}, paging(page, limit, column, direction));
		List<TargetDTO> results = new ArrayList<TargetDTO>();
		Paging<TargetDTO> targetPage = new Paging<TargetDTO>();
		if (entities.hasContent()) {
			entities.getContent().forEach(item -> {
				TargetDTO dto = Mapper.getMapper().map(item, TargetDTO.class);
				
				dto.setPurpose(item.getTargetPurpose().getName());
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
				dto.setPurpose(item.getTargetPurpose().getName());
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
		TargetSchool target = repo.getOne(targetId);
		
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
	public int insert(List<TargetDTO> dtos) {
		TargetPurpose purpose = purposeRepo.findByName(dtos.get(0).getPurpose());
		List<TargetSchool> targets = new ArrayList<>();
		dtos.forEach(item ->{		
			TargetSchool target = new TargetSchool();
			target.setActive(true);
			target.setSchoolYear(item.getSchoolYear());
			target.setSchool(schoolRepo.getOne(item.getSchoolId()));
			target.setTargetPurpose(purpose);
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
		Page<TargetSchool> entities = (Page<TargetSchool>) repo.findAll((Specification<TargetSchool>) (root, query, builder) -> {
			Join<TargetSchool, School> target_school = root.join(TargetSchool_.SCHOOL);
			Join<TargetSchool, User> target_user = root.join(TargetSchool_.USER);
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
	public void assignMutiple(List<TargetDTO> request) {
		User user = userRepo.findByUsername(request.get(0).getUsername());
		List<TargetSchool> targets = new ArrayList<>(); 
		for (TargetDTO targetDTO : request) {
			TargetSchool target = repo.getOne(targetDTO.getId());
			target.setUser(user);
			target.setAssignDate(targetDTO.getAssignDate());
			if(targetDTO.getNote()!= null)
			target.setNote(targetDTO.toString());
			targets.add(target);
		}
		repo.saveAll(targets);
	}
	public TargetDetails getOne(int id) {
		TargetSchool item = repo.getOne(id);
		TargetDetails dto = new TargetDetails();
		dto.setId(item.getId());
		dto.setSchoolYear(item.getSchoolYear());
		dto.setPurpose(item.getTargetPurpose().getName());
		dto.setSchoolName(item.getSchool().getName());
		dto.setDistrict(item.getSchool().getDistrict().getName());
		dto.setReprIsMale(item.getSchool().isReprIsMale());
		dto.setReprName(item.getSchool().getReprName());
		dto.setReprEmail(item.getSchool().getReprEmail());
		dto.setReprPhone(item.getSchool().getReprPhone());
		dto.setAssignDate(item.getAssignDate());
		dto.setSchoolStatus(item.getSchool().getSchoolStatus().getName());
		if(!ObjectUtils.isEmpty(item.getUser())){
		dto.setUserPhone(item.getUser().getPhone());
		dto.setUserEmail(item.getUser().getEmail());
		dto.setAvatar(item.getUser().getAvatar());
		dto.setFullName(item.getUser().getFullName());
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
		dto.setPurpose(item.getTargetPurpose().getName());
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
			dto.setMemorandums(memo);
		}
		return dto;
	}
	
	public void updateTarget(int id,TargetUpdateRequest request) {
		TargetSchool target = repo.getOne(id);
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
		if(!request.getPurpose().equalsIgnoreCase(target.getTargetPurpose().getName()))
		target.setTargetPurpose(purposeRepo.findByName(request.getPurpose()));
		repo.save(target);
	}
	@Override
	public void updatePurposeNote(int id, NotePurposeRequest request) {
		if(!ObjectUtils.isEmpty(request.getNote())||!ObjectUtils.isEmpty(request.getPurpose())) {
		TargetSchool target = repo.getOne(id);
		if(!target.getTargetPurpose().getName().equalsIgnoreCase(request.getPurpose()))
			target.setTargetPurpose(purposeRepo.findByName(request.getPurpose()));
		
		target.setNote(request.toString());
		
		repo.save(target);
		}
	}
	@Override
	public void unassign(int targetId) {
		TargetSchool target = repo.getOne(targetId);
		if(!ObjectUtils.isEmpty(target.getUser())) {
			target.setUser(null);
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
	public List<TargetTimelineItem> getTimeline(int targetId){
		Optional<TargetSchool> target = repo.findById(targetId);
		List<TargetTimelineItem> list = new ArrayList<TargetTimelineItem>();
		List<Report> reports = target.get().getReports();
		if(!ObjectUtils.isEmpty(reports))
			for (Report item : reports) {
				TargetTimelineItem timeline = new TargetTimelineItem(item.getId(), "report", item.getDate(),item.getDescription(), null, null, item.isSuccess(), null, null, 0);
				list.add(timeline);
			}
		List<com.app.demo.models.Service> services = target.get().getServices();
		if(!ObjectUtils.isEmpty(services))
			for (com.app.demo.models.Service item : services) {
				TargetTimelineItem timeline = new TargetTimelineItem(item.getId(), "service", item.getApproveDate(), null, item.getStatus(),item.getServiceType().getName() , false, item.getStartDate(), item.getEndDate(), item.getClassNumber());
				list.add(timeline);
				}
		 Collections.sort(list);
			
		return list;
	}
}

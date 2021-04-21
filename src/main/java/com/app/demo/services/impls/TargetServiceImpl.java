package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

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

import com.app.demo.dtos.AddTargetRequest;
import com.app.demo.dtos.LocationCard;
import com.app.demo.dtos.MutiAssignRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.dtos.TargetFilterRequest;
import com.app.demo.dtos.TargetRequest;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
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
	public Paging<TargetDTO> getTargetByFilter(String key,String purpose,SchoolType type, Level educationalLevel,Scale scale, String fullName, String district, String schoolYear, int page, int limit, String column, String direction) {
		Page<TargetSchool> entities = (Page<TargetSchool>) repo.findAll((Specification<TargetSchool>) (root, query, builder) -> {
			Join<TargetSchool, School> target_school = root.join(TargetSchool_.SCHOOL);			
			Join<TargetSchool, TargetPurpose> target_purpose = root.join(TargetSchool_.TARGET_PURPOSE);
			Join<School,District> school_district = target_school.join(School_.DISTRICT);
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				Predicate schoolName = builder.like(target_school.get(School_.NAME), "%" + key + "%");	
				Predicate repr = builder.like(target_school.get(School_.REPR_NAME), "%" + key + "%");
				Predicate year = builder.like(root.get(TargetSchool_.SCHOOL_YEAR), "%" + key + "%");		
				p = builder.or(schoolName,repr,year);
			}			
			if(!ObjectUtils.isEmpty(type)) {
				p = builder.and(p, builder.equal(target_school.get(School_.TYPE), type));
			}
			if(!ObjectUtils.isEmpty(educationalLevel)) {
				p = builder.and(p, builder.equal(target_school.get(School_.EDUCATIONAL_LEVEL), educationalLevel));
			}
			if(!ObjectUtils.isEmpty(scale)) {
				p = builder.and(p, builder.equal(target_school.get(School_.SCALE), scale));
			}
			if(!ObjectUtils.isEmpty(schoolYear)) {
				p = builder.and(p, builder.equal(root.get(TargetSchool_.SCHOOL_YEAR), schoolYear));
			}
			if(!ObjectUtils.isEmpty(district)) {
				p = builder.and(p, builder.equal(school_district.get(District_.NAME),district));
			}
			if(!ObjectUtils.isEmpty(fullName)) {
					Join<TargetSchool, User> target_user = root.join(TargetSchool_.USER);
				p = builder.and(p, builder.equal(target_user.get(User_.FULL_NAME), fullName));
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
				dto.setLevel(item.getSchool().getEducationalLevel().getValues());
				dto.setSchoolDescription(item.getSchool().getDescription());
				dto.setSchoolScale(item.getSchool().getScale());
				dto.setSchoolAddress(item.getSchool().getAddress());
				dto.setSchoolType(item.getSchool().getType().getValues());
				dto.setSchoolId(item.getSchool().getId());
				dto.setPurpose(item.getTargetPurpose().getName());
				results.add(dto);
			});
			targetPage.setList(results);
			targetPage.setTotalElements(entities.getTotalElements());
			targetPage.setTotalPage(entities.getTotalPages());
		}
		return targetPage;
	}
	@Override
	public void delete(int targetId) {
		TargetSchool target = repo.getOne(targetId);
		target.setActive(false);
		repo.save(target);	
	}
	@Override
	public void assign(int targetId, String username) {
		TargetSchool target = repo.getOne(targetId);
			User user = null;
			if(!ObjectUtils.isEmpty(username))
			 user = userRepo.getOne(username);
			target.setUser(user);
			repo.save(target);
			return;
		}
	@Override
	public int insert(AddTargetRequest dto) {
		if(!ObjectUtils.isEmpty(dto.getFilter()) && ObjectUtils.isEmpty(dto.getSchoolIds())) {
			TargetFilterRequest filter = dto.getFilter();
			List<School> entity = schoolRepo.findAll((Specification<School>) (root, query, builder) -> {
				Join<School,TargetSchool> target_school = root.join(School_.TARGET_SCHOOLS,JoinType.LEFT);
				Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
				Join<School, District> school = root.join(School_.DISTRICT);
				Predicate p = builder.conjunction();
				if(!ObjectUtils.isEmpty(filter.getDistrict()))
					p = builder.and(p, builder.equal(school.get(District_.NAME), filter.getDistrict()));
				if(!ObjectUtils.isEmpty(filter.getStatus()))
					p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME), filter.getStatus()));
				if(!ObjectUtils.isEmpty(filter.getLevelEducational()))
					p = builder.and(p, builder.equal(root.get(School_.EDUCATIONAL_LEVEL), Level.valueOfLabel(filter.getLevelEducational())));
				if(!ObjectUtils.isEmpty(filter.getScale()))
					p = builder.and(p, builder.equal(root.get(School_.SCALE), filter.getScale()));	
				if(!ObjectUtils.isEmpty(filter.getType()))
					p = builder.and(p, builder.equal(root.get(School_.TYPE), SchoolType.valueOfLabel(filter.getType())));		
				p = builder.and(p, builder.notEqual(target_school.get(TargetSchool_.SCHOOL_YEAR), dto.getSchoolYear()));
				return p;			
			});
			System.out.println("Số row tìm được "+entity.size());
			
			List<TargetSchool> targets = new ArrayList<>(); 
			entity.forEach(item -> {
				TargetSchool target = new TargetSchool();
				target.setActive(true);
				target.setSchool(item);
				target.setNote(dto.getNote());
				target.setSchoolYear(dto.getSchoolYear());
				target.setTargetPurpose(purposeRepo.findByName(dto.getPurpose()));
				targets.add(target);
			});
			return repo.saveAll(targets).size();
		}
		if(!ObjectUtils.isEmpty(dto.getSchoolIds())) {
			List<TargetSchool> targets = new ArrayList<>(); 
			dto.getSchoolIds().forEach(item ->{
				School school = schoolRepo.getOne(item);
				TargetSchool target = new TargetSchool();
				target.setActive(true);
				target.setSchool(school);
				target.setNote(dto.getNote());
				target.setSchoolYear(dto.getSchoolYear());
				target.setTargetPurpose(purposeRepo.findByName(dto.getPurpose()));
				targets.add(target);
			});
			return repo.saveAll(targets).size();
		}
		return -1;
		
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
	public void assignMutiple(MutiAssignRequest request) {
		List<TargetSchool> targets = repo.findAllById(request.getTargetId());
		User user = userRepo.findByUsername(request.getUsername());
		targets.forEach(item -> item.setUser(user));
		repo.saveAll(targets);
	}
	public TargetDTO getOne(int id) {
		TargetSchool item = repo.getOne(id);
		TargetDTO dto = new TargetDTO();
		dto.setId(item.getId());
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
		dto.setLevel(item.getSchool().getEducationalLevel().getValues());
		dto.setSchoolDescription(item.getSchool().getDescription());
		dto.setSchoolScale(item.getSchool().getScale());
		dto.setSchoolAddress(item.getSchool().getAddress());
		dto.setSchoolType(item.getSchool().getType().getValues());
		dto.setSchoolId(item.getSchool().getId());
		dto.setPurpose(item.getTargetPurpose().getName());
		return dto;
	}
	}

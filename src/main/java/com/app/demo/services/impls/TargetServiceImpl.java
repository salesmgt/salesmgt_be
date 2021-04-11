package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

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

import com.app.demo.dtos.LocationCard;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.TargetDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.School;
import com.app.demo.models.SchoolType;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.models.TargetPurpose;
import com.app.demo.models.TargetPurpose_;
import com.app.demo.models.School_;
import com.app.demo.models.TargetSchool;
import com.app.demo.models.TargetSchool_;
import com.app.demo.repositories.TargetRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.ITargetSchoolService;

@Service
public class TargetServiceImpl implements ITargetSchoolService {
	@Autowired
	private TargetRepository repo;
	
	@Autowired
	private UserRepository userRepo;


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
			Join<TargetSchool, User> target_user = root.join(TargetSchool_.USER);
			Join<TargetSchool, TargetPurpose> target_purpose = root.join(TargetSchool_.TARGET_PURPOSE);
			Join<School,District> school_district = target_school.join(School_.DISTRICT);
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				Predicate schoolName = builder.like(target_school.get(School_.NAME), "%" + key + "%");	
				Predicate repr = builder.like(target_school.get(School_.REPR_NAME), "%" + key + "%");
				Predicate fullname = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				Predicate year = builder.like(root.get(TargetSchool_.SCHOOL_YEAR), "%" + key + "%");		
				Predicate username = builder.like(target_user.get(User_.USERNAME), "%" + key + "%");
				p = builder.or(schoolName,fullname,repr,year,username);
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
				p = builder.and(p, builder.equal(target_user.get(User_.FULL_NAME), fullName));
			}
			if(!ObjectUtils.isEmpty(purpose)) {
				p = builder.and(p, builder.equal(target_purpose.get(TargetPurpose_.NAME),purpose));
			}
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
				dto.setReprGender(item.getSchool().isReprGender());
				dto.setReprName(item.getSchool().getReprName());
				dto.setReprEmail(item.getSchool().getReprEmail());
				dto.setReprPhone(item.getSchool().getReprPhone());
				dto.setUserPhone(item.getUser().getPhone());
				dto.setUserEmail(item.getUser().getEmail());
				dto.setAvatar(item.getUser().getAvatar());
				dto.setFullName(item.getUser().getFullName());
				dto.setUsername(item.getUser().getUsername());
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
	public void assign(int targetId, String username) {
		if(repo.existsById(targetId)){
			repo.assign(targetId, username);
		}			
	}
	public void insert(TargetDTO dto) {
		
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
	}

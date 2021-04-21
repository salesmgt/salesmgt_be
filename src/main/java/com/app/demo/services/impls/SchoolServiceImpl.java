package com.app.demo.services.impls;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

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

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.SchoolDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Level;
import com.app.demo.models.SchoolStatus;
import com.app.demo.models.SchoolStatus_;
import com.app.demo.models.Scale;
import com.app.demo.models.School;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.SchoolType;
import com.app.demo.models.School_;
import com.app.demo.models.TargetSchool;
import com.app.demo.models.TargetSchool_;
import com.app.demo.repositories.DistrictRepository;
import com.app.demo.repositories.SchoolRepository;
import com.app.demo.repositories.SchoolStatusRepository;
import com.app.demo.services.ISchoolService;

@Service
public class SchoolServiceImpl implements ISchoolService {
	
	@Autowired
	private SchoolRepository repo;
	
	@Autowired
	private DistrictRepository districtRepo;
	
	@Autowired
	private SchoolStatusRepository statusRepo;

	/**
	 * Method có chức năng tạo đối tượng Pageable bằng cách xác định chiều sort của Page (DES/ASC).
	 * @param  page: vị trí page, limit: số item trên 1 page, column: thuộc tính để sort, direction: chiều sort.
	 * @return Đối tượng Pageable bằng phương thức PageRequest.of(). Pageable được kế thừa từ lớp Page. 
	 * 
	 * @author Nguyen Hoang Gia
	 * @version 1.0
	 * @since 3/7/2021
	 */
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
	
	/**
	 * Method phức hợp dùng để xử lí logic filter và phân trang.
	 * @param  district: "quận 1", status: "Đang hợp tác", type="CONG_LAP", educationLevel: "THCS",scale: "Nhỏ".
	 * @param  key: keyword text để search gần giống, có thể thuộc các field (phone,name,address,description).
	 * @param  page: vị trí page, limit: số item trên 1 page, column: thuộc tính để sort, direction: chiều sort.
	 * @return đối tượng Paging đã được filter và phân trang. 
	 * @implSpec imlplements interface Specification gồm 3 param (root, criteriaQuery, criteriaBuilder) 
	 *          để custom phương thức getAll() của HQL/JPA bằng Predicate - mệnh đề điều kiện.
	 * @implSpec School_, District_ là những class được generate auto lúc build thông qua hibernate-jpamodelgen (xem pom.xml).
	 *			detail in "target/generated-source/"
	 * 
	 * @author  Nguyễn Hoàng Gia
	 * @version 1.0
	 * @since   3/7/2021
	 */
	@Override
	public Paging<SchoolDTO> getSchoolByFilter(String district, String status, SchoolType type, Level educationalLevel,Scale scale,String key,
												String schoolYear,int page, int limit, String column, String direction){		
		Page<School> pageEntities = (Page<School>) repo.findAll((Specification<School>) (root, query,criteriaBuilder) -> {
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Join<School, District> school = root.join(School_.DISTRICT);
			Predicate p = criteriaBuilder.conjunction();
			if (!ObjectUtils.isEmpty(key)) {
				Predicate phone = criteriaBuilder.like(root.get(School_.PHONE), "%" + key + "%");
				Predicate name =  criteriaBuilder.like(root.get(School_.NAME), "%" + key + "%");
				Predicate address = criteriaBuilder.like(root.get(School_.ADDRESS), "%" + key + "%");
				Predicate description = criteriaBuilder.like(root.get(School_.DESCRIPTION), "%" + key + "%");
				Predicate reprName = criteriaBuilder.like(root.get(School_.REPR_NAME), "%" + key + "%");
				Predicate reprPhone = criteriaBuilder.like(root.get(School_.REPR_PHONE), "%" + key + "%");
				p = criteriaBuilder.or(phone,name,address,description,reprName,reprPhone);
			}
			if (!ObjectUtils.isEmpty(scale)) {
				p = criteriaBuilder.and(p,criteriaBuilder.equal(root.get(School_.SCALE), scale));
			}
			if (!ObjectUtils.isEmpty(educationalLevel)) {
				p = criteriaBuilder.and(p,criteriaBuilder.equal(root.get(School_.EDUCATIONAL_LEVEL), educationalLevel));
			}
			if (!ObjectUtils.isEmpty(type)) {
				p = criteriaBuilder.and(p,criteriaBuilder.equal(root.get(School_.TYPE), type));
			}
			if (!ObjectUtils.isEmpty(schoolYear)) {
				p = criteriaBuilder.and(p,criteriaBuilder.notEqual(school_status.get(TargetSchool_.SCHOOL_YEAR), schoolYear));
			}
			if (!ObjectUtils.isEmpty(status)) {
				p = criteriaBuilder.and(p,criteriaBuilder.equal(school_status.get(SchoolStatus_.NAME), status));
			}
			if (!ObjectUtils.isEmpty(district)) {
				p = criteriaBuilder.and(p,criteriaBuilder.equal(school.get(District_.NAME), district));
			}
			p = criteriaBuilder.and(p, criteriaBuilder.isTrue(root.get(School_.IS_ACTIVE)));
				return p;
			}
		, paging(page, limit, column, direction));
		List<SchoolDTO> results = new ArrayList<SchoolDTO>();
		Paging<SchoolDTO> schoolPage = new Paging<SchoolDTO>();
		if (pageEntities.hasContent()) {
			pageEntities.getContent().forEach(school -> {
				SchoolDTO dto = Mapper.getMapper().map(school, SchoolDTO.class);
				dto.setDistrict(school.getDistrict().getName());
				dto.setStatus(school.getSchoolStatus().getName());
				dto.setEducationalLevel(school.getEducationalLevel().getValues());
				dto.setType(school.getType().getValues());
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
	 * @param dto: SchoolDTO object.
	 * @return boolean (true - success/ false - exception) 
	 * @throws SQLIntegrityConstraintViolationException 
	 */
	@Override
	public void insert(SchoolDTO dto)  {	
			School entity = Mapper.getMapper().map(dto, School.class);	
			entity.setType(SchoolType.valueOfLabel(dto.getType()));
			entity.setEducationalLevel(Level.valueOfLabel(dto.getEducationalLevel()));
			entity.setDistrict(districtRepo.findByName(dto.getDistrict()));
			entity.setSchoolStatus(statusRepo.findByName(dto.getStatus()));
			entity.setActive(true);
			repo.save(entity);
			}
		
	@Override
	 public void update(int id,SchoolDTO dto) {
		 if(repo.existsById(id)) {
			 School entity = Mapper.getMapper().map(dto, School.class);		
			 entity.setType(SchoolType.valueOfLabel(dto.getType()));
			 entity.setId(id);
				entity.setEducationalLevel(Level.valueOfLabel(dto.getEducationalLevel()));
				entity.setDistrict(districtRepo.findByName(dto.getDistrict()));
				entity.setSchoolStatus(statusRepo.findByName(dto.getStatus()));
				System.out.println(repo.save(entity).getName());
		 }
	 }
	 /**
	  * This method is used to delete a school by id - update isActive(false) column in database.
	  * @param int id: id of school.
	  * @exception if fail, throw the exception at controller to process.  
	  */
	@Override
	 public void delete(int id) {
		 School entity = repo.getOne(id);
		 entity.setActive(false);
		 repo.save(entity);
	 }
	@Override
	 public SchoolDTO getOne(int id) {
		 School school =  repo.getOne(id);
			SchoolDTO dto = Mapper.getMapper().map(school, SchoolDTO.class);
			dto.setDistrict(school.getDistrict().getName());
			dto.setStatus(school.getSchoolStatus().getName());
			dto.setEducationalLevel(school.getEducationalLevel().getValues());
			dto.setType(school.getType().getValues());
			return dto;
	 }
	@Override
	 public int saveAll(List<SchoolDTO> dtos) {
		 List<School> entities = new ArrayList<>();
		 dtos.forEach( dto -> {
			 School entity = Mapper.getMapper().map(dto, School.class);	
				entity.setType(SchoolType.valueOfLabel(dto.getType()));
				entity.setEducationalLevel(Level.valueOfLabel(dto.getEducationalLevel()));
				entity.setDistrict(districtRepo.findByName(dto.getDistrict()));
				entity.setSchoolStatus(statusRepo.findByName(dto.getStatus()));
				entity.setActive(true);
				entities.add(entity);
		 });
		return repo.saveAll(entities).size();
	 }
	public Paging<SchoolDTO> getSchoolForTarget(String district, String status, String type, String level,Scale scale,
			String schoolYear,int page, int limit, String column, String direction) {
		Page<School> pageEntities = (Page<School>)repo.findAll((Specification<School>) (root, query, builder) -> {
			Join<School,TargetSchool> target_school = root.join(School_.TARGET_SCHOOLS,JoinType.LEFT);
			Join<School, SchoolStatus> school_status = root.join(School_.SCHOOL_STATUS);
			Join<School, District> school = root.join(School_.DISTRICT);
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(district))
				p = builder.and(p, builder.equal(school.get(District_.NAME), district));
			if(!ObjectUtils.isEmpty(status))
				p = builder.and(p, builder.equal(school_status.get(SchoolStatus_.NAME),status));
			if(!ObjectUtils.isEmpty(level))
				p = builder.and(p, builder.equal(root.get(School_.EDUCATIONAL_LEVEL), Level.valueOfLabel(level)));
			if(!ObjectUtils.isEmpty(scale))
				p = builder.and(p, builder.equal(root.get(School_.SCALE), scale));	
			if(!ObjectUtils.isEmpty(type))
				p = builder.and(p, builder.equal(root.get(School_.TYPE), SchoolType.valueOfLabel(type)));		
			p = builder.and(p, builder.notEqual(target_school.get(TargetSchool_.SCHOOL_YEAR), schoolYear));
			return p;			
		}, paging(page, limit, column, direction));
		List<SchoolDTO> results = new ArrayList<SchoolDTO>();
		Paging<SchoolDTO> schoolPage = new Paging<SchoolDTO>();
		if (pageEntities.hasContent()) {
			pageEntities.getContent().forEach(school -> {
				SchoolDTO dto = Mapper.getMapper().map(school, SchoolDTO.class);
				dto.setDistrict(school.getDistrict().getName());
				dto.setStatus(school.getSchoolStatus().getName());
				dto.setEducationalLevel(school.getEducationalLevel().getValues());
				dto.setType(school.getType().getValues());
				results.add(dto);
				});
			schoolPage.setList(results);
			schoolPage.setTotalElements(pageEntities.getTotalElements());
			schoolPage.setTotalPage(pageEntities.getTotalPages());
		}
		return schoolPage;	
}
}


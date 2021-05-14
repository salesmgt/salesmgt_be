package com.app.demo.services.impls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.criteria.Join;
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

import com.app.demo.dtos.CommentRequest;
import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ReportDTO;
import com.app.demo.dtos.ReportDetails;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Report;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.Report_;
import com.app.demo.models.School;
import com.app.demo.models.Purpose;
import com.app.demo.models.Purpose_;
import com.app.demo.models.School_;
import com.app.demo.models.Task;
import com.app.demo.models.Task_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.ReportRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.services.IReportService;

@Service
public class ReportServiceImpl implements IReportService{
	@Autowired
	private ReportRepository repo;
	@Autowired
	private TaskRepository targetRepo;
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
	public Paging<ReportDTO> getReportByFilter(int targetId,String key,String district, String purpose, String fullName, String schoolYear,
			Date fromDate, Date toDate, int page, int limit, String column, String direction) {
		Page<Report> entities =  (Page<Report>) repo.findAll((Specification<Report>)(root,query,builder) -> {
			Join<Report, Task> report_target = root.join(Report_.TASK);
			Join<Task, User> target_user = report_target.join(Task_.USER);
			Join<Task, School> target_school = report_target.join(Task_.SCHOOL);
			Join<School, District> school_district = target_school.join(School_.DISTRICT);
			Join<Task, Purpose> target_purpose = report_target.join(Task_.PURPOSE);
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				Predicate schoolName = builder.like(target_school.get(School_.NAME), "%" + key + "%");	
				Predicate fullname = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				Predicate username = builder.like(target_user.get(User_.USERNAME), "%" + key + "%");
				Predicate cmt = builder.like(root.get(Report_.SUPERVISOR_COMMENT), "%" + key + "%");
				Predicate description = builder.like(root.get(Report_.DESCRIPTION), "%" + key + "%");
				Predicate name = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				p = builder.or(schoolName,fullname,username,cmt,description,name);
			}
			if (targetId != 0) {
				p = builder.and(p,builder.equal(report_target.get(Task_.ID), targetId));
			}
			if (!ObjectUtils.isEmpty(district)) {
				p = builder.and(p,builder.equal(school_district.get(District_.NAME), district));
			}
			if (!ObjectUtils.isEmpty(purpose)) {
				p = builder.and(p,builder.equal(target_purpose.get(Purpose_.NAME), purpose));
			}
			if (!ObjectUtils.isEmpty(schoolYear)) {
				p = builder.and(p,builder.equal(report_target.get(Task_.SCHOOL_YEAR), schoolYear));
			}
			if (!ObjectUtils.isEmpty(fullName)) {
				p = builder.and(p,builder.equal(target_user.get(User_.FULL_NAME), fullName));
			}
			if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
				p = builder.and(p,builder.between(root.get(Report_.DATE),fromDate,toDate));
			}
			
			return p;
		},paging(page, limit, column, direction));
		Paging<ReportDTO> reportPage = new Paging<ReportDTO>();
		if(entities.hasContent()){
		reportPage.setList(convertToDTO(entities.getContent()));
		reportPage.setTotalElements(entities.getTotalElements());
		reportPage.setTotalPage(entities.getTotalPages());
		}
		return reportPage;
	}
	private List<ReportDTO> convertToDTO(List<Report> entities) {
		final List<ReportDTO> dtos = new ArrayList<ReportDTO>();
		if (!ObjectUtils.isEmpty(entities)) {
			entities.forEach(item -> {
				ReportDTO dto = Mapper.getMapper().map(item, ReportDTO.class);
				dto.setId(item.getId());
				dto.setFullName(item.getTask().getUser().getFullName());
				dto.setUsername(item.getTask().getUser().getUsername());
				dto.setAddress(item.getTask().getSchool().getAddress());
				dto.setAvatar(item.getTask().getUser().getAvatar());
				dto.setDistrict(item.getTask().getSchool().getDistrict().getName());
				dto.setReprName(item.getTask().getSchool().getReprName());
				dto.setReprIsMale(item.getTask().getSchool().isReprIsMale());
				dto.setLevel(item.getTask().getSchool().getEducationalLevel().getName());
				dto.setSchoolName(item.getTask().getSchool().getName());
				dto.setSchoolYear(item.getTask().getSchoolYear());
				dto.setTargetId(item.getTask().getId());
				dto.setPurpose(item.getTask().getPurpose().getName());
				String cmt = item.getSupervisorComment();
				if(!ObjectUtils.isEmpty(cmt)) {
				String supervisor = cmt.substring(1,cmt.indexOf("]"));
				dto.setCommentedPerson(supervisor);
				dto.setContextComments(cmt.substring(cmt.indexOf("]")+2, cmt.length()));
				}
				dtos.add(dto);
			});			
		};
		return dtos;
}
	@Override
	public Report update(int id,ReportDetails dto) {
		Report entity = repo.getOne(id);
		entity.setId(id);
		entity.setDifficulty(dto.getDifficulty());
		entity.setFuturePlan(dto.getFuturePlan());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setPositivity(dto.getPositivity());
		entity.setSuccess(dto.isSuccess());
		return repo.save(entity);		
	}
	@Override
	public Report delete(int id) {
		Report report = repo.getOne(id);
		repo.delete(report);
		return report;
	}
	@Transactional
	@Override
	public String insert(List<ReportDTO> request) {
		List<Report> list = new ArrayList<>();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		List<Report> dupplicate = null;
		try {
			
		for (ReportDTO dto : request) {
			dupplicate = repo.findReportByDateAndTarget(dto.getDate().substring(0,10), dto.getTargetId());
			System.out.println(dupplicate);
			if(!ObjectUtils.isEmpty(dupplicate)){
				return "This school ["+dupplicate.get(0).getTask().getSchool().getName()+"] have already submitted report";
			}
			Report entity = new Report();
			Task target = targetRepo.getOne(dto.getTargetId());
			entity.setDifficulty(dto.getDifficulty());
			entity.setFuturePlan(dto.getFuturePlan());
			entity.setDescription(dto.getDescription());
			entity.setDate(sdf
			        .parse(dto.getDate()));
			entity.setPositivity(dto.getPositivity());
			entity.setSuccess(dto.isSuccess());
			entity.setTask(target);
			if(!ObjectUtils.isEmpty(dto.getCommentedPerson())){
				entity.setSupervisorComment(dto.toString());
				}
			list.add(entity);
		};
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "Created "+repo.saveAll(list).size()+" records";
	}
	@Override
	public ReportDTO getOne(int id) {
		Report item = repo.getOne(id);
		ReportDTO dto = Mapper.getMapper().map(item, ReportDTO.class);
		dto.setId(item.getId());
		dto.setPurpose(item.getTask().getPurpose().getName());
		dto.setUsername(item.getTask().getUser().getUsername());
		dto.setAddress(item.getTask().getSchool().getAddress());
		dto.setAvatar(item.getTask().getUser().getAvatar());
		dto.setDistrict(item.getTask().getSchool().getDistrict().getName());
		dto.setReprName(item.getTask().getSchool().getReprName());
		dto.setReprIsMale(item.getTask().getSchool().isReprIsMale());
		dto.setLevel(item.getTask().getSchool().getEducationalLevel().getName());
		dto.setSchoolName(item.getTask().getSchool().getName());
		dto.setSchoolYear(item.getTask().getSchoolYear());
		dto.setFullName(item.getTask().getUser().getFullName());
		dto.setTargetId(item.getTask().getId());
		String cmt = item.getSupervisorComment();
		if(!ObjectUtils.isEmpty(cmt)) {
		String supervisor = cmt.substring(1,cmt.indexOf("]"));
		dto.setCommentedPerson(supervisor);
		dto.setContextComments(cmt.substring(cmt.indexOf("]")+2, cmt.length()));
		}
		return dto;
	}
	@Override
	public void updateComment(int id,CommentRequest dto) {
		Report report = repo.getOne(id);
		if(!ObjectUtils.isEmpty(dto.getCommentedPerson()))
		report.setSupervisorComment(dto.toString());
		repo.save(report);
	}
}

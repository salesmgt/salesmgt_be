package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.Date;
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

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.ReportDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Report;
import com.app.demo.models.District;
import com.app.demo.models.District_;
import com.app.demo.models.Report_;
import com.app.demo.models.School;
import com.app.demo.models.TargetPurpose;
import com.app.demo.models.TargetPurpose_;
import com.app.demo.models.School_;
import com.app.demo.models.TargetSchool;
import com.app.demo.models.TargetSchool_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.ReportRepository;
import com.app.demo.services.IReportService;

@Service
public class ReportServiceImpl implements IReportService{
	@Autowired
	private ReportRepository repo;
	
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
	public Paging<ReportDTO> getReportByFilter(String key,String district, String purpose, String fullName, String schoolYear,
			Date fromDate, Date toDate, int page, int limit, String column, String direction) {
		Page<Report> entities =  (Page<Report>) repo.findAll((Specification<Report>)(root,query,builder) -> {
			Join<Report, TargetSchool> report_target = root.join(Report_.TARGET_SCHOOL);
			Join<TargetSchool, User> target_user = report_target.join(TargetSchool_.USER);
			Join<TargetSchool, School> target_school = report_target.join(TargetSchool_.SCHOOL);
			Join<School, District> school_district = target_school.join(School_.DISTRICT);
			Join<TargetSchool, TargetPurpose> target_purpose = report_target.join(TargetSchool_.TARGET_PURPOSE);
			Predicate p = builder.conjunction();
			if(!ObjectUtils.isEmpty(key)) {
				Predicate schoolName = builder.like(target_school.get(School_.NAME), "%" + key + "%");	
				Predicate fullname = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				Predicate username = builder.like(target_user.get(User_.USERNAME), "%" + key + "%");
				Predicate result = builder.like(root.get(Report_.RESULT), "%" + key + "%");
				Predicate cmt = builder.like(root.get(Report_.SUPERVISOR_COMMENT), "%" + key + "%");
				Predicate description = builder.like(root.get(Report_.DESCRIPTION), "%" + key + "%");
				Predicate name = builder.like(target_user.get(User_.FULL_NAME), "%" + key + "%");
				p = builder.or(schoolName,fullname,username,result,cmt,description,name);
			}
			if (!ObjectUtils.isEmpty(district)) {
				p = builder.and(p,builder.equal(school_district.get(District_.NAME), district));
			}
			if (!ObjectUtils.isEmpty(purpose)) {
				p = builder.and(p,builder.equal(target_purpose.get(TargetPurpose_.NAME), purpose));
			}
			if (!ObjectUtils.isEmpty(schoolYear)) {
				p = builder.and(p,builder.equal(report_target.get(TargetSchool_.SCHOOL_YEAR), schoolYear));
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
				dto.setUsername(item.getTargetSchool().getUser().getUsername());
				dto.setAddress(item.getTargetSchool().getSchool().getAddress());
				dto.setAvatar(item.getTargetSchool().getUser().getAvatar());
				dto.setDistrict(item.getTargetSchool().getSchool().getDistrict().getName());
				dto.setReprName(item.getTargetSchool().getSchool().getReprName());
				dto.setReprGender(item.getTargetSchool().getSchool().isReprGender());
				dto.setLevel(item.getTargetSchool().getSchool().getEducationalLevel().getValues());
				dto.setSchoolName(item.getTargetSchool().getSchool().getName());
				dto.setSchoolYear(item.getTargetSchool().getSchoolYear());
				String cmt = item.getSupervisorComment();
				if(!ObjectUtils.isEmpty(cmt)) {
				String supervisor = cmt.substring(1,cmt.indexOf("]"));
				dto.setCommentedPerson(supervisor);
				dto.setContextComments(cmt.substring(cmt.indexOf("]")+1, cmt.length()));
				}
				dtos.add(dto);
			});			
		};
		return dtos;
}
}

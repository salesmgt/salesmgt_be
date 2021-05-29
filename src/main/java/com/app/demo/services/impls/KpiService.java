package com.app.demo.services.impls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.KpiDTO;
import com.app.demo.dtos.KpiDetailDTO;
import com.app.demo.dtos.KpiDetailsDTO;
import com.app.demo.dtos.KpiGroupDTO;
import com.app.demo.dtos.KpiGroupDetails;
import com.app.demo.dtos.KpiInsertObject;
import com.app.demo.dtos.KpiUser;
import com.app.demo.dtos.KpiUserDetails;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Criteria;
import com.app.demo.models.Kpi;
import com.app.demo.models.KpiDetails;
import com.app.demo.models.KpiGroup;
import com.app.demo.models.KpiGroup_;
import com.app.demo.repositories.CriteriaRepository;
import com.app.demo.repositories.KpiDetailsRepository;
import com.app.demo.repositories.KpiGroupRepository;
import com.app.demo.repositories.KpiRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.IKpiService;

@Service
public class KpiService implements IKpiService {
	@Autowired
	private KpiRepository repo;
	
	@Autowired
	private KpiGroupRepository groupRepo;
	
	@Autowired
	private KpiDetailsRepository detailRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CriteriaRepository criRepo;
	
	@Override
	public void insert(KpiInsertObject request) {
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		sdff.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		KpiGroup group = new KpiGroup();
		try {
			group.setStartDate(sdff.parse(request.getStartDate()));
			group.setEndDate(sdff.parse(request.getEndDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		group.setGroupName(request.getGroupName());
		group.setActive(true);
		groupRepo.save(group);
		List<Kpi> inserKPIs = new ArrayList<>();
		List<KpiDetails> inserKPIDetails = new ArrayList<>();
		for (KpiUser user : request.getKpis()) {
			Kpi kpi = new Kpi();			
			kpi.setUser(userRepo.getOne(user.getUsername()));
			kpi.setKpiGroup(group);
			inserKPIs.add(kpi);
			repo.save(kpi);
			for (KpiDetailDTO kpis :user.getKpiDetails()) {
				KpiDetails details = new KpiDetails(); 
				Criteria criteria = criRepo.getOne(kpis.getCriteriaId());
				details.setCriteria(criteria);
				details.setTargetValue(kpis.getTargetValue());
				details.setWeight(kpis.getWeight());
				details.setKpi(kpi);
				inserKPIDetails.add(details);
			}
			detailRepo.saveAll(inserKPIDetails);		
		}
	}
		
	@Override
	public List<KpiGroupDTO> getAll(String status,String key) {
		List<KpiGroup> list = groupRepo.findAll((Specification<KpiGroup>) (root, query, criteriaBuilder) -> {
			Predicate p = criteriaBuilder.conjunction();
			if (!ObjectUtils.isEmpty(key)) {
				p = criteriaBuilder.and(p,criteriaBuilder.like(root.get(KpiGroup_.GROUP_NAME),"%" + key + "%"));
			}
			if (!ObjectUtils.isEmpty(status)) {
				if(status.equalsIgnoreCase("Being applied"))
					p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get(KpiGroup_.END_DATE),new Date()));
				else if(status.equalsIgnoreCase("Expired"))
				p = criteriaBuilder.and(p,criteriaBuilder.lessThanOrEqualTo(root.get(KpiGroup_.END_DATE),new Date()));
			}
			p = criteriaBuilder.and(p, criteriaBuilder.isTrue(root.get(KpiGroup_.IS_ACTIVE)));
			return p;
		});
		List<KpiGroupDTO> dtos = new ArrayList<>();
		for (KpiGroup item : list) {
			KpiGroupDTO dto = Mapper.getMapper().map(item, KpiGroupDTO.class);
			dto.setId(item.getId());
			dto.setSize(item.getKpis().size());
			dtos.add(dto);
		}
	return dtos;
	}
	@Override
	public KpiGroupDetails getOneKpiGroup(int groupId){
		KpiGroup group = groupRepo.getOne(groupId);
		List<Kpi> kpis = group.getKpis();
		KpiGroupDetails result = new KpiGroupDetails();
		List<KpiDTO> kpiDTOs = new ArrayList<>();
		List<KpiDetailsDTO> kpiDetailsDTO = new ArrayList<KpiDetailsDTO>();
		if(!ObjectUtils.isEmpty(kpis)) { //tính điểm tb cho từng tiêu chí, tính tổng số kpi cho user
			
			for (Kpi kpi : kpis) { //tính điểm mỗi username
				KpiDTO kpiDTO = new KpiDTO(kpi.getId(),kpi.getUser().getUsername(),kpi.getUser().getFullName(), kpi.getUser().getAvatar(),
						0, group.getStartDate(), group.getEndDate());
				double value = 0;
				for (KpiDetails item : kpi.getKpiDetails()) { //tinh điểm mỗi tiêu chí
//					if(item.getCriteria().getType().equalsIgnoreCase("minus"))
//					value = value - item.getWeight()*item.getActualValue()*100/item.getTargetValue();
//					else
						value = value + item.getWeight()*item.getActualValue()*100/item.getTargetValue();
					boolean dup = false;
					double citeriaValue = item.getActualValue()*100/item.getTargetValue();
					for (KpiDetailsDTO kpiDetailDTO : kpiDetailsDTO) {
						if(kpiDetailDTO.getCriteriaId().equalsIgnoreCase(item.getCriteria().getId())) {
							kpiDetailDTO.setValue(kpiDetailDTO.getValue()+citeriaValue);		
							dup =true;
						}
					}
					if(!dup) {
						KpiDetailsDTO criteria = new KpiDetailsDTO(item.getCriteria().getId(),item.getCriteria().getName(),
								citeriaValue,
								item.getCriteria().getType());
						kpiDetailsDTO.add(criteria);
					}	
				}
				kpiDTO.setValue(value);
				kpiDTOs.add(kpiDTO);
			}
			Collections.sort(kpiDTOs);
			result.setKpis(kpiDTOs);
			kpiDetailsDTO.forEach(item -> item.setValue(item.getValue()/kpiDetailsDTO.size()));
			Collections.sort(kpiDetailsDTO);
			result.setKpiDetails(kpiDetailsDTO);		
		}
		return result;
	}
	@Override
	public KpiUserDetails getByKpiId(int kpiId) {
		Kpi kpi = repo.getOne(kpiId);
		KpiUserDetails kpiUser = new KpiUserDetails();
		kpiUser.setUsername(kpi.getUser().getUsername());
		kpiUser.setAvatar(kpi.getUser().getAvatar());
		kpiUser.setFullName(kpi.getUser().getFullName());
		kpiUser.setStartDate(kpi.getKpiGroup().getStartDate());
		kpiUser.setEndDate(kpi.getKpiGroup().getEndDate());
		List<KpiDetailDTO> kpis = new ArrayList<>();
		for (KpiDetails item : kpi.getKpiDetails()) {
			KpiDetailDTO detail = new KpiDetailDTO();
			detail.setCriteriaId(detail.getCriteriaId());
			detail.setKpiDetailId(item.getId());
			detail.setCirteriaContent(item.getCriteria().getName());
			detail.setWeight(item.getWeight());
			detail.setTargetValue(item.getTargetValue());
			detail.setType(item.getCriteria().getType());
			detail.setActualValue(item.getActualValue());
			kpis.add(detail);
		}
		 double total =	kpis.stream().mapToDouble(item ->item.getWeight()*item.getActualValue()*100/item.getTargetValue()).sum();
		kpiUser.setKpis(kpis);
		kpiUser.setTotal(total);
		return kpiUser;
	}
}
package com.app.demo.services.impls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.joda.time.DateTime;
import org.joda.time.Days;
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
import com.app.demo.models.Kpi_;
import com.app.demo.models.Purpose;
import com.app.demo.models.Purpose_;
import com.app.demo.models.Report;
import com.app.demo.models.Report_;
import com.app.demo.models.Service_;
import com.app.demo.models.Task;
import com.app.demo.models.Task_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.CriteriaRepository;
import com.app.demo.repositories.KpiDetailsRepository;
import com.app.demo.repositories.KpiGroupRepository;
import com.app.demo.repositories.KpiRepository;
import com.app.demo.repositories.ReportRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.IKpiService;
import com.app.demo.utils.CalculateDays;

@Service
public class KpiService implements IKpiService {
	@Autowired
	private KpiRepository repo;
	
	@Autowired
	private ReportRepository reportRepo;
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private KpiGroupRepository groupRepo;
	
	@Autowired
	private KpiDetailsRepository detailRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CriteriaRepository criRepo;
	@Autowired
	private ServiceRepository serviceRepo;
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
				else
					p = criteriaBuilder.and(p, criteriaBuilder.isFalse(root.get(KpiGroup_.IS_ACTIVE)));
			}
			return p;
		});
		List<KpiGroupDTO> dtos = new ArrayList<>();
		
		for (KpiGroup item : list) {
			KpiGroupDTO dto = Mapper.getMapper().map(item, KpiGroupDTO.class);
			dto.setId(item.getId());
			List<String> size = new ArrayList<>();
			for (Kpi kpi : item.getKpis()) {
				size.add(kpi.getUser().getAvatar());
			}
			dto.setSize(size);
			dtos.add(dto);
		}
	return dtos;
	}
	@Override
	public void updateKPIManual(int detailId,double request) {
		KpiDetails detail = detailRepo.getOne(detailId);
		detail.setActualValue(request);
		detailRepo.save(detail);
	}
	@Override
	public KpiGroupDetails getOneKpiGroup(int groupId){
		KpiGroup group = groupRepo.getOne(groupId);
		List<Kpi> kpis = group.getKpis();
		KpiGroupDetails result = new KpiGroupDetails();
		List<KpiDTO> kpiDTOs = new ArrayList<>();
		List<KpiDetailsDTO> kpiDetailsDTO = new ArrayList<KpiDetailsDTO>();
		if(!ObjectUtils.isEmpty(kpis)) { //tính điểm tb cho từng tiêu chí, tính tổng số kpi cho user
			Date start = group.getStartDate();
			Date end = group.getEndDate();
			for (Kpi kpi : kpis) { //tính điểm mỗi username
				String username = kpi.getUser().getUsername();
				KpiDTO kpiDTO = new KpiDTO(kpi.getId(),kpi.getUser().getUsername(),kpi.getUser().getFullName(), kpi.getUser().getAvatar(),
						0, group.getStartDate(), group.getEndDate());
				double value = 0;
				for (KpiDetails item : kpi.getKpiDetails()) { //tinh điểm mỗi tiêu chí
					double citeriaValue = 0;
					double target = item.getTargetValue();
					double weight = item.getWeight();
					//
					if(group.isActive()&&group.getEndDate().after(new Date())){ //trường hợp còn hiện hành thì chỉ get thống kê
						
						switch (item.getCriteria().getId()) {
						case "DS":
							value = value + weight*calculateDoanhso(username, start,end)*100/target;						
							citeriaValue = calculateDoanhso(username,  start,end)*100/target;
							break;	
						case "NS":
							value = value + weight*calculateSoServiceSale(username,  start,end)*100/target;
							citeriaValue =calculateSoServiceSale(username,  start,end)*100/target;
							break;
						case "PCT":
							value = value + weight*caluculeTaskPercent(username,  start,end)*100/target;
							citeriaValue = caluculeTaskPercent(username,  start,end)*100/target;
							break;
						case "RP":
							value = value + weight*calculateReport(username,start,end)*100/target;
							citeriaValue = calculateReport(username,  start,end)*100/target;
							break;
						case "PRP":
							value = value + weight*calculateReportSuccess(username,start,end)*100/target;
							citeriaValue = calculateReportSuccess(username,  start,end)*100/target;
							break;
						case "PS":
							value = value + weight*calculateSaleSucessPercent(username,start,end)*100/target;
							citeriaValue = calculateSaleSucessPercent(username,  start,end)*100/target;
							break;
						default:
							value = value +weight*item.getActualValue()*100/target;
							citeriaValue = item.getActualValue()*100/target;
							break;
						}
					}
					//
					else {
						value = value + item.getWeight()*item.getActualValue()*100/target;
						citeriaValue = item.getActualValue()*100/target;
					}
					boolean dup = false;
					
					for (KpiDetailsDTO kpiDetailDTO : kpiDetailsDTO) {
						
						if(kpiDetailDTO.getCriteriaId().equalsIgnoreCase(item.getCriteria().getId())) {
							kpiDetailDTO.setValue(kpiDetailDTO.getValue()+citeriaValue);		
							dup =true;
						}
					}
					if(!dup) {
						KpiDetailsDTO criteria = new KpiDetailsDTO(item.getCriteria().getId(),item.getCriteria().getName(),
								citeriaValue,
								item.getCriteria().getType(),item.getWeight(),item.getCriteria().getDescription());
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
			result.setId(groupId);
			result.setStartDate(group.getStartDate());
			result.setEndDate(group.getEndDate());
			result.setGroupName(group.getGroupName());
		}
		return result;
	}
	@Override
	public KpiUserDetails getByKpiId(int kpiId) {
		Kpi kpi = repo.getOne(kpiId);
		String username = kpi.getUser().getUsername();
		KpiUserDetails kpiUser = new KpiUserDetails();
		kpiUser.setUsername(kpi.getUser().getUsername());
		kpiUser.setAvatar(kpi.getUser().getAvatar());
		kpiUser.setFullName(kpi.getUser().getFullName());
		kpiUser.setStartDate(kpi.getKpiGroup().getStartDate());
		kpiUser.setEndDate(kpi.getKpiGroup().getEndDate());
		List<KpiDetailDTO> kpis = new ArrayList<>();
		KpiGroup group = kpi.getKpiGroup();
		Date start = kpi.getKpiGroup().getStartDate();
		Date end = kpi.getKpiGroup().getEndDate();
		for (KpiDetails item : kpi.getKpiDetails()) {
			KpiDetailDTO detail = new KpiDetailDTO();
			double actualValue = 0;
			detail.setCriteriaId(detail.getCriteriaId());
			detail.setKpiDetailId(item.getId());
			detail.setCirteriaContent(item.getCriteria().getName());
			detail.setWeight(item.getWeight());
			detail.setTargetValue(item.getTargetValue());
			detail.setType(item.getCriteria().getType());
			detail.setDescription(item.getCriteria().getDescription());
			if(group.isActive()&&group.getEndDate().after(new Date())) {
				
				switch (item.getCriteria().getId()) {
				case "DS":						
					actualValue = calculateDoanhso(username, start,end);
					break;	
				case "NS":
					actualValue =calculateSoServiceSale(username, start,end);
					break;
				case "PCT":
					actualValue = caluculeTaskPercent(username, start,end);
					break;
				case "RP":
					actualValue = calculateReport(username,start,end);
					break;
				case "PRP":
					actualValue = calculateReportSuccess(username,start,end);
					break;
				case "PS":
					actualValue = calculateSaleSucessPercent(username,  start,end);
					break;
				default:
					actualValue = item.getActualValue();
					break;
				}
			}else {
				actualValue = item.getActualValue();
			}
			
			detail.setActualValue(actualValue);
			kpis.add(detail);
			
		}
		 double total =	kpis.stream().mapToDouble(item ->item.getWeight()*item.getActualValue()*100/item.getTargetValue()).sum();
		kpiUser.setKpis(kpis);
		kpiUser.setTotal(total);
		return kpiUser;
	}
	@Override
	public void setDisbale(int id) {
		KpiGroup group = groupRepo.getOne(id);
		group.setActive(false);
		groupRepo.save(group);
	}
	@Override
	public List<KpiGroupDTO> getGroupByUsername(String username,String status){
		List<KpiGroup> list = groupRepo.findAll((Specification<KpiGroup>) (root, query, criteriaBuilder) -> {
			Join<KpiGroup, Kpi> group_kpi = root.join(KpiGroup_.KPIS);
			Join<Kpi, User> group_kpi_user = group_kpi.join(Kpi_.USER);
			Predicate p = criteriaBuilder.conjunction();
			if (!ObjectUtils.isEmpty(status)) {
				if(status.equalsIgnoreCase("Being applied"))
				p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get(KpiGroup_.END_DATE),new Date()));
				else if(status.equalsIgnoreCase("Expired"))
				p = criteriaBuilder.and(p,criteriaBuilder.lessThanOrEqualTo(root.get(KpiGroup_.END_DATE),new Date()));
				else
					p = criteriaBuilder.and(p, criteriaBuilder.isFalse(root.get(KpiGroup_.IS_ACTIVE)));
			}
			p = criteriaBuilder.and(p,
					criteriaBuilder.equal(group_kpi_user.get(User_.USERNAME), username));
			return p;
		});
		List<KpiGroupDTO> dtos = new ArrayList<>();
		for (KpiGroup item : list) {
			KpiGroupDTO dto = Mapper.getMapper().map(item, KpiGroupDTO.class);
			dto.setId(item.getId());
			List<String> size = new ArrayList<>();
			for (Kpi kpi : item.getKpis()) {
				size.add(kpi.getUser().getAvatar());
			}
			dto.setSize(size);
			dtos.add(dto);
		}
	return dtos;
	}
	private double calculateDoanhso(String username,Date start,Date end) {
		List<com.app.demo.models.Service> services = serviceRepo.findAll((Specification<com.app.demo.models.Service>) (root, query, criteriaBuilder) -> {
			Join<Service,Task> service_task = root.join(Service_.TASK);
			Join<Task,User> service_task_user = service_task.join(Task_.USER);
			Predicate p = criteriaBuilder.conjunction();
			p= criteriaBuilder.and(p,criteriaBuilder.equal(root.get(Service_.STATUS),"approved"));
			p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get(Service_.APPROVE_DATE),start));
			p= criteriaBuilder.and(p,criteriaBuilder.lessThanOrEqualTo(root.get(Service_.APPROVE_DATE),end));
			p= criteriaBuilder.and(p,criteriaBuilder.equal(service_task_user.get(User_.USERNAME),username));
			return p;
		});
		double value = 0;
		if(!ObjectUtils.isEmpty(services)) {
			
			for (com.app.demo.models.Service service : services) {
				 int days = Days.daysBetween(new DateTime(service.getStartDate()),
						 new DateTime(service.getEndDate())).getDays();
				 double week = Math.ceil(days/7);
				value = value +service.getPricePerSlot()*service.getSlotNumber() * service.getClassNumber()*week;	
			}
		
		}
		return value;
	}
	private double calculateSoServiceSale(String username, Date start, Date end) {
		double value = 0;
		List<com.app.demo.models.Service> services = serviceRepo.findAll((Specification<com.app.demo.models.Service>) (root, query, criteriaBuilder) -> {
			Join<Service,Task> service_task = root.join(Service_.TASK);
			Join<Task,User> service_task_user = service_task.join(Task_.USER);
			Predicate p = criteriaBuilder.conjunction();
			p= criteriaBuilder.and(p,criteriaBuilder.equal(root.get(Service_.STATUS),"approved"));
			p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get(Service_.SUBMIT_DATE),start));
			p= criteriaBuilder.and(p,criteriaBuilder.lessThanOrEqualTo(root.get(Service_.SUBMIT_DATE),end));
			p= criteriaBuilder.and(p,criteriaBuilder.equal(service_task_user.get(User_.USERNAME),username));
			return p;
		});
		if(!ObjectUtils.isEmpty(services))
			value = services.size();
		return value;
	}
	private double caluculeTaskPercent(String username, Date start, Date end) {
		double value = 0;
		List<Task> tasks = taskRepo.findAll((root, query, builder) -> {
			Join<Task, User> target_user = root.join(Task_.USER);
			Predicate p = builder.conjunction();
			p= builder.and(p,builder.lessThanOrEqualTo(root.get(Task_.COMPLETED_DATE),end)); 
			p= builder.and(p,builder.greaterThanOrEqualTo(root.get(Task_.COMPLETED_DATE),start)); 
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			p = builder.and(p, builder.equal(root.get(Task_.RESULT), "successful"));
			return p;
		});
		List<Task> taskAll = taskRepo.findAll((root, query, builder) -> {
			Join<Task, User> target_user = root.join(Task_.USER);
			Predicate p = builder.conjunction();
			
			Predicate th1 = builder.conjunction();
			th1 = builder.between(root.get(Task_.ASSIGN_DATE), start, end);
			th1 = builder.and(p, builder.between(root.get(Task_.END_DATE), start, end));

			Predicate th21 = builder.lessThanOrEqualTo(root.get(Task_.ASSIGN_DATE), start);
			Predicate th22 = builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), start);
			Predicate th2 = builder.and(th21, th22);

			Predicate th31 = builder.lessThanOrEqualTo(root.get(Task_.ASSIGN_DATE), end);
			Predicate th32 = builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), end);
			Predicate th3 = builder.and(th31, th32);
			
			Predicate th41 = builder.lessThanOrEqualTo(root.get(Task_.ASSIGN_DATE), start);
			Predicate th42 = builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), end);
			Predicate th4 = builder.and(th41, th42);
			
			Predicate th = builder.or(th1, th2, th3, th4);
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			p = builder.and(p, th);
			return p;
		});
		if(!ObjectUtils.isEmpty(tasks))
			value = tasks.size()*100/taskAll.size();
		return value;
	}
	private double calculateReport(String username, Date start, Date end) {
		double value = 0;
		List<Report> reports = reportRepo.findAll((root,query,builder) -> {
			Join<Report, Task> report_target = root.join(Report_.TASK);
			Join<Task, User> target_user = report_target.join(Task_.USER);
			Predicate p = builder.conjunction();
			p = builder.and(p,builder.equal(target_user.get(User_.USERNAME), username));
			p = builder.and(p,builder.between(root.get(Report_.DATE),start,end));
			return p;
		});
		if(!ObjectUtils.isEmpty(reports))
			value = reports.size()/CalculateDays.daysWithoutWeekendDays(start, end);
		return value;
	}
	
	private double calculateReportSuccess(String username, Date start, Date end) {
		double value = 0;
		List<Report> reports = reportRepo.findAll((root,query,builder) -> {
			Join<Report, Task> report_target = root.join(Report_.TASK);
			Join<Task, User> target_user = report_target.join(Task_.USER);
			Predicate p = builder.conjunction();
			p = builder.and(p,builder.equal(target_user.get(User_.USERNAME), username));
			p = builder.and(p,builder.between(root.get(Report_.DATE),start,end));
			p = builder.and(p,builder.isTrue(root.get(Report_.IS_SUCCESS)));
			return p;
		});
		if(!ObjectUtils.isEmpty(reports))
			value = reports.size()/CalculateDays.daysWithoutWeekendDays(start, end);
		return value;
	}
	private double calculateSaleSucessPercent(String username,Date start,Date end) {
		double value = 0;
		List<Task> tasks = taskRepo.findAll((Specification<Task>) (root, query, builder) -> {
			Join<Task, User> target_user = root.join(Task_.USER);
			Join<Task, Purpose> target_purpose = root.join(Task_.PURPOSE);
			Predicate p = builder.conjunction();
			Predicate newSales = builder.equal(target_purpose.get(Purpose_.NAME),"Sales mới");
			Predicate taiKy = builder.equal(target_purpose.get(Purpose_.NAME),"Tái ký hợp đồng");
			Predicate kyMoi = builder.equal(target_purpose.get(Purpose_.NAME),"Ký mới hợp đồng");
			p = builder.or(newSales,taiKy,kyMoi);
			p = builder.and(p, builder.equal(root.get(Task_.RESULT), "successful"));
			p= builder.and(p,builder.lessThanOrEqualTo(root.get(Task_.COMPLETED_DATE),end)); 
			p= builder.and(p,builder.greaterThanOrEqualTo(root.get(Task_.COMPLETED_DATE),start)); 
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			return p;
		});
		List<Task> taskAll = taskRepo.findAll((Specification<Task>) (root, query, builder) -> {
			Join<Task, User> target_user = root.join(Task_.USER);
			Join<Task, Purpose> target_purpose = root.join(Task_.PURPOSE);
			Predicate p = builder.conjunction();
			Predicate newSales = builder.equal(target_purpose.get(Purpose_.NAME),"Sales mới");
			Predicate taiKy = builder.equal(target_purpose.get(Purpose_.NAME),"Tái ký hợp đồng");
			Predicate kyMoi = builder.equal(target_purpose.get(Purpose_.NAME),"Ký mới hợp đồng");
			p = builder.or(newSales,taiKy,kyMoi); 
			
			Predicate th1 = builder.conjunction();
			th1 = builder.between(root.get(Task_.ASSIGN_DATE), start, end);
			th1 = builder.and(p, builder.between(root.get(Task_.END_DATE), start, end));

			Predicate th21 = builder.lessThanOrEqualTo(root.get(Task_.ASSIGN_DATE), start);
			Predicate th22 = builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), start);
			Predicate th2 = builder.and(th21, th22);

			Predicate th31 = builder.lessThanOrEqualTo(root.get(Task_.ASSIGN_DATE), end);
			Predicate th32 = builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), end);
			Predicate th3 = builder.and(th31, th32);
			
			Predicate th41 = builder.lessThanOrEqualTo(root.get(Task_.ASSIGN_DATE), start);
			Predicate th42 = builder.greaterThanOrEqualTo(root.get(Task_.END_DATE), end);
			Predicate th4 = builder.and(th41, th42);
			
			Predicate th = builder.or(th1, th2, th3, th4);
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			p = builder.and(p, th);
			return p;
		});
		if(!ObjectUtils.isEmpty(tasks))
			value = tasks.size()*100/taskAll.size();
		return value;
	}
}
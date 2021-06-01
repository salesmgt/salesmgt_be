package com.app.demo.schedules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.util.ObjectUtils;

import com.app.demo.models.Kpi;
import com.app.demo.models.KpiDetails;
import com.app.demo.models.KpiGroup;
import com.app.demo.models.KpiGroup_;
import com.app.demo.models.Purpose;
import com.app.demo.models.Purpose_;
import com.app.demo.models.Report;
import com.app.demo.models.Report_;
import com.app.demo.models.School;
import com.app.demo.models.Service;
import com.app.demo.models.Service_;
import com.app.demo.models.Task;
import com.app.demo.models.Task_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.KpiGroupRepository;
import com.app.demo.repositories.KpiRepository;
import com.app.demo.repositories.KpiDetailsRepository;
import com.app.demo.repositories.ReportRepository;
import com.app.demo.repositories.SchoolRepository;
import com.app.demo.repositories.SchoolStatusRepository;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.utils.CalculateDays;

@Configuration
@EnableScheduling
public class ServiceUpdateSchedule /* implements SchedulingConfigurer */ {

	@Autowired
	private ServiceRepository repo;
	
	@Autowired
	private SchoolStatusRepository statusRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private KpiRepository kpiRepo;
	@Autowired
	private KpiDetailsRepository kpiDetailRepo;

	@Autowired
	private ReportRepository reportRepo;

	@Autowired
	private TaskRepository targetRepo;
	
	@Autowired
	private KpiGroupRepository groupRepo;
	
	@Autowired
	private SchoolRepository schoolRepo;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
	
//	@Scheduled(cron = "0 59 23 * * 1-6", zone = "Asia/Saigon")
//	public void calculateKPI()  {
//		Date now = new Date();	
//		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
//		String strNowDate = sdff.format(now);
//		Calendar c = Calendar.getInstance();
//		c.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
//		c.setTime(now);
//		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//		String strNowPreviousDate = null;
//		LocalDate date = LocalDate.parse(strNowDate);
//		if (dayOfWeek == 2)
//			strNowPreviousDate = date.minusDays(3).toString();
//		else
//			strNowPreviousDate = date.minusDays(1).toString();
//
//		List<String> usernames = userRepo.findAllUsername("SALESMAN", true,getCurrentYear());
//		usernames.forEach(item -> System.out.println(item));
//		List<Kpi> kpis = new ArrayList<Kpi>();
//		for (String username : usernames) {
//			Kpi kpi = new Kpi();
//			int reportNumber = reportRepo.countReportByDateAndUsername(strNowDate, username);
//			Kpi kpiPrevious = kpiRepo.findKpiByDateAndUsername(strNowPreviousDate, username);
//			if (reportNumber < 10) {
//				if (!ObjectUtils.isEmpty(kpiPrevious)) {
//					if (kpiPrevious.getStreak() > 0)
//						kpi.setStreak(0);
//					else
//						kpi.setStreak(kpiPrevious.getStreak() - 1);
//				} else
//					kpi.setStreak(-1);
//			} else {
//				int successNumber = reportRepo.countReportByDateAndUsernameAndResult(strNowDate, username, true);
//				if (!ObjectUtils.isEmpty(kpiPrevious)) {
//					if (successNumber < 3) {
//						if (kpiPrevious.getStreak() > 0)
//							kpi.setStreak(0);
//						else
//							kpi.setStreak(kpiPrevious.getStreak() - 1);
//					} else {
//						if (kpiPrevious.getStreak() >= 0)
//							kpi.setStreak(kpiPrevious.getStreak() + 1);
//						else
//							kpi.setStreak(0);
//					}
//				} else {
//					if (successNumber < 3) {
//						kpi.setStreak(-1);
//					} else {
//						kpi.setStreak(1);
//					}
//				}
//			}
//			User user = userRepo.getOne(username);
//			kpi.setUser(user);
//			try {
//				kpi.setDate(sdff.parse(strNowDate));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			kpis.add(kpi);
//		}
//		kpiRepo.saveAll(kpis);
//	}
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
	
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Saigon")
	public void update() {
		List<Service> list = repo.findByIsExpired(false);
		List<Service> newList = new ArrayList<>();
		boolean result = false;
		if (!ObjectUtils.isEmpty(list)) {
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
			String strNowDate = sdf.format(new Date());
			for (Service item : list) {
				String endDate = sdf.format(item.getEndDate());
				try {
					Date now = sdf.parse(strNowDate);
					Date end = sdf.parse(endDate);
					if (now.compareTo(end) > 0) {
						item.setExpired(true);
						result = true;
					}
					newList.add(item);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (result)
				repo.saveAll(newList);
		}
		;
	}
	
	@Scheduled(cron = "0 2 0 * * *", zone = "Asia/Saigon")
	public void updateTaskNgoaiSalesMoi() {
		List<Task> tasks = targetRepo.findBySchoolYearAndResultNotSalesMoi(getCurrentYear(), "TBD","Sales mới");
		List<Task> taskUpdate = new ArrayList<>();
		if (!ObjectUtils.isEmpty(tasks)) {
			for (Task item : tasks) {
					Date now = new Date();
					Date end = item.getEndDate();
					if (now.compareTo(end) > 0 && item.getResult().equalsIgnoreCase("TBD")) {
						item.setResult("failed");
						taskUpdate.add(item);
					}
					
			}
			if (!ObjectUtils.isEmpty(taskUpdate))
				targetRepo.saveAll(taskUpdate);
		}
		;
	}

//	@Override
//	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//        threadPoolTaskScheduler.setPoolSize(10);
//        threadPoolTaskScheduler.setThreadNamePrefix("your-scheduler-");
//        threadPoolTaskScheduler.initialize();
//
//        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
//	}
	
	
	//nếu 1 task đi sale mới qua dealine mà chưa kí hợp đồng, thì hệ thống tự chuyển sang Tiềm năng
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Saigon")
	public void autoUpdateSchool() {
		List<Task> tasks = targetRepo.findBySchoolYearAndResult(getCurrentYear(), "TBD","Sales mới");
		List<School> schools = new ArrayList<>();
		List<Task> taskUpdate = new ArrayList<>();
		for (Task item : tasks) {
				Date now = new Date();
				Date end = item.getEndDate();
				if (now.compareTo(end) > 0 && item.getResult().equalsIgnoreCase("TBD")) {
					item.setResult("failed");
					taskUpdate.add(item);
					School school = item.getSchool();
					if(school.getSchoolStatus().getName().equalsIgnoreCase("Chưa hợp tác")){
							school.setSchoolStatus(statusRepo.findByName("Tiềm năng"));
							schools.add(school);
					}	
				}
			if(!ObjectUtils.isEmpty(taskUpdate))
				targetRepo.saveAll(taskUpdate);
			if(!ObjectUtils.isEmpty(schools))
				schoolRepo.saveAll(schools);
		}
	}
	@Scheduled(cron = "0 2 0 * * *", zone = "Asia/Saigon")
	private void autoCalculateKpi() {
		List<KpiGroup> list = groupRepo.findAll((Specification<KpiGroup>) (root, query, criteriaBuilder) -> {
			Predicate p = criteriaBuilder.conjunction();
			p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get(KpiGroup_.END_DATE),new Date()));
			p = criteriaBuilder.and(p, criteriaBuilder.isTrue(root.get(KpiGroup_.IS_ACTIVE)));
			return p;
		});
	if(!ObjectUtils.isEmpty(list)) {
		for (KpiGroup kpiGroup : list) {
			processKPIGroup(kpiGroup);
		}
	}
	}
	private void processKPIGroup(KpiGroup group) {
		for (Kpi kpi : group.getKpis()) {
			processKPI(kpi,group.getStartDate(),group.getEndDate());
		}
	}
	private void processKPI(Kpi kpi,Date start,Date end) {
		List<KpiDetails> entities = new ArrayList<>();
		for (KpiDetails detail : kpi.getKpiDetails()) {
			double actual = 0;
			String username = kpi.getUser().getUsername();
			switch (detail.getCriteria().getId()) {
			case "DS":
				actual = calculateDoanhso(username, start,end);
				detail.setActualValue(actual);
				entities.add(detail);
				break;
			case "NS":
				actual = calculateSoServiceSale(username, start, end);
				detail.setActualValue(actual);
				entities.add(detail);
			break;
			case "PCT":
				actual = calculateTaskPercent(username, start, end);
				detail.setActualValue(actual);
				entities.add(detail);
			break;
			case "RP":
				actual = calculateReport(username,start,end);
				detail.setActualValue(actual);
				entities.add(detail);
				break;
			case "PRP":
				actual = calculateReportSuccess(username,start,end);
				detail.setActualValue(actual);
				entities.add(detail);
				break;
			case "PS":
				actual = calculateSaleSucessPercent(username,start,end);
				detail.setActualValue(actual);
				entities.add(detail);
				break;
			default:
				break;
			}
		}
		kpiDetailRepo.saveAll(entities);
		
	}
	private double calculateDoanhso(String username,Date start,Date end) {
		List<Service> services = repo.findAll((Specification<Service>) (root, query, criteriaBuilder) -> {
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
			
			for (Service service : services) {
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
		List<com.app.demo.models.Service> services = repo.findAll((Specification<com.app.demo.models.Service>) (root, query, criteriaBuilder) -> {
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
	private double calculateTaskPercent(String username, Date start, Date end) {
		double value = 0;
		List<Task> tasks = targetRepo.findAll((root, query, builder) -> {
			Join<Task, User> target_user = root.join(Task_.USER);
			Predicate p = builder.conjunction();
			p= builder.and(p,builder.lessThanOrEqualTo(root.get(Task_.COMPLETED_DATE),end)); 
			p= builder.and(p,builder.greaterThanOrEqualTo(root.get(Task_.COMPLETED_DATE),start)); 
			p = builder.and(p, builder.equal(target_user.get(User_.USERNAME), username));
			p = builder.and(p, builder.equal(root.get(Task_.RESULT), "successful"));
			return p;
		});
		List<Task> taskAll = targetRepo.findAll((root, query, builder) -> {
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
		List<Task> tasks = targetRepo.findAll((Specification<Task>) (root, query, builder) -> {
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
		List<Task> taskAll = targetRepo.findAll((Specification<Task>) (root, query, builder) -> {
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
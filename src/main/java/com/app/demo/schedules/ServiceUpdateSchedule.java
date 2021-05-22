package com.app.demo.schedules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.util.ObjectUtils;

import com.app.demo.models.Kpi;
import com.app.demo.models.Service;
import com.app.demo.models.User;
import com.app.demo.repositories.KpiRepository;
import com.app.demo.repositories.ReportRepository;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.TaskRepository;
import com.app.demo.repositories.UserRepository;

@Configuration
@EnableScheduling
public class ServiceUpdateSchedule /* implements SchedulingConfigurer */ {

	@Autowired
	private ServiceRepository repo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private KpiRepository kpiRepo;

	@Autowired
	private ReportRepository reportRepo;

	@Autowired
	private TaskRepository targetRepo;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
	
	@Scheduled(cron = "0 59 23 * * 1-6", zone = "Asia/Saigon")
	public void calculateKPI()  {
		Date now = new Date();	
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		String strNowDate = sdff.format(now);
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		c.setTime(now);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String strNowPreviousDate = null;
		LocalDate date = LocalDate.parse(strNowDate);
		if (dayOfWeek == 2)
			strNowPreviousDate = date.minusDays(3).toString();
		else
			strNowPreviousDate = date.minusDays(1).toString();

		List<String> usernames = userRepo.findAllUsername("SALESMAN", true,getCurrentYear());
		usernames.forEach(item -> System.out.println(item));
		List<Kpi> kpis = new ArrayList<Kpi>();
		for (String username : usernames) {
			Kpi kpi = new Kpi();
			int reportNumber = reportRepo.countReportByDateAndUsername(strNowDate, username);
			Kpi kpiPrevious = kpiRepo.findKpiByDateAndUsername(strNowPreviousDate, username);
			if (reportNumber < 10) {
				if (!ObjectUtils.isEmpty(kpiPrevious)) {
					if (kpiPrevious.getStreak() > 0)
						kpi.setStreak(0);
					else
						kpi.setStreak(kpiPrevious.getStreak() - 1);
				} else
					kpi.setStreak(-1);
			} else {
				int successNumber = reportRepo.countReportByDateAndUsernameAndResult(strNowDate, username, true);
				if (!ObjectUtils.isEmpty(kpiPrevious)) {
					if (successNumber < 3) {
						if (kpiPrevious.getStreak() > 0)
							kpi.setStreak(0);
						else
							kpi.setStreak(kpiPrevious.getStreak() - 1);
					} else {
						if (kpiPrevious.getStreak() >= 0)
							kpi.setStreak(kpiPrevious.getStreak() + 1);
						else
							kpi.setStreak(0);
					}
				} else {
					if (successNumber < 3) {
						kpi.setStreak(-1);
					} else {
						kpi.setStreak(1);
					}
				}
			}
			User user = userRepo.getOne(username);
			kpi.setUser(user);
			try {
				kpi.setDate(sdff.parse(strNowDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			kpis.add(kpi);
		}
		kpiRepo.saveAll(kpis);
	}
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
			for (Service item : list) {
				String strNowDate = sdf.format(new Date());
				String endDate = sdf.format(item.getEndDate());
				try {
					Date now = sdf.parse(strNowDate);
					Date end = sdf.parse(endDate);
					if (now.compareTo(end) > 1) {
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

//	@Override
//	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//        threadPoolTaskScheduler.setPoolSize(10);
//        threadPoolTaskScheduler.setThreadNamePrefix("your-scheduler-");
//        threadPoolTaskScheduler.initialize();
//
//        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
//	}
//	@Scheduled(cron = "0 30 16 * * 1-6", zone = "Asia/Saigon")
//	public void autoSubmitReport() {
//		List<String> usernames = userRepo.findAllUsername("SALESMAN", true,getCurrentYear());
//		
//	}
}
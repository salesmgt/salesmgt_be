package com.app.demo.schedules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ObjectUtils;

import com.app.demo.models.Service;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.TaskRepository;

@Configuration
@EnableScheduling
public class ServiceUpdateSchedule /*implements SchedulingConfigurer*/ {

	@Autowired
	private ServiceRepository repo;
	
	@Autowired
	private TaskRepository targetRepo;
	
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Saigon")
	public void updateTask() {
	
	}
	
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Saigon")
	public void update() {
		List<Service> list = repo.findByIsExpired(false);
		List<Service> newList = new ArrayList<>();
		boolean result = false;
		if (!ObjectUtils.isEmpty(list)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		};
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
}

//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
//	System.out.println(sdf.format(item.getDate()));

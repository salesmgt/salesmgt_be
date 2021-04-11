package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.ActivityDTO;
import com.app.demo.dtos.ActivityViewRequest;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.PersonalActivity;

import com.app.demo.models.PersonalActivity_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.PersonalActivityRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.IActivityService;

@Service
public class ActivityServiceImpl implements IActivityService {

	@Autowired
	private PersonalActivityRepository repo;
	
	@Autowired
	private UserRepository userRepo;

	public List<ActivityDTO> getActivities(ActivityViewRequest request) {
		List<PersonalActivity> entities = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		LocalDate date = request.getCurrentDate();
		if (request.getName().equalsIgnoreCase("navigating")) {
			switch (request.getCurrentView()) {
			case "Week":
				LocalDate startOfWeek = date;
				while (startOfWeek.getDayOfWeek() != DayOfWeek.SUNDAY) {
					startOfWeek = startOfWeek.minusDays(1);
				}
				LocalDate endOfWeek = date;
				while (endOfWeek.getDayOfWeek() != DayOfWeek.SATURDAY) {
					endOfWeek = endOfWeek.plusDays(1);
				}
				Date end1 = null;
				Date start1 = null;
				try {
					start1 = sdf.parse(startOfWeek.toString());
					end1 = sdf.parse(endOfWeek.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entities = getListByNavigatorAndWeek(request, start1, end1);
				return convertToDTO(entities);
			case "Month":
				System.out.println("vào getlist covert month ");
				LocalDate endDate = date.withDayOfMonth(date.getMonth().length(date.isLeapYear())).plusDays(6);
				LocalDate startDate = date.withDayOfMonth(1).minusDays(6);
				Date end2 = null;
				Date start2 = null;
				try {
					start2 = sdf.parse(startDate.toString());
					end2 = sdf.parse(endDate.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entities = getListByNavigatorAndWeek(request, start2, end2);

				return convertToDTO(entities);
			case "Day":
				LocalDate next = date.plusDays(1);
				LocalDate previous = date.minusDays(1);
				Date endDay = null;
				Date startDay = null;
				try {
					startDay = sdf.parse(next.toString());
					endDay = sdf.parse(previous.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entities = getListByNavigatorAndWeek(request, startDay, endDay);
				return convertToDTO(entities);
			}
		}
		return null;
	}

	private List<PersonalActivity> getListByNavigatorAndWeek(ActivityViewRequest request, Date start, Date end) {
		return repo.findAll((Specification<PersonalActivity>) (root, query, builder) -> {
			Join<PersonalActivity, User> act_user = root.join(PersonalActivity_.USER);
			Predicate p = builder.conjunction();
			p = builder.equal(act_user.get(User_.USERNAME), request.getUsername());
			Predicate byRecurrence = builder.isNotNull(root.get(PersonalActivity_.recurrenceRule));

			// logic for date by week range{
			Predicate th1 = builder.conjunction();
			th1 = builder.between(root.get(PersonalActivity_.START_TIME), start, end);
			th1 = builder.and(p, builder.between(root.get(PersonalActivity_.END_TIME), start, end));

			Predicate th21 = builder.lessThanOrEqualTo(root.get(PersonalActivity_.START_TIME), start);
			Predicate th22 = builder.greaterThanOrEqualTo(root.get(PersonalActivity_.END_TIME), start);
			Predicate th2 = builder.and(th21, th22);

			Predicate th31 = builder.lessThanOrEqualTo(root.get(PersonalActivity_.START_TIME), end);
			Predicate th32 = builder.greaterThanOrEqualTo(root.get(PersonalActivity_.END_TIME), end);
			Predicate th3 = builder.and(th31, th32);
			Predicate th41 = builder.lessThanOrEqualTo(root.get(PersonalActivity_.START_TIME), start);
			Predicate th42 = builder.greaterThanOrEqualTo(root.get(PersonalActivity_.END_TIME), end);
			Predicate th4 = builder.and(th41, th42);
			Predicate th = builder.or(th1, th2, th3, th4, byRecurrence);
			// }
			p = builder.and(p, th);
			return p;
		});
	}
	private List<ActivityDTO> convertToDTO(List<PersonalActivity> entities) {
		final List<ActivityDTO> dtos = new ArrayList<ActivityDTO>();
		if (!ObjectUtils.isEmpty(entities)) {
			entities.forEach(item -> {
				ActivityDTO dto = Mapper.getMapper().map(item, ActivityDTO.class);
				dto.setUsername(item.getUser().getUsername());
				SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d y H:m:s ZZZ");
				String startTime = formatter.format(item.getStartTime());
				String endTime = formatter.format(item.getEndTime());
				dto.setStartTime(startTime);
				dto.setEndTime(endTime);
				dtos.add(dto);
			});
		};
		
		return dtos;
	}
	@Override
	public void insert(ActivityDTO dto) {
		try {
		User user = userRepo.findByUsername(dto.getUsername());
		PersonalActivity entity = Mapper.getMapper().map(dto,PersonalActivity.class );
		entity.setUser(user);
		entity.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        .parse(dto.getEndTime()));	
		entity.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		        .parse(dto.getStartTime()));	
		repo.save(entity);
	}catch (Exception e) {
		e.printStackTrace();
	}
	}
	@Override
	public void delete(List<Integer> ids) {
		List<PersonalActivity> entities1 = null;
		if(ids.size()==1) {
			entities1 = repo.findByRecurrenceID(ids.get(0));
		}
		List<PersonalActivity> entities = repo.findAllById(ids);
		if(!ObjectUtils.isEmpty(entities)) {
			if(entities1 != null) {
				entities =  Stream.concat(entities.stream(), entities1.stream())
                        .collect(Collectors.toList());
			}
		repo.deleteAll(entities);
		}
		
	}
	@Override
	public void update(ActivityDTO dto, int id) {
		try {
		boolean result = repo.existsById(id);
		if(result) {
		PersonalActivity entity = Mapper.getMapper().map(dto,PersonalActivity.class );
		User user = userRepo.findByUsername(dto.getUsername());
		entity.setUser(user);
			entity.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.parse(dto.getEndTime()));
		entity.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		        .parse(dto.getStartTime()));	
		repo.save(entity);
		}else throw new SQLException("Could not get it by this Id "+id);
	} catch (ParseException | SQLException e) {
		e.printStackTrace();
	}	
	}
	public void updatePatch(int id, String ex) {
		PersonalActivity entity = repo.getOne(id);
		entity.setRecurrenceException(ex);
		repo.save(entity);
	}
}

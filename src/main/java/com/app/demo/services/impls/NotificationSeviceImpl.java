package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.app.demo.dtos.NotificationDTO;
import com.app.demo.dtos.NotifyRequest;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Notification;
import com.app.demo.models.Notification_;
import com.app.demo.models.TargetSchool;
import com.app.demo.pagination.Paging;
import com.app.demo.repositories.NotificationRepository;
import com.app.demo.repositories.TargetRepository;

@Service
public class NotificationSeviceImpl {
	@Autowired
	private NotificationRepository repo;
	@Autowired
	private TargetRepository targetRepo;
	
	
	public Paging<NotificationDTO> getNotifications(String username, int limit) {

		Pageable paging = PageRequest.of(0, limit, Sort.by("createDate").descending());
		Page<Notification> entities = repo.findAll((Specification<Notification>) (root, query, builder) -> {
			Predicate p = builder.conjunction();
			p = builder.equal(root.get(Notification_.USER), username);
			return p;
		}, paging);
		List<NotificationDTO> results = new ArrayList<NotificationDTO>();
		Paging<NotificationDTO> page = new Paging<NotificationDTO>();
		if (entities.hasContent()) {
			entities.getContent().forEach(item -> {
				NotificationDTO dto = Mapper.getMapper().map(item, NotificationDTO.class);
				dto.setUsername(item.getUser().getUsername());
				results.add(dto);
			});
			page.setList(results);
			page.setTotalElements(entities.getTotalElements());
			page.setTotalPage(entities.getTotalPages());
		}
		return page;
	}

	public void createCompletedContractNotification(int targetId) {
		TargetSchool target = targetRepo.getOne(targetId);
		Notification notification = new Notification();
		notification.setTitle("New contract has been signed in " + target.getSchool().getName());
		notification.setContent(target.getUser().getUsername() + "has just signed a contract with "
				+ target.getSchool().getName() + " school.");
		notification.setType("contract");
		notification.setUser(target.getUser());
		notification.setRead(false);
		repo.save(notification);
		
		NotifyRequest notiRequest = new NotifyRequest();
		notiRequest.setTitle("New contract has been signed in " + target.getSchool().getName());
		notiRequest.setBody(target.getUser().getUsername() + "has just signed a contract with "
				+ target.getSchool().getName() + " school.");
		notiRequest.setToken(null);
	}

}

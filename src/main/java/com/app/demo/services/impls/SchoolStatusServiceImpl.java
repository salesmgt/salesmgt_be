package com.app.demo.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.SchoolStatus;
import com.app.demo.repositories.SchoolStatusRepository;
import com.app.demo.services.ISchoolStatusService;

@Service
public class SchoolStatusServiceImpl implements ISchoolStatusService {

	@Autowired
	private SchoolStatusRepository repo;
	@Override
	public List<String> getSchoolStatus() {
		List<SchoolStatus> entities = repo.findAll();
		List<String> names = entities.stream().map(SchoolStatus::getName).collect(Collectors.toList());
		return names;
	}

}

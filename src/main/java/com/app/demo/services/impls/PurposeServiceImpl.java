package com.app.demo.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.Purpose;
import com.app.demo.repositories.PurposeRepository;
import com.app.demo.services.IPurposeService;
@Service
public class PurposeServiceImpl implements IPurposeService {
	@Autowired
	private PurposeRepository repo;
	@Override
	public List<String> getPurposes(){
		List<Purpose> entities = repo.findAll();
		List<String> names = entities.stream().map(Purpose::getName).collect(Collectors.toList());
		return names;
	}
}

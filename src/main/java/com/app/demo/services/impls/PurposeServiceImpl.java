package com.app.demo.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.TargetPurpose;
import com.app.demo.repositories.TargetPurposeRepository;
import com.app.demo.services.IPurposeService;
@Service
public class PurposeServiceImpl implements IPurposeService {
	@Autowired
	private TargetPurposeRepository repo;
	@Override
	public List<String> getPurposes(){
		List<TargetPurpose> entities = repo.findAll();
		List<String> names = entities.stream().map(TargetPurpose::getName).collect(Collectors.toList());
		return names;
	}
}

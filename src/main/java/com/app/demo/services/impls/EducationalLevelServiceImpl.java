package com.app.demo.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.models.EducationalLevel;
import com.app.demo.repositories.EducationalLevelRepository;
import com.app.demo.services.IEducationalLevelService;

@Service
public class EducationalLevelServiceImpl implements IEducationalLevelService {
 @Autowired
 private EducationalLevelRepository repo;
 
 @Override
 public List<String> getAll(){
 List<EducationalLevel> entities = repo.findAll();
	List<String> names = entities.stream().map(EducationalLevel::getName).collect(Collectors.toList());
	return names;
}
}

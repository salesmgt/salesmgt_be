package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.dtos.CriteriaDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Criteria;
import com.app.demo.repositories.CriteriaRepository;
import com.app.demo.services.ICriteriaService;

@Service
public class CriteriaService implements ICriteriaService {

	@Autowired
	private CriteriaRepository repo;
	@Override
	public List<?> getAll() {
		 List<Criteria> entities = repo.findAll();
		 List<CriteriaDTO> dtos = new ArrayList<CriteriaDTO>();
			entities.stream().forEach(item ->dtos.add(Mapper.getMapper().map(item, CriteriaDTO.class)));
			return dtos;
		}
	

}

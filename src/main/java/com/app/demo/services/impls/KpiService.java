package com.app.demo.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.demo.repositories.KpiRepository;
import com.app.demo.services.IKpiService;

@Service
public class KpiService implements IKpiService {
	@Autowired
	private KpiRepository repo;
	
	

}

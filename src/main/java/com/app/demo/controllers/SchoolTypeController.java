package com.app.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.models.SchoolType;

@CrossOrigin
@RestController
@RequestMapping(path = "/types")
public class SchoolTypeController {

	@GetMapping
	public List<String> getType(){
		return  Stream.of(SchoolType.values())
                .map(SchoolType::getValues) // map using 'getValue'
                .collect(Collectors.toList());
	}
}

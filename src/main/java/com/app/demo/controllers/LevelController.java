package com.app.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.models.Level;

@CrossOrigin
@RestController
@RequestMapping(path = "/levels")
public class LevelController {

	@GetMapping
	public List<String> getLevels(){
	return Stream.of(Level.values())
    .map(Level::name)
    .collect(Collectors.toList());
}
}

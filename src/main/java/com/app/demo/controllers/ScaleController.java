package com.app.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.models.Scale;

@CrossOrigin
@RestController
@RequestMapping(path = "/scales")
public class ScaleController {
	@GetMapping
	public List<String> getScales(){
	return Stream.of(Scale.values())
    .map(Scale::name)
    .collect(Collectors.toList());
	}
}

package com.app.demo.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityViewRequest {
	private String action;
	private String currentView;
	private String name;
	private String username;
	
	private LocalDate currentDate;
	
	private String previousDate;
}

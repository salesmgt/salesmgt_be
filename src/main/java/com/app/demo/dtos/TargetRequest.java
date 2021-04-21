package com.app.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TargetRequest {
	private int id;
	private String schoolYear;
	private String note;
	private String username;
	private List<Integer> schoolId;
}

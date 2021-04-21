package com.app.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddTargetRequest {
	private String schoolYear;
	private String note;
	private String purpose;
	private List<Integer> schoolIds;
	private TargetFilterRequest filter;
}

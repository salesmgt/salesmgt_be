package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TargetDTO {
	private int id;
	private String purpose;
	private String schoolName;
	private String fullName;
	private String avatar;
	private String reprName;
	private boolean reprGender;
	private String username;
	private String district;
	private String schoolYear;
}

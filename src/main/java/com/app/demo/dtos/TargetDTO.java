package com.app.demo.dtos;

import com.app.demo.models.Scale;

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
	private boolean reprIsMale;
	private String username;
	private String district;
	private String schoolYear;
	private String reprEmail;
	private String reprPhone;
	private String userPhone;
	private String userEmail;
	private String level;
	private String schoolStatus;
	private String schoolType;
	private Scale schoolScale;
	private String schoolAddress;
	private String schoolDescription;
	private String note;
	private int schoolId;
}

package com.app.demo.dtos;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskDetails {
	private int id;
	private String purpose;
	private String schoolName;
	private String fullName;
	private String avatar;
	private String reprName;
	private Date endDate;
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
	private String schoolAddress;
	private String note;
	private Date assignDate;
	private String noteBy;
	private String result;
	private String schoolId;
	private List<ServiceDTO> services;

}

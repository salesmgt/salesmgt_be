package com.app.demo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServiceDTO {
	private int id;
	private String serviceType;
	private Date startDate;
	private Date approveDate;
	private String status; // approved-pending-rejected
	private String rejectedReason;
	private Date endDate; //deadline
	private boolean isExpired;
	private int classNumber;
	private String note;
	private int targetSchoolId =0;

}

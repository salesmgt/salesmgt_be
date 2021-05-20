package com.app.demo.dtos;

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
	private String avatar;
	private String fullName;
	private String username;
	private String schoolName;
	private String serviceType;
	private String startDate;
	private int studentNumber;
	private String approveDate;
	private String status; // approved-pending-rejected
	private String rejectedReason;
	private String endDate; //deadline
	private boolean isExpired;
	private int classNumber;
	private double pricePerSlot;
	private String note;
	private int slotNumber;
	private String educationLevel;
	private String submitDate;
	private String address;
	private int taskId =0;
}

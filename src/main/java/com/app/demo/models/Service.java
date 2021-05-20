package com.app.demo.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Service extends Auditable<String> {
	@Id
	@Column(name = "service_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Date startDate;
	private Date approveDate;
	private Date submitDate;
	private String status; // approved-pending-rejected
	private String rejectedReason;
	private Date endDate; //deadline
	private boolean isExpired;
	private int classNumber;
	private int studentNumber;
	private int slotNumber;
	private String note;
	private double pricePerSlot;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="task_id")
	private Task task;	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service_type_id")
	private ServiceType serviceType;	
	
}

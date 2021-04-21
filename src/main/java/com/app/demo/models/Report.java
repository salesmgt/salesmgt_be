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

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Report extends Auditable<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id") 
	private int id;
	
	private Date date;
	
	@Column(name = "report_description")
	private String description;
	@Column(length = 50)
	private String result;
	
	private boolean isActive;
	
	private String positivity;

	private String difficulty;
	
	private String futurePlan;
	
	private String supervisorComment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="target_school_id")
	private TargetSchool targetSchool;	
}

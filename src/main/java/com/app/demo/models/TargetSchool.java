package com.app.demo.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "target_school")
@EntityListeners(AuditingEntityListener.class)
public class TargetSchool extends Auditable<String> {
	@Id
	@Column(name="target_school_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 10)
	private String schoolYear;
	
	private String status;
	
	private boolean isActive;
	private Date assignDate;
	private Date endDate;
	private String note;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="target_school_purpose_id")
	private TargetPurpose targetPurpose;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="school_id")
	private School school;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy="targetSchool", fetch = FetchType.LAZY)
	private List<Report> reports;
	@OneToMany(mappedBy="targetSchool", fetch = FetchType.LAZY)
	private List<com.app.demo.models.Service> services;
}

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
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
public class Task extends Auditable<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 10)
	private String schoolYear;
	private Date completedDate;
	private boolean isActive;
	private Date assignDate;
	private Date endDate;
	private String note;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="purpose_id")
	private Purpose purpose;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="school_id")
	private School school;
	private String result;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy="task", fetch = FetchType.LAZY)
	private List<Report> reports;
	@OneToMany(mappedBy="task", fetch = FetchType.LAZY)
	private List<com.app.demo.models.Service> services;
}

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class PersonalActivity extends Auditable<String> {
	@Id
	@Column(name = "personal_activity_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 50)
	private String title;
	@Column(length = 50)
	private String location;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;	
	@Column(name ="recurrence_id")
	private int recurrenceID;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@Column
	private String remark;
	@Column(name = "activity_description")
	private String description;
	private String recurrenceRule;
	@Column(name = "recurrence_exception")
	private String recurrenceException;
	private boolean isAllDay;
	@Column
	private boolean isCompleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
		
}
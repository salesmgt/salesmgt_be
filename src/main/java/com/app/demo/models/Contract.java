package com.app.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Contract extends Auditable<String> {
	@Id
	@Column(name = "contract_id",length = 50)
	private String id;
	@Column(length =50)
	private String duration;
	private String service;
	private String revenueCriteria;
	private String note;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="target_school_id")
	private TargetSchool targetSchool;	
	
}

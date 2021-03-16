package com.app.demo.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TargetPurpose {
	@Id
	@Column(name = "target_purpose_id")
	private String id;
	
	@Column(name = "target_purpose_name")
	private String name;
	
	@OneToMany(mappedBy="targetPurpose", fetch = FetchType.LAZY)
	private List<TargetSchool> targetSchools;
}

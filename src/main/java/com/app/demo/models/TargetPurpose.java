package com.app.demo.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="target_school_purpose")
@EntityListeners(AuditingEntityListener.class)
public class TargetPurpose extends Auditable<String> {
	@Id
	@Column(name = "target_school_purpose_id",length = 10)
	private String id;
	
	@Column(name = "target_school_purpose_name",length = 50)
	private String name;
	
	@OneToMany(mappedBy="targetPurpose", fetch = FetchType.LAZY)
	private List<TargetSchool> targetSchools;
}

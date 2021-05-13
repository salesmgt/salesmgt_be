package com.app.demo.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school")
@EntityListeners(AuditingEntityListener.class)
public class School extends Auditable<String> {
	@Id
	private String schoolId;
	@Column(name = "school_name",length = 50)
	private String name;

	@Column(name = "school_address")
	private String address;
	
	@Column(name = "school_phone",length = 15)
	private String phone;
	
	@Column(name = "school_type")
	@Enumerated(EnumType.ORDINAL)
	private SchoolType type;

	private boolean isActive;

	@Column(name = "representative_name")
	private String reprName;

	@Column(name = "representative_phone")
	private String reprPhone;

	@Column(name = "representative_email")
	private String reprEmail;

	@Column(name = "representative_is_male")
	private boolean reprIsMale;
	
	private double longitude;
	private double latitude;

	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
	private List<TargetSchool> targetSchools;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_status_id")
	private SchoolStatus schoolStatus;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "educational_level_id")
	private EducationalLevel educationalLevel;

}

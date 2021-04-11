package com.app.demo.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school")
@EntityListeners(AuditingEntityListener.class)
public class School extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "school_id")
	private int id;

	@Column(name = "school_name")
	private String name;

	@Column(name = "school_address")
	private String address;

	@Column(name = "school_phone")
	private String phone;

	@Enumerated(EnumType.ORDINAL)
	private Level educationalLevel;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "school_scale")
	private Scale scale;

	@Column(name = "school_type")
	@Enumerated(EnumType.ORDINAL)
	private SchoolType type;

	@Column(name = "school_description")
	private String description;

	private boolean isActive;

	@Column(name = "representative_name")
	private String reprName;

	@Column(name = "representative_phone")
	private String reprPhone;

	@Column(name = "representative_email")
	private String reprEmail;

	@Column(name = "representative_gender")
	private boolean reprGender;

	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
	private List<TargetSchool> targetSchools;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_status_id")
	private SchoolStatus schoolStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id")
	private District district;

}

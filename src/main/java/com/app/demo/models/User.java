package com.app.demo.models;

import java.util.Date;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable<String> {

	@Id
	private String username;

	@Column
	private String passwordHash;

	@Column
	private String fullName;

	@Column(name = "user_email")
	private String email;

	@Column(name = "user_phone")
	private String phone;

	@Column(name = "is_Active")
	private boolean active;

	@Column(name = "user_avatar")
	private String avatar;

	@Column(name = "user_gender")
	private boolean gender;

	@Column
	private String address;

	@Column
	private Date birthDate;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<TargetSchool> targetSchools;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="role_id")
	private Role role;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<PersonalActivity> personalActivities;
}

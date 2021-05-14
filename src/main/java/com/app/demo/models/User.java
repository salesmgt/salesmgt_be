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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
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
	@Column(length = 30)
	private String username;

	private String passwordHash;
	@Column(length = 50)
	private String fullName;

	@Column(name = "user_email",length =50)
	private String email;
	@Column(name = "user_phone",length = 15)
	private String phone;

	@Column(name = "is_Active")
	private boolean active;

	@Column(name = "user_avatar")
	private String avatar;
	
	private String privateToken;
	
	@Column(name = "user_isMale")
	private boolean isMale;
	
	private double longitude;
	private double latitude;

	private String address;

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Task> tasks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="role_id")
	private Role role;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<PersonalActivity> personalActivities;
	
}

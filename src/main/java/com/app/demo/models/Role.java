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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
@EntityListeners(AuditingEntityListener.class)
public class Role extends Auditable<String> {

	@Id
	@Column(name = "role_id")
	private int id;

	private String name;
	@OneToMany(mappedBy  ="role", fetch = FetchType.LAZY)
	private List<User> users;

}

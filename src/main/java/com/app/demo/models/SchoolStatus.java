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

@Entity
@Table(name="school_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SchoolStatus extends Auditable<String> {

	@Id
	@Column(name = "school_status_id",length = 10)
	private String id;
	
	@Column(name = "school_status_name",length = 50)
	private String name;
	
	@OneToMany(mappedBy  ="schoolStatus", fetch = FetchType.LAZY)
	private List<School> schools;
}

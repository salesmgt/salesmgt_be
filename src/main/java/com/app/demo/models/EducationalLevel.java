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

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EducationalLevel extends Auditable<String> {
	@Id
	@Column(name = "educational_level_id")
	private int id;
	@Column(name = "educational_level_name")
	private String name;
	@OneToMany(mappedBy  ="educationalLevel", fetch = FetchType.LAZY)
	private List<School> schools;
	
}

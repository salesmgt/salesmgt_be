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
public class District {
	@Id
	@Column(name = "district_id")
	private int id;
	@Column(name = "district_name",length = 50)
	private String name;
	@OneToMany(mappedBy  ="district", fetch = FetchType.LAZY)
	private List<School> schools;
	
}

package com.app.demo.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Criteria")
public class Criteria {
	@Id
	private String id;
	private String name;
	private String description;
	private String type;
	@OneToMany(mappedBy="criteria", fetch = FetchType.LAZY)
	private List<KpiDetails> kpiDetails;
}

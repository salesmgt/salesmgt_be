package com.app.demo.models;

import java.util.List;

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
public class ServiceType {
	@Id
	private int id;
	private String name;
	@OneToMany(mappedBy  ="serviceType", fetch = FetchType.LAZY)
	private List<com.app.demo.models.Service> services;
}

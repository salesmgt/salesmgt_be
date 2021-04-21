package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ContractDTO {
	private String id;
	private String duration;
	private String service;
	private String revenueCriteria;
	private String note;
	private int targetId;
	private String username;
	private String fullname;
	private String avatar;
}

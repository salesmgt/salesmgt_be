package com.app.demo.dtos;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KpiGroupDTO {
	private int id;
	private Date startDate;
	private Date endDate;
	private boolean isActive;
	private String groupName;
	private List<String> size;
}

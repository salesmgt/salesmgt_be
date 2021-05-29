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
public class KpiUserDetails {
	private String username;
	private String fullName;
	private String avatar;
	private Date startDate;
	private Date endDate;
	private double total;
	private List<KpiDetailDTO> kpis;
}

package com.app.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KpiGroupDetails {
	private int id;
	private String groupName;
	private List<KpiDetailsDTO> kpiDetails;
	private List<KpiDTO> kpis;
}

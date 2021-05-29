package com.app.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KpiUser {
	private String username;
	private List<KpiDetailDTO> kpiDetails;
}

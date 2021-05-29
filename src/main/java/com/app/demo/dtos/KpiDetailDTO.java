package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KpiDetailDTO {
	private double actualValue;
	private double targetValue;
	private double weight;
	private int criteriaId;
	private int kpiDetailId;
	private String cirteriaContent;
	private String type;
}

package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KpiDetailsDTO implements Comparable<KpiDetailsDTO> {
	private String criteriaId;
	private String criteria;
	private double value;
	private String type;
	private double weight = 0;
	private String descrption;
	
	@Override
	public int compareTo(KpiDetailsDTO o) {
		// TODO Auto-generated method stub
		return Double.compare(o.getValue(),this.value);
	}
}

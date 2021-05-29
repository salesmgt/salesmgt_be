package com.app.demo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KpiDTO implements Comparable<KpiDTO> {
	private int kpiId;
	private String username;
	private String fullName;
	private String avatar;
	private double value;
	private Date startDate;
	private Date endDate;
	@Override
	public int compareTo(KpiDTO o) {
		// TODO Auto-generated method stub
		return Double.compare(o.getValue(),this.value );
	}
}

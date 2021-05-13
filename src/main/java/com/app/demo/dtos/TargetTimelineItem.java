package com.app.demo.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TargetTimelineItem implements Comparable<TargetTimelineItem> {
	private int id;
	private String type;
	private Date date;
	private String reportDescription;
	private String status; // approved-pending-rejected
	private String service;
	@JsonProperty("isSuccess")
	private boolean isSuccess;
	private Date startDate;
	private Date endDate;
	private int classNumber;
	@Override
	public int compareTo(TargetTimelineItem o) {
		// TODO Auto-generated method stub
		return this.getDate().compareTo(o.getDate());
	}
	
	
}

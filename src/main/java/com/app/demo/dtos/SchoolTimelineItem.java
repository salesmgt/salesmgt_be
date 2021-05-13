package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class SchoolTimelineItem implements Comparable<SchoolTimelineItem>{
	private int targetId;
	private String fullName;
	private String avatar;
	private String schoolYear;
	private String purpose;
	private String status;
	private String services;
	@Override
	public int compareTo(SchoolTimelineItem o) {
		return this.getSchoolYear().compareTo(o.getSchoolYear());
	}
}
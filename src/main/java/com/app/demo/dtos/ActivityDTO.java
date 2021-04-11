package com.app.demo.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String title;
	private String location;
	private String startTime;
	private String endTime;
	private String remark;
	private String description;
	private String recurrenceRule;
	private String recurrenceException;
	@JsonProperty("isAllDay")
	private boolean isAllDay;
	@JsonProperty("isCompleted")
	private boolean isCompleted;
	private int recurrenceID;
}	

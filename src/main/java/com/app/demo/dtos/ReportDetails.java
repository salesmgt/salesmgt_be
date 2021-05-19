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
public class ReportDetails {
	@JsonProperty("isSuccess")
	private boolean isSuccess;
    private String description;
    private String positivity;
    private String difficulty;
    private String futurePlan;
    private String date;
}

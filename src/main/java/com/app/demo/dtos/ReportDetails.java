package com.app.demo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetails {
	private String result;
    private String description;
    private String positivity;
    private String difficulty;
    private String futurePlan;
    private Date date;
}

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
public class ReportDTO {
	private int id;
    private Date date;
    private String fullName;
    private String username;
    private String avatar;
    private String schoolName;
    private String level;
    private String district;
    private String address;
    private String reprName;
    private boolean reprIsMale;
    private String purpose;
    private String result;
    private String description;
    private String positivity;
    private String difficulty;
    private String futurePlan;
    private String commentedPerson;
    private String contextComments;
    private String schoolYear;
    private int targetId;
	public ReportDTO(int id, Date date, String result, String description, String positivity, String difficulty,
			String futurePlan) {
		super();
		this.id = id;
		this.date = date;
		this.result = result;
		this.description = description;
		this.positivity = positivity;
		this.difficulty = difficulty;
		this.futurePlan = futurePlan;
	}
	@Override
	public String toString() {
		return "[" + commentedPerson + "] " + contextComments;
	}
    
    
}

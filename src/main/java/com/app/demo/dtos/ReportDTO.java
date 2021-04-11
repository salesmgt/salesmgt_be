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
    private boolean reprGender;
    private String purpose;
    private String result;
    private String description;
    private String positivity;
    private String difficulty;
    private String futurePlan;
    private String commentedPerson;
    private String contextComments;
    private String schoolYear;
}

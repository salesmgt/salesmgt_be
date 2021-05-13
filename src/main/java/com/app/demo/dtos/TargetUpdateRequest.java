package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TargetUpdateRequest {
	private int id;
	private String schoolYear;
	private String note;
	private String purpose;
	private String reprEmail;
	private String reprPhone;
	private Boolean reprIsMale;
	private String reprName;
}

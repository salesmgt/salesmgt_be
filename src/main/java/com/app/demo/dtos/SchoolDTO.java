package com.app.demo.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolDTO {
	private String schoolId;
	@NotBlank(message = "Name is mandatory")
	private String name;

	private String address;

	private String phone;
	private String educationalLevel;
	private String type;
	private boolean isActive;
	private String status;
	private String reprName;
	private String reprPhone;
	private String reprEmail;
	private boolean reprIsMale;
	private String district;
	private double latitude;
	private double longitude;
	
}

package com.app.demo.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolDTO {
	private int id;
	@NotBlank(message = "Name is mandatory")
	private String name;

	private String address;

	@Size(min=10, max=11)
	private String phone;
	@NotBlank(message = "Level is mandatory")
	private String educationalLevel;
	@NotBlank(message = "Scale is mandatory")
	private String scale;
	@NotBlank(message = "SchoolType is mandatory")
	private String type;
	private String description;
	private boolean isActive;
	@NotNull
	private String status;
	private String reprName;
	@Size(min=10, max=11)
	private String reprPhone;
	@Email(message = "Email should be valid")
	private String reprEmail;
	private boolean reprIsMale;
	@NotBlank(message = "District is mandatory")
	private String district;
	
}

package com.app.demo.dtos;

import javax.validation.constraints.Email;
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
	@NotNull
	private String name;
	@NotNull
	private String address;
	@NotNull
	@Size(min=10, max=11)
	private String phone;
	@Email(message = "Email should be valid")
	private String email;
	@NotNull
	private String educationalLevel;
	@NotNull
	private String scale;
	@NotNull
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
	private boolean reprGender;
	@NotNull
	private String district;
	
}

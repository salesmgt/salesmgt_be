package com.app.demo.dtos;


import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private String username;
	
	private String passwordHash;
	@NotBlank(message = "Full name is mandatory")
	private String fullName;
	@Email(message = "Email should be valid")
	private String email;
	@Size(min=10, max=11)
	private String phone;
	
	private boolean isActive;
	
	private String avatar;
	@NotNull
	@JsonProperty("isMale")
	private boolean isMale;
	
	private String address;
	private Date birthDate;
	@NotBlank
	private String roleName;
}

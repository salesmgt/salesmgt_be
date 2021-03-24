package com.app.demo.dtos;

import java.util.Date;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	@NotNull
	private String fullName;
	@Email(message = "Email should be valid")
	private String email;
	@Size(min=10, max=11)
	private String phone;
	
	private boolean isActive;
	
	private String avatar;
	@NotNull
	private boolean gender;
	
	private String address;
	
	private Date birthDate;
	@NotNull
	private String roleName;
}

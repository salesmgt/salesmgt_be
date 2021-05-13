package com.app.demo.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Principle {
	private String reprName;
	@Size(min=10, max=11)
	private String reprPhone;
	@Email(message = "Email should be valid")
	private String reprEmail;
	private Boolean reprIsMale;
}

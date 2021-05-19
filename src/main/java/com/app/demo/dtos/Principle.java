package com.app.demo.dtos;

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
	private String reprPhone;
	private String reprEmail;
	private Boolean reprIsMale;
}

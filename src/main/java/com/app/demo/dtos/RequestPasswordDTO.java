package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestPasswordDTO {
	private String oldPassword;
	private String newPassword;
}

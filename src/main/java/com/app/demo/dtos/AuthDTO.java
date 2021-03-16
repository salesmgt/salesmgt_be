package com.app.demo.dtos;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
	@NotEmpty(message = "Vui lòng nhập username!")
	private String username;
	
	@NotEmpty(message = "Vui lòng nhập mật khẩu!")
	@Length(min = 6, message = "Mật khẩu ít nhất 6 ký tự!")
	private String password;
	

}

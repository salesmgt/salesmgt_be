package com.app.demo.dtos;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class MutiAssignRequest {
	@NotEmpty(message = "List of targets must be not null")
	private List<Integer> targetId;
	@NotBlank(message = "PIC is mandatory")
	private String username;
}

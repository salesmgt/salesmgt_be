package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RequestProfileDTO {
	private String attribute;
	private String value;
	private double longitude;
	private double latitude;
}

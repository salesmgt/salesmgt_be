package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestSalesman {
	private String username;
	private String avatar;
	private String fullName;
	private int assignNumber;
	private double distance;
}

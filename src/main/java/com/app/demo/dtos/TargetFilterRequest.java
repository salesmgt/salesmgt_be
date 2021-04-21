package com.app.demo.dtos;

import com.app.demo.models.Scale;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TargetFilterRequest {
	private String district;
	private String levelEducational;
	private String status;
	private Scale scale;
	private String type;
	@JsonProperty("isAll")
	private boolean isAll;
}

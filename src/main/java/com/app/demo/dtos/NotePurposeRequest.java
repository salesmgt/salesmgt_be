package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotePurposeRequest {
	private String note;
	private String purpose;
	private String noteBy;
	@Override
	public String toString() {
		return "[" + noteBy + "] " +note;
	}
	
	
}

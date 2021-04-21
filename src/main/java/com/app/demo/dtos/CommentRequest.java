package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
	private String commentedPerson;
	private String contextComments; 
	@Override
	public String toString() {
		return "[" + commentedPerson + "] " + contextComments;
	}
}

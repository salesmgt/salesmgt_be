package com.app.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SuggestionSalesman implements Comparable<SuggestionSalesman>{
	private String username;
	private String avatar;
	private String address;
	private String fullName;
	private int numberOfTask;
	private String content;
	private double distance;
	private double point;
	@Override
	public int compareTo(SuggestionSalesman o) {
		return  this.getPoint() > o.getPoint() ? -1 :1;
	}
}

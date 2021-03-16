package com.app.demo.pagination;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Paging<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<T> list;
	private int totalPage;
	private long totalElements;
}

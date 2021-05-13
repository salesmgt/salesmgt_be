package com.app.demo.dtos;

import java.util.List;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
	private TreeMap<String,List<TotalSchoolDTO>> salesMoi;
	private TreeMap<String,List<TotalSchoolDTO>> theoDoi;
	private TreeMap<String,List<TotalSchoolDTO>> taiKy;
	private TreeMap<String,List<TotalSchoolDTO>> kyMoi;
	private TreeMap<String,List<TotalSchoolDTO>> chamSoc;
}

package com.app.demo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableDTO {
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;	
	private Date modifiedDate;
	
}

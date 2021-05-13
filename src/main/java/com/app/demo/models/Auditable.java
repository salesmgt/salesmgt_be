package com.app.demo.models;

import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {
	@Column(length = 50,nullable = true)
	@CreatedBy
	private String createdBy;
	
	@Column
	@CreatedDate
    @Temporal(TIMESTAMP)
	private Date createdDate;
	
	@LastModifiedBy
	@Column(length = 50,nullable = true)
	private String modifiedBy;
	
	@LastModifiedDate
	@Temporal(TIMESTAMP)
	@Column
	private Date modifiedDate;
}

package com.app.demo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private int id;
	private String title;
	private boolean isRead;
	private String type;
	private String content;
	private Date createdDate;
	private String username;
}

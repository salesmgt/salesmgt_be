package com.app.demo.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyRequest {
	private String token;
    private String title;
    private String message;
    private Map<String, String> data;
}

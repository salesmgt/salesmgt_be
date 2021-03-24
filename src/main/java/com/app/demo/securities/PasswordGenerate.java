package com.app.demo.securities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;

public class PasswordGenerate {
	public static void main(String[] args) {
		List<String> enumNames = Stream.of(SchoolType.values())
                .map(SchoolType::name)
                .collect(Collectors.toList());
		enumNames.forEach(item -> System.out.println(item.));
		   
		}
		         
	
}

package com.app.demo.securities;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;

public class PasswordGenerate {
	public static void main(String[] args) {
		String cmt = "Duplicate entry '3 Mcguire Terrace' for key 'school.school_address_UNIQUE'";
		
		
		System.out.println(cmt.split("'")[1]);
	}
		         
	
}

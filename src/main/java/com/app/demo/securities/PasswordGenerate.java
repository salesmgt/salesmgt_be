package com.app.demo.securities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.app.demo.SalesmgtApplicationTest;
import com.app.demo.utils.VNCharacterUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class PasswordGenerate {
	
	
	public static void main(String[] args) throws ParseException {
//		//Spherical Law of Cosines
//		Toado diem1 = new Toado(10.807950,106.664690);
//		Toado diem2 = new Toado(21.207480,105.779170);
//		double theta = diem1.getLongitude() - diem2.getLongitude();
//		double dist = Math.sin(Math.toRadians(diem1.getLatitude())) * Math.sin(Math.toRadians(diem2.getLatitude())) + Math.cos(Math.toRadians(diem1.getLatitude())) * Math.cos(Math.toRadians(diem2.getLatitude())) * Math.cos(Math.toRadians(theta));
//		dist = Math.acos(dist);
//		System.out.println(dist*6371+" km rad");
//		dist = Math.toDegrees(dist);
//		dist = dist * 60 * 1.1515;
//		dist = dist * 1.609344;
//		System.out.println(dist+" km");
////		System.out.println(Math.sin(Math.toRadians(diem1.getLatitude())));
////		System.out.println(Math.sin(Math.toRadians(diem1.getLatitude())) * Math.sin(Math.toRadians(diem2.getLatitude())) + Math.cos(Math.toRadians(diem1.getLatitude())) * Math.cos(Math.toRadians(diem2.getLatitude())) * Math.cos(Math.toRadians(theta)));
//		
//		
//		//Haversine nè
//		var lat1 = diem1.getLatitude();
//		var lat2 = diem2.getLatitude();
//		var lon1 = diem1.getLongitude();
//		var lon2 = diem2.getLongitude();
//		var R = 6371; // km
//		var dLat = Math.toRadians(lat2-lat1);
//		var dLon = Math.toRadians(lon2-lon1); 
//		var a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
//		        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//		        Math.sin(dLon/2) * Math.sin(dLon/2); 
//		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
//		var d = R * c;
//	System.out.println(d);
//	System.out.println(Math.sqrt(Math.pow(lat1-lat2, 2)+Math.pow(lon1-lon2, 2)));
//	double khoangcach1 = Math.sqrt(Math.pow(lat1-lat2, 2)+Math.pow(lon1-lon2, 2));
//	khoangcach1 = khoangcach1/2;
//	double goc = Math.sin(khoangcach1/6371)*2;
//	System.out.println(goc);
//	System.out.println(2*Math.PI*6371*goc/360);
		String strNowDate = "2021-05-18";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		c.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));
		c.setTime(sdf.parse(strNowDate));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		System.out.println(dayOfWeek);
		String strNowPreviousDate = null;
		LocalDate date = LocalDate.parse(strNowDate);
		if(dayOfWeek == 2)
			strNowPreviousDate = date.minusDays(3).toString();
		
		else
			strNowPreviousDate = date.minusDays(1).toString();
		System.out.println(strNowPreviousDate);
	}
}
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Toado {
	private double latitude;
	private double longitude;
}

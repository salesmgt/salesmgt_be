package com.app.demo.utils;

public class DistanceCalculateUtils {
	 public static double calculate(double lat1,double lon1, double lat2, double lon2) {
//		 var R = 6371; // km
//			var dLat = Math.toRadians(lat2-lat1);
//			var dLon = Math.toRadians(lon2-lon1); 
//			var a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
//			        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//			        Math.sin(dLon/2) * Math.sin(dLon/2); 
//			var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
//			return R * c;
		 
		 double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			System.out.println(dist*6371+" km rad");
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
			return dist;
	 }

}

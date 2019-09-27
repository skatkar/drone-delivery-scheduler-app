/**
 * 
 */
package com.dronescheduler.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dronescheduler.model.Order;

/**
 * @author SWAPNIL
 *
 */
public class DroneSchedulerUtils {

	private final static int TIME_PER_STEP_IN_MINUTES = 1; 
	
	public static LocalDate getCurrentDate(){
		return LocalDate.now();
	}
	
	public static LocalDateTime getCurrentDateTime(){
		return LocalDateTime.now();
	}
	
	// Method to calculate the time in minutes for drone delivery
	public static int getOrderDeliveryInSeconds(Order order){
		return getDroneDeliveryDistance(order) * TIME_PER_STEP_IN_MINUTES * 60;
	}
	
	public static int getDroneReturnTimeInSeconds(Order order){
		return (getDroneDeliveryDistance(order) * TIME_PER_STEP_IN_MINUTES * 60) / 2;
	}
	
	// Method to find the distance for a drone delivery
	public static int getDroneDeliveryDistance(Order order){
		int totalDistance = 0;
		int startIndex = 0, endIndex = 0;
		
		String orderLocation = order.getLocation();
		
		for(int i = 0; i < orderLocation.length(); i++ ){
			if(orderLocation.charAt(i) >= 48 && orderLocation.charAt(i) <= 57){
				endIndex++;
			}else if(orderLocation.charAt(i) == 'N' || orderLocation.charAt(i) == 'S'){
				startIndex = i + 1;
				endIndex = i + 1;
			}else if(orderLocation.charAt(i) == 'E' || orderLocation.charAt(i) == 'W'){
				totalDistance += Integer.parseInt(orderLocation.substring(startIndex,endIndex));
				startIndex = i + 1;
				endIndex = i + 1;
			}
		}
		
		totalDistance += Integer.parseInt(orderLocation.substring(startIndex,endIndex));
		
		return totalDistance;
	}
	
	// Method to return the ratingType based on the delivery time in hours 
	public static String getRatingType(){
		return "";
	}
}

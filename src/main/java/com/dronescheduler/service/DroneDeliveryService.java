/**
 * 
 */
package com.dronescheduler.service;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dronescheduler.dao.Warehouse;
import com.dronescheduler.exception.DroneDeliverySchedulerException;
import com.dronescheduler.exception.InvalidDeliveryOrderException;
import com.dronescheduler.factory.InputDataReaderFactory;
import com.dronescheduler.model.Order;
import com.dronescheduler.util.DroneSchedulerUtils;

/**
 * <h1>DroneDeliveryService</h1>
 * 
 * The class that handles the scheduling of the orders. 
 * 
 * @author SWAPNIL
 *
 */
public class DroneDeliveryService {

	private static DroneDeliveryService instance;
	
	private LocalDateTime currentDateTime;
	private LocalDateTime deliveryStartsAt;
	private LocalDateTime deliveryEndsAt;
	private Warehouse warehouse;
	
	/**
	 * Creates a DroneDeliveryService instance based on the provided input parameters.
	 * @param warehouse
	 * @param currentDateTime
	 * @param deliveryStartTime
	 * @param deliveryEndTime
	 */
	private DroneDeliveryService(Warehouse warehouse, LocalDateTime currentDateTime, String deliveryStartTime,String deliveryEndTime){
		this.warehouse = warehouse;
		this.currentDateTime = currentDateTime;
		
		String[] startTime = getTimeSplitted(deliveryStartTime);
		String[] endTime = getTimeSplitted(deliveryEndTime);
		
		this.deliveryStartsAt = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonth(), currentDateTime.getDayOfMonth(),
				Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
		this.deliveryEndsAt = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonth(), currentDateTime.getDayOfMonth(),
				Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
	}
	
	/**
	 * Method to split the time string into hour,minute and second. 
	 */
	private String[] getTimeSplitted(String time) {
		return time.split(":");
	}

	/**
	 * Returns the singleton instance of the service
	 * @param warehouse
	 * @param currentDateTime
	 * @param startTime
	 * @param endTime
	 * @return service instance
	 */
	public static DroneDeliveryService getInstance(String location, String inputType, LocalDateTime currentDateTime, String startTime, String endTime){
		if(instance == null){
			instance = new DroneDeliveryService(Warehouse.getInstance(InputDataReaderFactory.getInputReader(inputType, location, currentDateTime.toLocalDate())), currentDateTime, startTime, endTime);
		}else{
			if(!instance.currentDateTime.toLocalDate().isEqual(currentDateTime.toLocalDate())){
				instance.currentDateTime = currentDateTime;
				instance.warehouse.setInputDataReader(InputDataReaderFactory.getInputReader(inputType, location, currentDateTime.toLocalDate()));
				instance.deliveryStartsAt = LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.parse(startTime));
				instance.deliveryEndsAt = LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.parse(endTime));
			}
			
		}
		return instance;
	}
	
	/**
	 * Handles the scheduling of the orders. It first checks whether is it a good time to do the scheduling or not
	 * @return Map containing either the list of delivered & undelivered orders or status message describing why it did not work right now. 
	 * @throws DroneDeliverySchedulerException
	 */
	public Map<String,List<String>> handleOrders() throws DroneDeliverySchedulerException{
		
		if(canDeliverOrders()){
			warehouse.createOrders();
			return scheduleOrders(warehouse.getOrders());
		}
		Map<String, List<String>> outputData = new HashMap<String,List<String>>();
		List<String> statusDescription = new ArrayList<>();
		
		statusDescription.add("Orders can not be delivered at this moment.");
		outputData.put("status", statusDescription);
		return outputData;
	}
	
	/**
	 * A method to check whether the deliveries can be made or not.
	 */
	private boolean canDeliverOrders(){
		return (currentDateTime.isEqual(deliveryStartsAt) || currentDateTime.isAfter(deliveryStartsAt))
				&& currentDateTime.isBefore(deliveryEndsAt);
	}
	
	/**
	 * A method to schedule drone deliveries.
	 * @param orders
	 */
	private Map<String, List<String>> scheduleOrders(List<Order> orders) {
		Map<String, List<String>> outputData = new HashMap<>();
		List<String> deliveredOrdersData = new ArrayList<String>();
		List<String> undeliveredOrdersData = new ArrayList<String>();

		int promoters = 0, detractors = 0;

		for (Order order : orders) {

			try {

				
				if(order.getOrderedAt().equals(deliveryEndsAt) || order.getOrderedAt().isAfter(deliveryEndsAt)){
					//System.out.println("**"+order.getOrder_id() + " " + order.getOrderedAt() + " Order placed after delivery closing time");
					throw new InvalidDeliveryOrderException("Order placed at/after delivery closing time");
				}
				
				LocalDateTime estimatedDeliveryAt = getEstimatedTimeOfDelivery(order);
				//System.out.println("**"+order.getOrder_id() + " " + order.getOrderedAt() + " " + estimatedDeliveryAt +"**");

				if (estimatedDeliveryAt.isEqual(deliveryEndsAt) || estimatedDeliveryAt.isBefore(deliveryEndsAt)) {
					deliveredOrdersData.add(new StringBuilder(order.getOrder_id()).append(" ")
							.append(currentDateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS)).toString());

					long timeDuration = Duration.between(currentDateTime, estimatedDeliveryAt).getSeconds();

					if (timeDuration <= 3600) {
						promoters++;
					} else if (timeDuration >= 3600 * 4 && timeDuration <= 3600 * 10) {
						detractors++;
					}
					currentDateTime = estimatedDeliveryAt
							.plusSeconds(DroneSchedulerUtils.getDroneReturnTimeInSeconds(order));
				} else
					throw new InvalidDeliveryOrderException("Order delivery time exceeds delivery closing time");
			} catch (DateTimeException | InvalidDeliveryOrderException e) {
				undeliveredOrdersData
						.add(new StringBuilder(e.getMessage()).append(" ").append(order.getOrder_id()).toString());
			}

		}
		
		outputData.put("delivered", getNPS(deliveredOrdersData, promoters, detractors, orders.size()));
		outputData.put("undelivered", undeliveredOrdersData);

		return outputData;
	}
	
	/**
	 * A method to calculate the estimated time of delivery for a given order
	 * @param order
	 */
	private LocalDateTime getEstimatedTimeOfDelivery(Order order){
		int totalTravelTime = DroneSchedulerUtils.getOrderDeliveryInSeconds(order);
		
		int hours = totalTravelTime / 3600;
		int minutes = (totalTravelTime % 3600) / 60;
		int seconds = totalTravelTime % 60;
		
		return currentDateTime.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
	}
	
	/**
	 * Method to calculate NPS score.
	 * The computed score is appended to the existing output list
	 */
	private List<String> getNPS(List<String> outputData, int promoters, int detractors, int totalOrders){
		double NPSScore = (promoters * 100.0 / totalOrders) - (detractors * 100.0 / totalOrders); 
		outputData.add("NPS " + new DecimalFormat("###.###").format(NPSScore));
		return outputData;
	}
}

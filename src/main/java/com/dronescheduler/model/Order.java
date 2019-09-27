/**
 * 
 */
package com.dronescheduler.model;

import java.time.LocalDateTime;

/**
 * @author SWAPNIL
 *
 */
public class Order {

	String order_id;
	String location;
	int feedback;
	LocalDateTime orderedAt,deliveredAt;
	
	public Order(String order_id,String location,LocalDateTime orderedAt){
		this.order_id = order_id;
		this.location = location;
		this.orderedAt = orderedAt;
	}

	public String getOrder_id() {
		return order_id;
	}

	public String getLocation() {
		return location;
	}

	public int getFeedback() {
		return feedback;
	}

	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliveredAt) {
		this.deliveredAt = deliveredAt;
	}
	
	
}

/**
 * 
 */
package com.dronescheduler.comparator;

import java.util.Comparator;

import com.dronescheduler.model.Order;
import com.dronescheduler.util.DroneSchedulerUtils;

/**
 * @author SWAPNIL
 *
 */
public class OrderComparator implements Comparator<Order> {

	public int compare(Order order1, Order order2) {
		
		int r = Integer.compare(DroneSchedulerUtils.getDroneDeliveryDistance(order1) + DroneSchedulerUtils.getDroneDeliveryDistance(order1), 
				DroneSchedulerUtils.getDroneDeliveryDistance(order2) + DroneSchedulerUtils.getDroneDeliveryDistance(order2));
		return r == 0 ? order1.getOrderedAt().compareTo(order2.getOrderedAt()) : r;
	}

}

/**
 * 
 */
package com.dronescheduler.reader;

import java.util.List;

import com.dronescheduler.exception.DroneDeliverySchedulerException;
import com.dronescheduler.model.Order;

/**
 * @author SWAPNIL
 *
 */
public interface InputDataReader {

	public List<Order> readDataFromSource() throws DroneDeliverySchedulerException;
}

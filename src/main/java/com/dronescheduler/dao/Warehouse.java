/**
 * 
 */
package com.dronescheduler.dao;

import java.util.Collections;
import java.util.List;

import com.dronescheduler.comparator.OrderComparator;
import com.dronescheduler.exception.DroneDeliverySchedulerException;
import com.dronescheduler.model.Order;
import com.dronescheduler.reader.InputDataReader;

/**
 * @author SWAPNIL
 *
 */
public class Warehouse {

	private static Warehouse warehouse;
	
	private InputDataReader inputDataReader;
	private List<Order> orders;
	
	private Warehouse(InputDataReader inputDataReader){
		this.inputDataReader = inputDataReader;
	}
	
	
	public static Warehouse getInstance(InputDataReader inputDataReader){
		if(warehouse == null){
			warehouse = new Warehouse(inputDataReader);
		}
		
		return warehouse;
	}
	
	public void setInputDataReader(InputDataReader inputDataReader) {
		this.inputDataReader = inputDataReader;
	}


	public void createOrders() throws DroneDeliverySchedulerException{
		orders = inputDataReader.readDataFromSource();
		Collections.sort(orders,new OrderComparator());
	}
	
	public List<Order> getOrders(){
		return orders;
	}
}

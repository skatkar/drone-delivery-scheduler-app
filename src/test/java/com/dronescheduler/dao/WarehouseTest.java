package com.dronescheduler.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import com.dronescheduler.comparator.OrderComparator;
import com.dronescheduler.exception.DroneDeliverySchedulerException;
import com.dronescheduler.factory.InputDataReaderFactory;
import com.dronescheduler.model.Order;
import com.dronescheduler.reader.InputDataReader;


public class WarehouseTest {

	static InputDataReader inputDataReader;
	
	@Test
	public void testGetOrders() throws DroneDeliverySchedulerException {
		Warehouse warehouse = Warehouse.getInstance(InputDataReaderFactory.getInputReader("file", "inputTest.txt", LocalDate.of(2019,9,10)));
		warehouse.createOrders();
		List<Order> orders = warehouse.getOrders();
		assertEquals(4,orders.size());
	}
	
	@Test
	public void testGetOrders_Sorted() throws DroneDeliverySchedulerException{
		OrderComparator comparator = new OrderComparator();
		Warehouse warehouse = Warehouse.getInstance(InputDataReaderFactory.getInputReader("file", "inputTest.txt", LocalDate.of(2019,9,12)));
		warehouse.createOrders();
		
		List<Order> orders = warehouse.getOrders();
		Order order1 = orders.get(0);
		Order order2 = orders.get(1);
		
		int result = comparator.compare(order1, order2);
		assertEquals(4,orders.size());
		assertEquals(-1,result);
	}

}

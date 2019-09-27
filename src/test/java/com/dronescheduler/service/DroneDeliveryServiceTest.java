/**
 * 
 */
package com.dronescheduler.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.dronescheduler.exception.DroneDeliverySchedulerException;

/**
 * Test class for DroneDeliveryService methods
 * @author SWAPNIL
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DroneDeliveryServiceTest {

	DroneDeliveryService instance;
	static String location;
	
	
	@Test
	public void testHandleOrders_NoDeliveryNow() throws DroneDeliverySchedulerException{
		
		LocalDateTime testDateTime = LocalDateTime.of(2019,9,20,6,0,0);
						
		instance = DroneDeliveryService.getInstance("inputTest.txt", "file",testDateTime, "08:00:00", "22:00:00");
		assertEquals(1,instance.handleOrders().size());
	}
	
	@Test
	public void testHandleOrders_deliveredAll() throws DroneDeliverySchedulerException{
		
		LocalDateTime testDateTime = LocalDateTime.of(2019,9,21,6,0,0);
				
		instance = DroneDeliveryService.getInstance("inputTest.txt", "file",testDateTime, "06:00:00", "22:00:00");
		Map<String,List<String>> outputData = instance.handleOrders();
		System.out.println(outputData);
		assertNotNull(outputData);
		assertEquals(2,outputData.size());
		assertEquals(5,outputData.get("delivered").size());
		assertEquals(0,outputData.get("undelivered").size());
	}
	
	@Test
	public void testHandleOrders_notDeliveredAll() throws DroneDeliverySchedulerException{
		
		LocalDateTime testDateTime = LocalDateTime.of(2019,9,22,6,0,0);
		
		instance = DroneDeliveryService.getInstance("inputTest.txt", "file",testDateTime, "06:00:00", "07:00:00");
		Map<String,List<String>> outputData = instance.handleOrders();
		assertNotNull(outputData);
		assertEquals(2,outputData.size());
		assertEquals(4,outputData.get("delivered").size());
		assertEquals(1,outputData.get("undelivered").size());
	}
	
	@Test
	public void testHandleOrders_notDeliveredAll_dateNotChanged() throws DroneDeliverySchedulerException{
		
		LocalDateTime testDateTime = LocalDateTime.of(2019,9,22,6,0,0);
		
		instance = DroneDeliveryService.getInstance("inputTest.txt", "file",testDateTime, "06:00:00", "07:00:00");
		Map<String,List<String>> outputData = instance.handleOrders();
		assertNotNull(outputData);
		assertEquals(2,outputData.size());
		assertEquals(1,outputData.get("delivered").size());
		assertEquals(4,outputData.get("undelivered").size());
	}
	
	@Test
	public void testHandleOrders_deliveredNone() throws DroneDeliverySchedulerException{
		System.out.println("** testHandleOrders_deliveredNone() **");
		LocalDateTime testDateTime = LocalDateTime.of(2019,9,23,5,0,0);
				
		instance = DroneDeliveryService.getInstance("inputTest.txt", "file",testDateTime, "05:00:00", "05:05:00");
		Map<String,List<String>> outputData = instance.handleOrders();
		assertNotNull(outputData);
		assertEquals(2,outputData.size());
		System.out.println(outputData.get("delivered").get(0));
		assertEquals("NPS 0",outputData.get("delivered").get(0));
		assertEquals(4,outputData.get("undelivered").size());
	}
}

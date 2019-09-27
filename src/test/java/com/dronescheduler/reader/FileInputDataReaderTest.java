/**
 * 
 */
package com.dronescheduler.reader;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import com.dronescheduler.exception.DroneDeliverySchedulerException;
import com.dronescheduler.model.Order;

/**
 * @author SWAPNIL
 *
 */
public class FileInputDataReaderTest {

	InputDataReader inputReader;
	
	@Test
	public void readDataFromSourceTest() throws DroneDeliverySchedulerException{
		inputReader = new FileInputDataReader("inputTest.txt", LocalDate.now());
		List<Order> orders = inputReader.readDataFromSource();
		assertEquals(4,orders.size());
	}
	
	@Test(expected = DroneDeliverySchedulerException.class)
	public void whenExceptionThrown_thenExpectationSatisfied() throws DroneDeliverySchedulerException{
		inputReader = new FileInputDataReader("sample.txt", LocalDate.now());
		List<Order> orders = inputReader.readDataFromSource();
	}
}

/**
 * 
 */
package com.dronescheduler.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dronescheduler.exception.DroneDeliverySchedulerException;
import com.dronescheduler.model.Order;

/**
 * @author SWAPNIL
 *
 */
public class FileInputDataReader implements InputDataReader {

	private String fileLocation;
	private LocalDate currentDate;
		
	public FileInputDataReader(String fileLocation, LocalDate currentDate){
		this.fileLocation = fileLocation;
		this.currentDate  = currentDate;
	}
	
	/**
	 *  Read the file and create the list of orders
	 */
	public List<Order> readDataFromSource() throws DroneDeliverySchedulerException {
		List<Order> orders = new ArrayList<Order>();

		File file = new File(fileLocation);
		try (BufferedReader fileReader = new BufferedReader(new FileReader(file));) {

			String line;

			while ((line = fileReader.readLine()) != null) {
				String[] orderData = line.split(" ");
				String[] timeSeparated = orderData[2].split(":");

				String orderId = orderData[0];
				String deliveryLocation = orderData[1];
				
				LocalDateTime orderTime = LocalDateTime.of(currentDate.getYear(), currentDate.getMonth(),
						currentDate.getDayOfMonth(), Integer.parseInt(timeSeparated[0]),
						Integer.parseInt(timeSeparated[1]), Integer.parseInt(timeSeparated[2]));

				orders.add(new Order(orderId, deliveryLocation, orderTime));
			}
			return orders;
		} catch (IOException e) {
			throw new DroneDeliverySchedulerException("Exception while fetching data from the input source");
		}

	}

	
}

package com.dronescheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.dronescheduler.service.DroneDeliveryService;
import com.dronescheduler.util.DroneSchedulerUtils;

/**
 * The main class to initiate the drone scheduling process
 * @author SWAPNIL
 *
 */
public class DroneScheduler {
	public static void main(String[] args) {
		
		if(args.length == 0){
			System.out.println("Please provide the input file path. Exiting now.");
			System.exit(-1);
		}
		
		System.out.println("Code execution started using an input file: " + args[0]);
		DroneDeliveryService service = DroneDeliveryService.getInstance(args[0],
				"file",DroneSchedulerUtils.getCurrentDateTime(), "06:00:00", "22:00:00");
		
		try {
			
			Map<String,List<String>> output = service.handleOrders();
			if(output.size() == 1){
				System.out.println(output.get("status").get(0));
			}else{
				output.forEach((key,value) ->{
					try {
						//System.out.println(key + value);
						writeToFile(key,value);
					} catch (Exception e) {
						System.out.println("Exception while writing " + key + " data to the file");
						e.printStackTrace();
					}
				});
			}
			
		} catch (Exception e) {
			System.out.println("Exception while sceduling the drone delivery");
			e.printStackTrace();
		}
		
		System.out.println("Output files are available at output directory");
	}
	
	/*
	 * A method to write the provided data to the file. The output file will be placed in the output directory
	 */
	private static void writeToFile(String fileType, List<String> dataToWrite) throws Exception{
		String directoryName = "output";
		File directory = new File(directoryName);

		if (! directory.exists()){
	        directory.mkdir();
	    }
		
		String fileName = directoryName + "//" +DroneSchedulerUtils.getCurrentDate() + "_" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")) + "_" + fileType + ".txt";
		File file = new File(fileName);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file));){
			for(String line : dataToWrite){
				writer.write(line + "\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

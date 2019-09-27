/**
 * 
 */
package com.dronescheduler.factory;

import java.time.LocalDate;

import com.dronescheduler.reader.FileInputDataReader;
import com.dronescheduler.reader.InputDataReader;

/**
 * @author SWAPNIL
 *
 */
public class InputDataReaderFactory {

	public static InputDataReader getInputReader(String inputType,String fileLocation, LocalDate currentDate) {
		switch(inputType) {
		case "file":
			return new FileInputDataReader(fileLocation,currentDate);
		default: return null;
		}
	}
}

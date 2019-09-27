/**
 * 
 */
package com.dronescheduler.exception;

/**
 * @author SWAPNIL
 *
 */
public class InvalidDeliveryOrderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidDeliveryOrderException(String message){
		super(message);
	}

}

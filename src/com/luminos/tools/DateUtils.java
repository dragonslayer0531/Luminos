package com.luminos.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * Sets up date utilities for client
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class DateUtils {

	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat hour = new SimpleDateFormat("HH"), 
			minute = new SimpleDateFormat("mm"), 
			second = new SimpleDateFormat("ss");
	

	/**
	 * Calculates current time as a float
	 * 
	 * @return		Time of Day in Seconds
	 */
	public static float getTimeOfDaySeconds() {
		return (((Float.parseFloat(hour.format(cal.getTime())) * 3600) + 
				(Float.parseFloat(minute.format(cal.getTime())) * 60) + 
				Float.parseFloat(second.format(cal.getTime()))) * 1000);
	}
	
	/**
	 * Gets the date time in format yyyyMMdd_HHmmss
	 * 
	 * @return		Date time
	 */
	public static String getDateTime() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return df.format(cal.getTime());
	}
	
}

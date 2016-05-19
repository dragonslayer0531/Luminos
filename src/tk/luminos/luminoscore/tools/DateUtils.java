package tk.luminos.luminoscore.tools;

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

	static SimpleDateFormat hour, minute, second;
	public static float totalTime;
	private static Calendar cal;
	
	/**
	 * Constructor
	 */
	public DateUtils() {
		
	}

	/**
	 * Calculates current time as a float
	 */
	private void realTime() {
		cal = Calendar.getInstance();
		hour = new SimpleDateFormat("HH");
		minute = new SimpleDateFormat("mm");
		second = new SimpleDateFormat("ss");

		totalTime = (((Float.parseFloat(hour.format(cal.getTime())) * 3600) + (Float.parseFloat(minute.format(cal.getTime())) * 60) + Float.parseFloat(second.format(cal.getTime()))) * 1000);
	}
	
	/**
	 * Gets total time
	 * 
	 * @return Current Time
	 */
	public float getCurrentTime() {
		realTime();
		return totalTime;
	}

}

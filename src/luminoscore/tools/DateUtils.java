package luminoscore.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

	static SimpleDateFormat hour, minute, second;
	public static float totalTime;
	private static Calendar cal;
	
	public DateUtils() {
		
	}

	private void realTime() {
		cal = Calendar.getInstance();
		hour = new SimpleDateFormat("HH");
		minute = new SimpleDateFormat("mm");
		second = new SimpleDateFormat("ss");

		totalTime = (((Float.parseFloat(hour.format(cal.getTime())) * 3600) + (Float.parseFloat(minute.format(cal.getTime())) * 60) + Float.parseFloat(second.format(cal.getTime()))) * 1000);
	}
	
	public void cleanUp() {
		cal = null;
		hour = null;
		minute = null;
		second = null;
		totalTime = 0;
	}
	
	public float getTotalTime() {
		realTime();
		return totalTime;
	}

}

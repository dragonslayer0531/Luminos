package tk.luminos.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class PerformanceMonitor {
	
	private OperatingSystemMXBean bean;
	
	private int availableProcessors;
	private long lastSystemTime = 0;
	private long lastProcessCPUTime = 0;
	
	public PerformanceMonitor() {
		super();
		bean = ManagementFactory.getOperatingSystemMXBean();
		availableProcessors = bean.getAvailableProcessors();
	}
	
	public synchronized double getProcessorUsage() {
		if (lastSystemTime == 0) {
			baselineCounters();
			return -1;
		}
		
		long systemTime = System.nanoTime();
		long processCPUTime = 0;
		
		if ( bean instanceof com.sun.management.OperatingSystemMXBean )
			processCPUTime = ( (com.sun.management.OperatingSystemMXBean) bean).getProcessCpuTime();
		
		double cpuUsage = (processCPUTime - lastProcessCPUTime) / ((double) (systemTime - lastSystemTime));
		lastSystemTime = systemTime;
		lastProcessCPUTime = processCPUTime;
		
		return cpuUsage / availableProcessors;
	}

	private void baselineCounters() {
		lastSystemTime = System.nanoTime();
		
		if ( bean instanceof com.sun.management.OperatingSystemMXBean )
			lastProcessCPUTime = ( (com.sun.management.OperatingSystemMXBean) bean).getProcessCpuTime();
	}

}

package tk.luminos;

public class Luminos {
	
	/**Integer value representing false in the engine*/public static final Integer LUMINOS_FALSE = 0x0;
	/**Integer value representing true in the engine*/public static final Integer LUMINOS_TRUE = 0x1;
	
	public enum ExitStatus {
		
		SUCCESS(0x0),
		FAILURE_GENERAL(0x1);
		
		private int status = 0;
		private ExitStatus(int status) {
			this.status = status;
		}
		
	}
	
	/**
	 * Exits the engine with a given error code
	 * 
	 * @param status		Exit status
	 */
	public static void exit(ExitStatus status) {
		System.exit(status.status);
	}

}

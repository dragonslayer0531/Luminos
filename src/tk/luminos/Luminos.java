package tk.luminos;

public class Luminos {
	
	/**Integer value representing false in the engine*/public static final Integer LUMINOS_FALSE = 0x0;
	/**Integer value representing true in the engine*/public static final Integer LUMINOS_TRUE = 0x1;
	/**Integer value representing front faces*/public static final Integer FRONT_FACE = 0x404;
	/**Integer value representing back faces*/public static final Integer BACK_FACE = 0x405;
	/**Integer value representing both front and back faces*/public static final Integer FRONT_AND_BACK_FACE = 0x408;
	
	/**Integer value representing an unexpected exit*/public static final Integer EXIT_UNEXPECTED = -0x1;
	/**Integer value representing an expected exit*/public static final Integer EXIT_SUCCESS = 0x0;
	/**Integer value representing a failure exit*/public static final Integer EXIT_FAILURE = 0x1;
	
	/**
	 * Exits the engine with a given error code
	 * 
	 * @param status		Exit status
	 */
	public static void exit(Integer status) {
		System.out.println("EXIT STATUS: " + status);
		System.exit(status);
	}

}

package tk.luminos;

public class Luminos {
	
	public static final Integer LUMINOS_FALSE = 0x0;
	public static final Integer LUMINOS_TRUE = 0x1;
	public static final Integer FRONT_FACE = 0x404;
	public static final Integer BACK_FACE = 0x405;
	public static final Integer FRONT_AND_BACK_FACE = 0x408;
	
	public static final Integer EXIT_UNEXPECTED = -0x1;
	public static final Integer EXIT_SUCCESS = 0x0;
	public static final Integer EXIT_FAILURE = 0x1;
	
	public static void exit(Integer status) {
		System.exit(status);
	}

}

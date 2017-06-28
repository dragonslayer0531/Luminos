package tk.luminos.util;

import tk.luminos.Application;

/**
 * Random for Engine
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Random extends java.util.Random {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates random
	 */
	public Random() {
		super();
		this.setSeed(Application.getValue("SEED"));;
	}

}

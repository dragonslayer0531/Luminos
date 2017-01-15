package tk.luminos.tools;

import static tk.luminos.ConfigData.SEED;

public class Random extends java.util.Random {

	private static final long serialVersionUID = 1L;
	
	public Random() {
		super();
		this.setSeed(SEED);;
	}

}

package com.luminos.tools;

import static com.luminos.ConfigData.SEED;

public class Random extends java.util.Random {

	private static final long serialVersionUID = 1L;
	
	public Random() {
		super();
		this.setSeed(SEED);;
	}

}

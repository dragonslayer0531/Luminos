package com.luminos.audio;

public class AudioSource {
	
	private int bufferID;
	
	public AudioSource(int id) {
		this.bufferID = id;
	}

	public int getID() {
		return bufferID;
	}

}

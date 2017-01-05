package com.luminos.graphics.gameobjects;

import com.luminos.tools.maths.vector.Vector3f;

public interface Light {
	
	public Vector3f getColor();

}

class Attenuation {
	
	public float constant = 0;
	public float linear = 0;
	public float exponent = 0;
	
	public Attenuation() {
		
	}
	
	public Vector3f getAttenuation() {
		return new Vector3f(exponent, linear, constant);
	}
	
}
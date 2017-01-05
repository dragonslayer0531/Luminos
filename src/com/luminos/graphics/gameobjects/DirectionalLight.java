package com.luminos.graphics.gameobjects;

import com.luminos.tools.maths.vector.Vector3f;

public class DirectionalLight implements Light {
	
	private Vector3f color;
	private Vector3f direction;
	private float intensity;

	public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		this.color = color;
		this.direction = direction;
		this.intensity = intensity;
	}
	
	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	@Override
	public Vector3f getColor() {
		return color;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public float getIntensity() {
		return intensity;
	}

}
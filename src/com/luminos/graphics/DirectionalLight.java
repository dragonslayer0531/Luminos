package com.luminos.graphics;

import org.joml.Vector3f;

public class DirectionalLight {
	
	private Vector3f color;
	private Vector3f direction;
	
	private float intensity;

	public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		this.color = color;
		this.direction = direction;
		this.intensity = intensity;
	}
	
	public DirectionalLight(DirectionalLight light) {
		this(new Vector3f(light.color), new Vector3f(light.direction), light.intensity);
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

}

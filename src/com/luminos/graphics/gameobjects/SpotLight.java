package com.luminos.graphics.gameobjects;

import com.luminos.tools.maths.vector.Vector3f;

public class SpotLight implements Light {

	private PointLight pointLight;
	private Vector3f direction;
	private float angle;
	
	public SpotLight(PointLight pointLight, Vector3f direction, float angle) {
		this.pointLight = pointLight;
		this.direction = direction;
		this.angle = angle;
	}

	public PointLight getPointLight() {
		return pointLight;
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	private String id;
		
	@Override
	public String getID() {
		return id;
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public Vector3f getColor() {
		return null;
	}

	
	
}

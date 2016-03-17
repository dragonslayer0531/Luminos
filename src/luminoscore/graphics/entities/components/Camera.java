package luminoscore.graphics.entities.components;

import luminoscore.graphics.entities.Entity;
import luminoscore.util.math.Vector3f;

public class Camera {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/17/2016
	 */
	
	//Constructor Fields
	private Entity entity;
	
	//Data Fields
	private float distanceFromPlayer = 0;
	private float angleAroundPlayer = 0;
	private float pitch = 0;
	private float roll = 0;
	private float yaw = 0;

	private float pitchChangeSpeed = 0;
	private Vector3f position = new Vector3f(0, 0, 0);

	//Constructor
	public Camera(Entity entity) {
		this.entity = entity;
	}
	
	//Moves the camera
	public void move() {
		float hd = calculateHorizontalDistance();
		float vd = calculateVerticalDistance();
		calculateCameraPosition(hd, vd);
		this.yaw = 180 - (entity.getRotation().y + angleAroundPlayer);
		this.roll = entity.getRotation().z;
		this.pitch = entity.getRotation().x;
		
	}
	
	//Calculated the position of the camera
	private void calculateCameraPosition(float hd, float vd) {
		float theta = entity.getRotation().y + angleAroundPlayer;
		float offsetX = (float) (hd * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (hd * Math.cos(Math.toRadians(theta)));
		
		position.x = entity.getPosition().x - offsetX;
		position.y = entity.getPosition().y + vd + 4;
		position.z = entity.getPosition().z - offsetZ;
	}
	
	//Calculates the horizontal displacement of the camera
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	//Calculates the vertical displacement of the camera
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	//Invert Pitch
	public void invertPitch() {
		pitch *= -1;
	}

	//Getter-Setter Methods
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}

	public void setDistanceFromPlayer(float distanceFromPlayer) {
		this.distanceFromPlayer = distanceFromPlayer;
	}

	public float getAngleAroundPlayer() {
		return angleAroundPlayer;
	}

	public void setAngleAroundPlayer(float angleAroundPlayer) {
		this.angleAroundPlayer = angleAroundPlayer;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitchChangeSpeed() {
		return pitchChangeSpeed;
	}

	public void setPitchChangeSpeed(float pitchChangeSpeed) {
		this.pitchChangeSpeed = pitchChangeSpeed;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

}

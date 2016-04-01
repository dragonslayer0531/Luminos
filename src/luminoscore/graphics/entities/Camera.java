package luminoscore.graphics.entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static float distanceFromPlayer = 25f;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	public static float pitch = 0;
	public static float pitchChangeSpeed = 0;
	static float yaw = 0;
	
	private float roll;
	
	private Entity player;
	
	public Camera(Entity player){
		this.player = player;
	}
	
	public void move(){
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		Camera.yaw = 180 - (player.getRotation().y + angleAroundPlayer);
		yaw%=360;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = player.getRotation().y + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + 4;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	public void invertPitch() {
		pitch *= -1;
	}
}

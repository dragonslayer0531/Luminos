package luminoscore.graphics.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * The Camera determines the view matrix of the GL Viewport
 *
 */

public class Camera {
	
	private static float distanceFromPlayer = 3f;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	public static float pitch = 0;
	public static float pitchChangeSpeed = 0;
	static float yaw = 0;
	
	private float roll;
	
	private Entity player;
	
	/**
	 * @param player	Determines the focal {@link entity} of the camera
	 * 
	 * Constructor determining the positioning of the Camera
	 */
	public Camera(Entity player){
		this.player = player;
		this.player.setCamera(this);
	}
	
	/**
	 * Called after the focal entity moves.  Moves the camera in respect to the entity
	 */
	public void move(){
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		Camera.yaw = 180 - (player.getRotation().y + angleAroundPlayer);
		yaw%=360;
	}

	/**
	 * @return Vector3f 	Value containing the positon of the camera
	 * 
	 * Gets the position of the camera
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return float		Value containing the pitch rotation of the camera
	 * 
	 * Gets the pitch of the camera
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return float		Value containing the yaw rotation of the camera
	 * 
	 * Gets the yaw of the camera
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return float		Value containing the roll rotation of the camera
	 * 
	 * Gets the roll of the camera
	 */
	public float getRoll() {
		return roll;
	}
	
	/**
	 * Inverts the pitch of the camera
	 */
	public void invertPitch() {
		pitch *= -1;
	}
	
	//**********************************Private Methods*******************************************//
	
	/**
	 * @param horizDistance  	Horizontal distance between camera and entity
	 * @param verticDistance	Vertical distance between camera and entity
	 * 
	 * Calculates the camera's rotation and position
	 */
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = player.getRotation().y + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + .5f;
	}
	
	/**
	 * @return float			X offset of camera
	 * 
	 * Calculates the X offset of the camera
	 */
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	/**
	 * @return float			Z offset of camera
	 * 
	 * Calculate the Z offset of the camera
	 */
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
}

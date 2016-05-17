package luminoscore.tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import luminoscore.GlobalLock;
import luminoscore.graphics.entities.Camera;

/**
 * 
 * Creates mouse picker
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class MousePicker {
	
	private Vector3f currentRay;
	
	private Matrix4f projectionMatrix, viewMatrix;
	private Camera camera;
	
	/**
	 * Constructor
	 * 
	 * @param camera			Camera to cast from
	 * @param projectionMatrix	Projection matrix of camera
	 */
	public MousePicker(Camera camera, Matrix4f projectionMatrix) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}
	
	/**
	 * Gets ray from mouse
	 * 
	 * @return	Current Ray
	 */
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	/**
	 * Updates view matrix and calculates
	 */
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateRay();
	}
	
//*************************************************Private Methods***********************************************//
	
	/**
	 * Calculates ray
	 * 
	 * @return Mouse's ray
	 */
	private Vector3f calculateRay() {
		float mouseX = 0;
		float mouseY = 0;
		Vector2f normalizedCoords = getNormalizedCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	/**
	 * Converts Eye Coordinates to World Coordinates
	 * 
	 * @param eyeCoords	Eye coordinates
	 * @return 	World Coordinates
	 */
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	/**
	 * Calculates Eye Coordinates from Clip Coordinates
	 * 
	 * @param clipCoords	Clip coordinates of ray
	 * @return 				Eye coordinates
	 */
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	/**
	 * Normalizes mouse position coordinates
	 * 
	 * @param mouseX	X position of mouse
	 * @param mouseY	Y position of mouse
	 * @return 			Normalzied Coords
	 */
	private Vector2f getNormalizedCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / GlobalLock.WIDTH;
		float y = (2f * mouseY) / GlobalLock.HEIGHT;
		return new Vector2f(x, y);
	}

}

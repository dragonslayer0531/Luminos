package luminoscore.tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Camera;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Creates mouse picker
 *
 */

public class MousePicker {
	
	private Vector3f currentRay;
	private GLFWWindow window;
	
	private Matrix4f projectionMatrix, viewMatrix;
	private Camera camera;
	
	/**
	 * @param camera			Camera to cast from
	 * @param projectionMatrix	Projection matrix of camera
	 * @param window			Window to cast from
	 * 
	 * Constructor
	 */
	public MousePicker(Camera camera, Matrix4f projectionMatrix, GLFWWindow window) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.window = window;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}
	
	/**
	 * @return Vector3f	Current Ray
	 * 
	 * Gets ray from mouse
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
	 * @return Vector3f ray
	 * 
	 * Calculates ray
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
	 * @param eyeCoords	Eye coordinates
	 * @return Vector3f	World Coordinates
	 * 
	 * Converts Eye Coordinates to World Coordinates
	 */
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	/**
	 * @param clipCoords	Clip coordinates of ray
	 * @return Vector4f		Eye coordinates
	 * 
	 * Calculates Eye Coordinates from Clip Coordinates
	 */
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	/**
	 * @param mouseX	X position of mouse
	 * @param mouseY	Y position of mouse
	 * @return Vector2f	Normalzied Coords
	 * 
	 * Normalizes mouse position coordinates
	 */
	private Vector2f getNormalizedCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / window.getWidth();
		float y = (2f * mouseY) / window.getHeight();
		return new Vector2f(x, y);
	}

}

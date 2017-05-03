package tk.luminos.utilities;

import java.util.List;

import tk.luminos.Application;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.Camera;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

/**
 * Class that sets up mouse data
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private Vector3 currentRay = new Vector3();

	private Matrix4 projectionMatrix;
	private Matrix4 viewMatrix;
	private Camera camera;
	
	private List<Terrain> terrain;
	private Vector3 currentTerrainPoint;

	/**
	 * Constructor
	 * 
	 * @param cam			Camera used to render
	 * @param projection	Projection matrix
	 * @param terrain		Terrains
	 */
	public MousePicker(Camera cam, Matrix4 projection, List<Terrain> terrain) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = MathUtils.createViewMatrix(camera);
		this.terrain = terrain;
	}
	
	/**
	 * Gets the point on the world that was selected
	 * 
	 * @return	point on the world
	 */
	public Vector3 getWorldPoint() {
		if(currentTerrainPoint == null) return null;
		if(currentTerrainPoint.y > 0) return currentTerrainPoint;
		else return new Vector3(currentTerrainPoint.x, 0, currentTerrainPoint.z);
	}

	/**
	 * Gets current mouse ray
	 * 
	 * @return	current mouse ray
	 */
	public Vector3 getCurrentRay() {
		return currentRay;
	}

	/**
	 * Updates the mouse picker
	 */
	public void update() {
		viewMatrix = MathUtils.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
	}

	private Vector3 calculateMouseRay() {
		float mouseX = 0;
		float mouseY = 0;
		Vector2 normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4 clipCoords = new Vector4(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4 eyeCoords = toEyeCoords(clipCoords);
		Vector3 worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3 toWorldCoords(Vector4 eyeCoords) {
		Matrix4 invertedView = Matrix4.invert(viewMatrix, null);
		Vector4 rayWorld = Matrix4.transform(invertedView, eyeCoords, null);
		Vector3 mouseRay = new Vector3(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}

	private Vector4 toEyeCoords(Vector4 clipCoords) {
		Matrix4 invertedProjection = Matrix4.invert(projectionMatrix, null);
		Vector4 eyeCoords = Matrix4.transform(invertedProjection, clipCoords, null);
		return new Vector4(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2 getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Application.getValue("WIDTH") - 1f;
		float y = (2.0f * mouseY) / Application.getValue("HEIGHT") - 1f;
		return new Vector2(x, y);
	}
		
	private Vector3 getPointOnRay(Vector3 ray, float distance) {
		Vector3 camPos = camera.getPosition();
		Vector3 start = new Vector3(camPos.x, camPos.y, camPos.z);
		Vector3 scaledRay = new Vector3(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3.add(start, scaledRay, null);
	}
	
	private Vector3 binarySearch(int count, float start, float finish, Vector3 ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3 endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.x, endPoint.z);
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3 ray) {
		Vector3 startPoint = getPointOnRay(ray, start);
		Vector3 endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isUnderGround(Vector3 testPoint) {
		Terrain terrain = getTerrain(testPoint.x, testPoint.z);
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	private Terrain getTerrain(float worldX, float worldZ) {
		for(Terrain t : terrain) {
			if(t.isOnTerrain(new Vector3(worldX, 0, worldZ))) return t;
		}
		return null;
	}

}

package com.luminos.tools;

import java.util.List;

import com.luminos.ConfigData;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector2f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.maths.vector.Vector4f;

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

	private Vector3f currentRay = new Vector3f();

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private List<Terrain> terrain;
	private Vector3f currentTerrainPoint;

	/**
	 * Constructor
	 * 
	 * @param cam			Camera used to render
	 * @param projection	Projection matrix
	 * @param terrain		Terrains
	 */
	public MousePicker(Camera cam, Matrix4f projection, List<Terrain> terrain) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = Maths.createViewMatrix(camera);
		this.terrain = terrain;
	}
	
	/**
	 * Gets the point on the world that was selected
	 * 
	 * @return	point on the world
	 */
	public Vector3f getWorldPoint() {
		if(currentTerrainPoint == null) return null;
		if(currentTerrainPoint.y > 0) return currentTerrainPoint;
		else return new Vector3f(currentTerrainPoint.x, 0, currentTerrainPoint.z);
	}

	/**
	 * Gets current mouse ray
	 * 
	 * @return	current mouse ray
	 */
	public Vector3f getCurrentRay() {
		return currentRay;
	}

	/**
	 * Updates the mouse picker
	 */
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
	}

	private Vector3f calculateMouseRay() {
		float mouseX = 0;
		float mouseY = 0;
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / ConfigData.WIDTH - 1f;
		float y = (2.0f * mouseY) / ConfigData.HEIGHT - 1f;
		return new Vector2f(x, y);
	}
		
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
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

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isUnderGround(Vector3f testPoint) {
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
			if(t.isOnTerrain(new Vector3f(worldX, 0, worldZ))) return t;
		}
		return null;
	}

}

package luminoscore.tools;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.entities.Camera;

public class Maths {

	/**
	 * @param translation	2D Translation
	 * @param scale			2D Scale
	 * @return Matrix4f		Transformation Matrix
	 * 
	 * Creates transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	/**
	 * @param translation	3D Translation
	 * @param rx			Rotation around X
	 * @param ry			Rotation around Y
	 * @param rz			Rotation around Z
	 * @param scale			Uniform scale
	 * @return Matrix4f		Transformation Matrix
	 * 
	 * Creates transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	/**
	 * @param translation	3D Translation
	 * @param rotation		3D Rotation
	 * @param scale			Uniform Scale
	 * @return Matrix4f		Transformation Matrix
	 * 
	 * Creates transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		return Maths.createTransformationMatrix(translation, rotation.x, rotation.y, rotation.z, scale);
	}
	
	/**
	 * @param translation	3D Translation
	 * @param rx			Rotation around X
	 * @param ry			Rotation around Y
	 * @param rz			Rotation around Z
	 * @param scale			2D scale
	 * @return Matrix4f		Transformation Matrix
	 * 
	 * Creates transformation matrix
	 */
	public static Matrix4f createWaterTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, 1, scale.y), matrix, matrix);
		return matrix;
	}

	/**
	 * @param p1		Value One
	 * @param p2		Value Two
	 * @param p3		Value Three
	 * @param pos		Position inside values
	 * @return float	Weighted value
	 * 
	 * Calculates barycenter of points
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	/**
	 * @param camera	Camera for view matrix
	 * @return Matrix4f	View Matrix
	 * 
	 * Creates view matrix
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	/**
	 * @param pointOne	First Point
	 * @param pointTwo	Second Point
	 * @return float	Distance
	 * 
	 * Gets distance between two points
	 */
	public static float getDistance(Vector3f pointOne, Vector3f pointTwo) {
		float distance = 0;

		float x1 = pointOne.x;
		float y1 = pointOne.y;
		float z1 = pointOne.z;

		float x2 = pointTwo.x;
		float y2 = pointTwo.y;
		float z2 = pointTwo.z;

		distance = (float) Math.pow((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2)), .5f);

		return distance;
	}
	
	/**
	 * @param origin	Point of origin
	 * @param points	Points to be checked
	 * @return Vector3f	Furthest point
	 * 
	 * Determines furthest point from origin
	 */
	public static Vector3f getFurthestPoint(Vector3f origin, List<Vector3f> points) {
		float distance = getDistance(origin, points.get(0));
		Vector3f positionVector = points.get(0);
		
		for(int i = 0; i < points.size(); i++) {
			if(getDistance(origin, points.get(i)) > distance) {
				distance = getDistance(origin, points.get(i));
				positionVector = points.get(i);
			}
		}
		
		return positionVector;
		
	}
	
	/**
	 * @param origin	Point of origin
	 * @param points	Points to be checked	
	 * @return Vector3f	Closest point
	 * 
	 * Determines closest point to origin
	 */
	public static Vector3f getClosestPoint(Vector3f origin, List<Vector3f> points) {
		
		float distance = getDistance(origin, points.get(0));
		Vector3f positionVector = points.get(0);
		
		for(Vector3f point : points) {
			float distance_cur = getDistance(origin, point);
			if(distance_cur < distance) {
				distance = distance_cur;
				positionVector = point;
			}
		}
		
		return positionVector;
		
	}
	
	/**
	 * @param pos1		Position One
	 * @param pos2		Position Two
	 * @param pos3		Position Three
	 * @return Vector3f	Normal of plane
	 * 
	 * Calculates the normal of the plane given by pos1, pos2, and pos3
	 */
	public static Vector3f getNormal(Vector3f pos1, Vector3f pos2, Vector3f pos3) {
		
		Vector3f normalVector = null;
		
		Vector3f vec1 = Vector3f.sub(pos2, pos1, null);
		Vector3f vec2 = Vector3f.sub(pos3, pos1, null);
		
		normalVector = new Vector3f(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
		
		
		return normalVector;
		
	}
	
	/**
	 * @param a			First value
	 * @param b			Second value
	 * @param f			Blend value
	 * @return float	Interpolated value
	 * 
	 * Calculates linear interpolation between values
	 */
	public static float LinearInterpolation(float a, float b, float f) {
		float possible = (a + b + f) / 3;
		if(Math.abs(possible) < 1) {
			return possible;
		} else {
			return 1;
		}
	}
	
	/**
	 * @param a			First value
	 * @param b			Second value
	 * @param blend		Blend value
	 * @return float	Interpolated value
	 * 
	 * Calculates cosine interpolation between values
	 */
	public static float CosineInterpolation(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float)(1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}
	
	/**
	 * @param in		Middle value
	 * @param param1	Lower bound
	 * @param param2	Upper bound
	 * @return boolean	Between upper and lower
	 * 
	 * Calculates if 
	 */
	public static boolean isBetween(float in, float param1, float param2) {
		float one = param1;
		float two = param2;
		if(param1 > param2) {
			one = param2;
			two = param1;
		}
		return (in >= one && in <= two);
	}

}

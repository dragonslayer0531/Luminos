package tk.luminos.luminoscore.tools;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.gameobjects.Camera;

/**
 * 
 * Complex math calculations for the engine.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Maths {

	/**
	 * Creates transformation matrix
	 * 
	 * @param translation	2D Translation
	 * @param scale			2D Scale
	 * @return 				Transformation Matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	/**
	 * Creates transformation matrix
	 * 
	 * @param translation	3D Translation
	 * @param rx			Rotation around X
	 * @param ry			Rotation around Y
	 * @param rz			Rotation around Z
	 * @param scale			Uniform scale
	 * @return 				Transformation Matrix
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
	 * Creates transformation matrix
	 * 
	 * @param translation	3D Translation
	 * @param rotation		3D Rotation
	 * @param scale			Uniform Scale
	 * @return 				Transformation Matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		return Maths.createTransformationMatrix(translation, rotation.x, rotation.y, rotation.z, scale);
	}
	
	/**
	 * Creates transformation matrix
	 * 
	 * @param translation	3D Translation
	 * @param rotation		3D Rotation
	 * @param scale			3D Scale
	 * @return				Transformation Matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	
	/**
	 * Creates transformation matrix
	 * 
	 * @param translation	3D Translation
	 * @param rx			Rotation around X
	 * @param ry			Rotation around Y
	 * @param rz			Rotation around Z
	 * @param scale			2D scale
	 * @return 				Transformation Matrix
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
	 * Rotates a point around the origin
	 * 
	 * @param point			Location vector to rotate
	 * @param rotation		Rotation vector
	 * @return				Location vector of rotated point vector
	 */
	public static Vector3f rotate(Vector3f point, Vector3f rotation) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.m00 = point.x;
		matrix.m11 = point.y;
		matrix.m22 = point.z;
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		return new Vector3f(matrix.m00, matrix.m11, matrix.m22);
	}
	
	/**
	 * Checks the equivalence of two point vectors
	 * 
	 * @param one		Point vector one
	 * @param two		Point vector two
	 * @param error		Value of difference allowed to pass
	 * @return			Are the points similar within the error
	 */
	public static boolean checkEquivalence(Vector3f one, Vector3f two, float error) {
		if(one.x - two.x < error || one.y - two.y < error || one.z - two.z < error) return true;
		return false;
	}

	/**
	 * Calculates barycenter of points
	 * 
	 * @param p1		Value One
	 * @param p2		Value Two
	 * @param p3		Value Three
	 * @param pos		Position inside values
	 * @return 			Weighted value
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	/**
	 * Creates view matrix of {@link Camera}
	 * 
	 * @param camera	Camera for view matrix
	 * @return 			View Matrix
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
	 * Gets distance between two points
	 * 
	 * @param pointOne	First Point
	 * @param pointTwo	Second Point
	 * @return 			Distance
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
	 * Determines furthest point from origin
	 * 
	 * @param origin	Point of origin
	 * @param points	Points to be checked
	 * @return 			Furthest point
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
	 * Determines closest point to origin
	 * 
	 * @param origin	Point of origin
	 * @param points	Points to be checked	
	 * @return 			Closest point
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
	 * Calculates the normal of the plane given by pos1, pos2, and pos3
	 * 
	 * @param pos1		Position One
	 * @param pos2		Position Two
	 * @param pos3		Position Three
	 * @return 			Normal of plane
	 */
	public static Vector3f getNormal(Vector3f pos1, Vector3f pos2, Vector3f pos3) {
		
		Vector3f normalVector = null;
		
		Vector3f vec1 = Vector3f.sub(pos2, pos1, null);
		Vector3f vec2 = Vector3f.sub(pos3, pos1, null);
		
		normalVector = new Vector3f(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
		
		
		return normalVector;
		
	}
	
	/**
	 * Calculates linear interpolation between values
	 * 
	 * @param a			First value
	 * @param b			Second value
	 * @param f			Blend value
	 * @return 			Interpolated value
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
	 * Calculates cosine interpolation between values
	 * 
	 * @param a			First value
	 * @param b			Second value
	 * @param blend		Blend value
	 * @return ``		Interpolated value
	 */
	public static float CosineInterpolation(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float)(1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}
	
	/**
	 * Checks if a value is between two bounds 
	 * 
	 * @param in		Middle value
	 * @param param1	Lower bound
	 * @param param2	Upper bound
	 * @return 			Is between upper and lower
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
	
	/**
	 * Converts a three component RGB value to its integer equivalent
	 * 
	 * @param color		0-255 float values for each component
	 * @return			Integer value of the color
	 */
	public static int rgbToInt(Vector3f color) {
		int red = (int) color.x;
		int green = (int) color.y;
		int blue = (int) color.z;
		if((red & 255) != red || (green & 255) != green || (blue & 255) != blue)
			throw new IllegalArgumentException("RGB input invalid");
		return (red << 16) | (green << 8) | blue;
	}
	
	/**
	 * Calculates the integer equivalent of a color
	 * 
	 * @param r			Integer containing R value of color
	 * @param g			Integer containing G value of color
	 * @param b			Integer containing B value of color
	 * @return 			int value of color
	 */
	public static int rgbToInt(int r, int g, int b) {
		return rgbToInt(new Vector3f(r, g, b));
	}
	
	/**
	 * Gets the minimum value of an array
	 * 
	 * @param vals		Array to calculate minimum of
	 * @return			Minimum value in array
	 */
	public static float getMinimum(float[] vals) {
		float min = vals[0];
		for(float val : vals) {
			if(val < min) min = val;
		}
		return min;
	}
	
	/**
	 * Gets the maximum value of an array
	 * 
	 * @param vals		Array to calculate maximum of
	 * @return			Maximum value in array
	 */
	public static float getMaximum(float[] vals) {
		float max = vals[0];
		for(float val : vals) {
			if(val > max) max = val;
		}
		return max;
	}

}

package tk.luminos.maths;

/**
 * Allows for OpenGL Shader Language math functionality in Java code
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class GLMath {
	
	/**
	 * Clamps floating point decimal between two values
	 * 
	 * @param x			Value to constrain
	 * @param minVal	Minimum value
	 * @param maxVal	Maximum value
	 * @return			Constrained value
	 */
	public static Number clamp(float x, float minVal, float maxVal) {
		return x < minVal ? minVal : (x > maxVal ? maxVal : x);
	}
	
	/**
	 * Clamps integer between two values
	 * 
	 * @param x			Value to constrain
	 * @param minVal	Minimum value
	 * @param maxVal	Maximum value
	 * @return			Constrained value
	 */
	public static Number clamp(int x, int minVal, int maxVal) {
		return x < minVal ? minVal : (x > maxVal ? maxVal : x);
	}
	
	/**
	 * Calculates the cross product of two vectors
	 * 
	 * @param x			First of two vectors
	 * @param y			Seconds of two vectors
	 * @return			Cross product
	 */
	public static Vector3 cross(Vector3 x, Vector3 y) {
		return Vector3.cross(x, y, null);
	}
	
	/**
	 * Calculates the determinant of a matrix
	 * 
	 * @param mat	Matrix of which to take determinant of
	 * @return		Determinant of matrix
	 */
	public static float determinant(Matrix mat) {
		return mat.determinant();
	}
	
	/**
	 * Calculates the dot product of two vectors
	 * 
	 * @param x		First of two vectors
	 * @param y		Second of two vectors
	 * @return		Dot product
	 */
	public static float dot(Vector x, Vector y) {
		return x.dot(y);
	}

}

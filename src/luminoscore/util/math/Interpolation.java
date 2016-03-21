package luminoscore.util.math;

public class Interpolation {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/17/2016
	 */
	
	/*
	 * @param a First float
	 * @param b Second float
	 * @param blend Proportion of Distance of a to b
	 * @return float
	 * 
	 * Interpolates two floats using cosine
	 */
	public static float cosineInterpolation(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + (b * f);
	}
	
	/*
	 * @param a First float
	 * @param b Second float
	 * @param blend Proportion of Distance of a to b
	 * @return float
	 * 
	 * Interpolates two floats using sine
	 */
	public static float sineInterpolation(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.sin(theta)) * 0.5f;
		return a * (1f - f) + (b * f);
	}
	
	/*
	 * @param a First float
	 * @param b Second float
	 * @param f Proportion of Distance of a to b
	 * @return float
	 * 
	 * Interpolates two floats linearly
	 */
	public static float linearInterpolation(float a, float b, float f) {
		return a * (1f - f) + (b * f);
	}

}

package luminoscore.util.math;

public class Conversions {

	/*
	 * @param radius Radial coordinate
	 * @param theta Theta coordinate
	 * @param phi Phi coordinate
	 * @return Vector3f
	 * 
	 * Calculate Cartesian coordinate from Polar Coordinate
	 */
	public static Vector3f polarToCartesian(float radius, float theta, float phi) {
		Vector3f cart = new Vector3f();
		cart.x = (float) (radius * Math.sin(Math.toRadians(theta)) * Math.cos(Math.toDegrees(phi)));
		cart.y = (float) (radius * Math.sin(Math.toRadians(theta)) * Math.sin(Math.toRadians(phi)));
		cart.z = (float) (radius * Math.cos(Math.toRadians(theta)));
		
		return cart;
	}
	
}

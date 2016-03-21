package luminoscore.util.math;

import luminoscore.util.math.vector.Vector3f;

public class Algebra {
	
	/*
	 * @param one Vector one
	 * @param two Vector two
	 */
	public static double getDistance(Vector3f one, Vector3f two) {
		double xOff = Math.pow(one.x - two.x, 2);
		double yOff = Math.pow(one.y - two.y, 2);
		double zOff = Math.pow(one.z - two.z, 2);
		return Math.sqrt(xOff + yOff + zOff);
	}

}

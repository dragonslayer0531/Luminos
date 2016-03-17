package luminoscore.util.math;

public class Conversions {

	public static Vector3f polarToCartesian(float radius, float polar, float alpha) {
		Vector3f cart = new Vector3f();
		cart.x = (float) (radius * Math.sin(Math.toRadians(polar)) * Math.cos(Math.toDegrees(alpha)));
		cart.y = (float) (radius * Math.sin(Math.toRadians(polar)) * Math.sin(Math.toRadians(alpha)));
		cart.z = (float) (radius * Math.cos(Math.toRadians(polar)));
		
		return cart;
	}
	
}

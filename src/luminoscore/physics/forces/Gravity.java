package luminoscore.physics.forces;

import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.entities.components.ComponentException;
import luminoscore.graphics.entities.components.Mass;
import luminoscore.util.math.Vector3f;

public class Gravity {
	
	//Value of Gravitational Constant
	private static final double GRAVITY = 6.7 * Math.pow(10, -11);

	/*
	 * @param effected Entity to have gravity acted upon by
	 * @param center Entity to be the center of gravity
	 * @throws ComponentException
	 * @return double
	 * 
	 * Calculates the force of gravity on the effected object
	 */
	public static double calculateGravitationalForce(Entity effected, Entity center) throws ComponentException {
		double numerator = GRAVITY * (float) effected.getComponentValue(Mass.class) * (float) center.getComponentValue(Mass.class);
		double denominator = Math.sqrt(Math.pow((effected.getPosition().x - center.getPosition().x), 2) + Math.pow(effected.getPosition().y - center.getPosition().y, 2) + Math.pow(effected.getPosition().z - center.getPosition().z, 2));
		return numerator/denominator;
	}
	
	/*
	 * @param effected Entity to have gravity acted upon by
	 * @param center Entity to be the center of gravity
	 * @returns Vector3f
	 * 
	 * Calculates the vector of the gravitational force
	 */
	public static Vector3f calculateGravitationalVector(Entity effected, Entity center) {
		try {
			double mag = calculateGravitationalForce(effected, center);
			Vector3f vec = Vector3f.sub(effected.getPosition(), center.getPosition());
			vec.normalise();
			return Vector3f.scale(vec, (float) mag);
		} catch (ComponentException e) {
			e.printStackTrace();
		}
		
		return new Vector3f();
		
	}

}

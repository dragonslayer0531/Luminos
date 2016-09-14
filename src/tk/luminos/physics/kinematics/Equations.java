package tk.luminos.physics.kinematics;

/**
 * Static equations for relation of kinematics
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Equations {
	
	/**
	 * Calculates distance in regards to initial velocity, acceleration, and time
	 * 
	 * @param init_velocity		Initial velocity
	 * @param acceleration		Acceleration
	 * @param time				Time
	 * @return					Distance traveled
	 */
	public static float distanceWithAcceleration(float init_velocity, float acceleration, float time) {
		float p1 = init_velocity * time;
		float p2 = .5f * acceleration * time * time;
		return p1 + p2;
	}
	
	/**
	 * Calculates distance in regards to initial velocity, terminal velocity, and time
	 * 
	 * @param init_velocity		Initial velocity
	 * @param term_velocity		Terminal velocity
	 * @param time				Time
	 * @return					Distance traveled
	 */
	public static float distanceWithoutAcceleration(float init_velocity, float term_velocity, float time) {
		float p1 = init_velocity + term_velocity;
		float frac = p1 / 2;
		return frac * time;
	}
	
	/**
	 * Calculates terminal velocity in regards to initial velocity, acceleration, and distance
	 * 
	 * @param init_velocity		Initial velocity
	 * @param acceleration		Acceleration
	 * @param distance			Distance
	 * @return					Terminal velocity
	 */
	public static float finalVelocityWithoutTime(float init_velocity, float acceleration, float distance) {
		float p1 = init_velocity * init_velocity + 2 * acceleration * distance;
		return (float) Math.sqrt(p1);
	}
	
	/**
	 * Calculates terminal velocity in regards to initial velocity, acceleration, and time
	 * 
	 * @param init_velocity		Initial velocity
	 * @param acceleration		Acceleration
	 * @param time				Time
	 * @return					Terminal velocity
	 */
	public static float finalVelocityWithTime(float init_velocity, float acceleration, float time) {
		return init_velocity + acceleration * time;
	}
	
	/**
	 * Calculates time in regards to distance, initial velocity, and terminal velocity
	 * 
	 * @param distance			Distance
	 * @param init_velocity		Initial velocity
	 * @param term_velocity		Terminal velocity
	 * @return					Time
	 */
	public static float timeWithoutAcceleration(float distance, float init_velocity, float term_velocity) {
		float p1 = 2 * distance;
		float p2 = init_velocity + term_velocity;
		return p1 / p2;
	}
	
	/**
	 * Calculates time in regards to initial velocity, terminal velocity, and acceleration
	 * 
	 * @param init_velocity		Initial velocity
	 * @param term_velocity		Terminal velocity
	 * @param acceleration		Acceleration
	 * @return					Time
	 */
	public static float timeWithAcceleration(float init_velocity, float term_velocity, float acceleration) {
		return (term_velocity - init_velocity) / acceleration;
	}

}

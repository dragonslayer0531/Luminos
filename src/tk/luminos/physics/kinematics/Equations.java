package tk.luminos.physics.kinematics;

public class Equations {
	
	public static float distanceWithAcceleration(float init_velocity, float acceleration, float time) {
		float p1 = init_velocity * time;
		float p2 = .5f * acceleration * time * time;
		return p1 + p2;
	}
	
	public static float distanceWithoutAcceleration(float init_velocity, float term_velocity, float time) {
		float p1 = init_velocity + term_velocity;
		float frac = p1 / 2;
		return frac * time;
	}
	
	public static float finalVelocityWithoutTime(float init_velocity, float acceleration, float distance) {
		float p1 = init_velocity * init_velocity + 2 * acceleration * distance;
		return (float) Math.sqrt(p1);
	}
	
	public static float finalVelocityWithTime(float init_velocity, float acceleration, float time) {
		return init_velocity + acceleration * time;
	}
	
	public static float timeWithoutAcceleration(float distance, float init_velocity, float term_velocity) {
		float p1 = 2 * distance;
		float p2 = init_velocity + term_velocity;
		return p1 / p2;
	}
	
	public static float timeWithAcceleration(float init_velocity, float term_velocity, float acceleration) {
		return (term_velocity - init_velocity) / acceleration;
	}

}

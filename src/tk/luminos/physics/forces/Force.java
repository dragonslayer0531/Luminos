package tk.luminos.physics.forces;

import tk.luminos.graphics.display.GLFWWindow;
import tk.luminos.graphics.gameobjects.Entity;
import tk.luminos.maths.vector.Vector3f;
import tk.luminos.physics.Body;

/**
 * Superclass used by forces
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Force {
	
	private Vector3f compositeForce;
	
	public Force(Vector3f direction, float strength) {
		direction.normalise();
		this.compositeForce = Vector3f.scale(direction, strength, null);
	}
	
	public void update(Body body, GLFWWindow window) {
		float mass = body.getMass();
		Entity entity = body.getEntity();
		Vector3f init_velocity = entity.getVelocity();
		float finalVelocity = (init_velocity.magnitude() + compositeForce.magnitude() * window.getFrameTime()) / mass;
		Vector3f comp = Vector3f.add(init_velocity, compositeForce, null);
		comp.normalise();
		comp.scale(finalVelocity * window.getFrameTime());
		entity.setVelocity(Vector3f.add(entity.getVelocity(), comp, null));
	}

}

package tk.luminos.physics;

import java.util.List;

import tk.luminos.graphics.display.GLFWWindow;
import tk.luminos.physics.forces.Force;

public class Manager {
	
	public static void update(List<Body> bodies, GLFWWindow window) {
		for(Body body : bodies){
			List<Force> forces = body.getForces();
			for(Force force : forces) {
				force.update(body, window);
			}
		}
	}

}

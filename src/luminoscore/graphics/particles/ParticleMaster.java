package luminoscore.graphics.particles;

import java.util.Iterator;
import java.util.List;

import luminoscore.graphics.display.GLFWWindow;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Static class for performing world-wide particle tasks
 *
 */

public class ParticleMaster {
	
	/**
	 * @param particles	List of particles in the world
	 * @param window	Window to get frame time of
	 * 
	 * Updates all particles in world
	 */
	public static void update(List<Particle> particles, GLFWWindow window) {
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()) {
			Particle p = iterator.next();
			if(p.update(window)) {
				iterator.remove();
			}
		}
	}

}

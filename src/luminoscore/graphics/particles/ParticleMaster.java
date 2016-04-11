package luminoscore.graphics.particles;

import java.util.ArrayList;
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
	
	private static List<Particle> particles = new ArrayList<Particle>();
	
	/**
	 * @param particle	Particle to be added
	 * 
	 * Adds particle to list
	 */
	public static void addParticle(Particle particle) {
		particles.add(particle);
	}
	
	/**
	 * @param particles	Particles to be added
	 * 
	 * Adds particles to list
	 */
	public static void addAllParticles(List<Particle> particles) {
		ParticleMaster.particles.addAll(particles);
	}
	
	/**
	 * @param particles	List of particles in the world
	 * @param window	Window to get frame time of
	 * 
	 * Updates all particles in world
	 */
	public static void update(GLFWWindow window) {
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()) {
			Particle p = iterator.next();
			if(p.update(window)) {
				iterator.remove();
			}
		}
	}

}

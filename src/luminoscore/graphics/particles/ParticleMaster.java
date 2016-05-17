package luminoscore.graphics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.textures.ParticleTexture;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Static class for performing world-wide particle tasks
 *
 */

public class ParticleMaster {
	
	public static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	
	/**
	 * @param particle	Particle to be added
	 * 
	 * Adds particle to list
	 */
	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if(list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}
	
	/**
	 * @param particles	Particles to be added
	 * 
	 * Adds particles to list
	 */
	public static void addAllParticles(List<Particle> particles) {
		for(Particle particle : particles) {
			ParticleMaster.addParticle(particle);
		}
	}
	
	/**
	 * @param particles	List of particles in the world
	 * @param window	Window to get frame time of
	 * 
	 * Updates all particles in world
	 */
	public static void update(GLFWWindow window) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = (Iterator<Entry<ParticleTexture, List<Particle>>>) particles.entrySet().iterator();
		while(mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while(iterator.hasNext()) {
				Particle p = iterator.next();
				boolean stillAlive = p.update(window);
				if(!stillAlive) {
					iterator.remove();
					if(list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			
		}
	}

}

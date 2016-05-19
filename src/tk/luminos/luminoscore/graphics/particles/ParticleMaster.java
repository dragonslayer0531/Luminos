package tk.luminos.luminoscore.graphics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tk.luminos.luminoscore.graphics.display.GLFWWindow;
import tk.luminos.luminoscore.graphics.textures.ParticleTexture;

/**
 * 
 * Static class for performing world-wide particle tasks
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ParticleMaster {
	
	public static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	
	/**
	 * Adds {@link Particle} to list
	 * 
	 * @param particle	Particle to be added
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
	 * Adds {@link Particle}s to list
	 * 
	 * @param particles	Particles to be added
	 */
	public static void addAllParticles(List<Particle> particles) {
		for(Particle particle : particles) {
			ParticleMaster.addParticle(particle);
		}
	}
	
	/**
	 * Updates all particles in world
	 * 
	 * @param window	{@link GLFWWindow} to get frame time of
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

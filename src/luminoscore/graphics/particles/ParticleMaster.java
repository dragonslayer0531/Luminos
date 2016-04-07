package luminoscore.graphics.particles;

import java.util.Iterator;
import java.util.List;

import luminoscore.graphics.display.GLFWWindow;

public class ParticleMaster {
	
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

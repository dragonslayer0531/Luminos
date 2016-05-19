package tk.luminos.luminoscore.physics;

import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.display.GLFWWindow;

/**
 * Interface used by forces
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface Force {
	
	/**
	 * Calculate method for forces
	 * 
	 * @param window		{@link GLFWWindow} to calculate frametime with
	 * @return				Updated entity position
	 */
	public Vector3f calculate(GLFWWindow window);

}

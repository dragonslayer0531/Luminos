package tk.luminos.graphics.gameobjects;

import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Light interface
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface Light extends SceneObject {
	
	/**
	 * Gets the color of the light
	 * 
	 * @return		Light color
	 */
	public Vector3f getColor();

}

class Attenuation {
	
	public float constant = 0;
	public float linear = 0;
	public float exponent = 0;
	
	public Attenuation() {
		
	}
	
	public Vector3f getAttenuation() {
		return new Vector3f(exponent, linear, constant);
	}
	
}
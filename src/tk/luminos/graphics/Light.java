package tk.luminos.graphics;

import tk.luminos.maths.Vector3;

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
	public Vector3 getColor();

}

class Attenuation {
	
	float constant = 0;
	float linear = 0;
	float exponent = 0;
	
	/**
	 * Creates default attenuation object
	 */
	public Attenuation() {
		this(0, 0, 0);
	}
	
	/**
	 * Creates attentuation object
	 * 
	 * @param constant		constant attenuation
	 * @param linear		linear attenuation
	 * @param exponent		exponential attenuation
	 */
	public Attenuation(float constant, float linear, float exponent)
	{
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
	}
	
	/**
	 * Gets the attenuation
	 * 
	 * @return		attenuation
	 */
	public Vector3 getAttenuation() {
		return new Vector3(exponent, linear, constant);
	}
	
}
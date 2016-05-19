package tk.luminos.luminoscore.graphics.gameobjects;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * The light is a wrapper for position, color, and attenuation
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class Light {
	
	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	/**
	 * Constructor using position and color
	 * 
	 * @param position		Initial position of the light
	 * @param color			Initial color of the light
	 */
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	/**
	 * Constructor using position, color, and attenuation
	 * 
	 * @param position		Initial position of the light
	 * @param color			Initial color of the light
	 * @param attenuation	Initial attenuation of the light
	 */
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	/**
	 * Gets the attenuation of the light instance
	 * 
	 * @return Attenuation of light 
	 */
	public Vector3f getAttenuation(){
		return attenuation;
	}

	/**
	 * Gets the position of the light instance
	 * 
	 * @return Position of light
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Gets the color of the light instance
	 * 
	 * @return Color of light
	 */
	public Vector3f getColor() {
		return color;
	}

}

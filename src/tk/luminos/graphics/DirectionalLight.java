package tk.luminos.graphics;

import tk.luminos.maths.Vector3;

/**
 * 
 * Directional light for scene lighting.  Usually only
 * one is implemented into the shaders, as there is only
 * one sun.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class DirectionalLight implements Light {
	
	private Vector3 color;
	private Vector3 direction;
	private float intensity;
	private String id;

	/**
	 * Creates directional light
	 * 
	 * @param color			Color of light
	 * @param direction		Direction of light
	 * @param intensity		Intensity of light
	 */
	public DirectionalLight(Vector3 color, Vector3 direction, float intensity) {
		this.color = color;
		this.direction = direction;
		this.intensity = intensity;
	}
	
	/**
	 * Sets the direction of the light
	 * 
	 * @param direction		Direction of light
	 */
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}
	
	/**
	 * Sets the color of the light
	 * 
	 * @param color		Color of light
	 */
	public void setColor(Vector3 color) {
		this.color = color;
	}
	
	/**
	 * Sets the intensity of the light
	 * 
	 * @param intensity		Intensity of light
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	/**
	 * Gets the color of the light
	 * 
	 * @return 	Light color
	 */
	@Override
	public Vector3 getColor() {
		return color;
	}
	
	/**
	 * Gets the direction of the light
	 * 
	 * @return		Light direction
	 */
	public Vector3 getDirection() {
		return direction;
	}
	
	/**
	 * Gets the intensity of the light
	 * 
	 * @return		Light intensity
	 */
	public float getIntensity() {
		return intensity;
	}
	
	/**
	 * Sets the ID of the light
	 * 
	 * @param id	ID of light
	 */
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the ID of the light
	 * 
	 * @return		Light ID
	 */
	@Override
	public String getID() {
		return id;
	}

}

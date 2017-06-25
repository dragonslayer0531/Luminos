package tk.luminos.graphics;

import tk.luminos.maths.Vector;
import tk.luminos.maths.Vector3;

/**
 * 
 * The light is a wrapper for position, color, and attenuation
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class PointLight implements Light {
	
	private Vector3 position;
	private Vector3 color;
	private Attenuation attenuation;
	private String id;
	
	/**
	 * Constructor using position and color
	 * 
	 * @param position		Initial position of the light
	 * @param color			Initial color of the light
	 */
	public PointLight(Vector3 position, Vector3 color) {
		this.position = position;
		this.color = color;
		this.attenuation = new Attenuation();
		this.attenuation.exponent = 1;
	}
	
	/**
	 * Constructor using position, color, and attenuation
	 * 
	 * @param position		Initial position of the light
	 * @param color			Initial color of the light
	 * @param attenuation	Initial attenuation of the light
	 */
	public PointLight(Vector3 position, Vector3 color, Attenuation attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.attenuation.exponent = 1;
	}
	
	/**
	 * Increases the position of the GameObject by delta
	 * 
	 * @param delta		amount to increase
	 */
	public void increasePosition(Vector delta) {
		Vector3 d = (Vector3) delta;
		position.x += d.x;
		position.y += d.y;
		position.z += d.z;
	}
	
	/**
	 * Gets the attenuation of the light instance
	 * 
	 * @return Attenuation of light 
	 */
	public Vector3 getAttenuation(){
		return attenuation.getAttenuation();
	}

	/**
	 * Gets the position of the light instance
	 * 
	 * @return Position of light
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Gets the color of the light instance
	 * 
	 * @return Color of light
	 */
	public Vector3 getColor() {
		return color;
	}
	
	/**
	 * Sets the ID of the light
	 * 
	 * @param id		ID of light
	 */
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the ID of the light
	 * 
	 * @return		ID of light
	 */
	@Override
	public String getID() {
		return id;
	}

}

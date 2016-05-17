package luminoscore.graphics.entities;

import org.lwjgl.util.vector.Vector3f;

import luminosutils.serialization.LArray;
import luminosutils.serialization.LObject;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * The light is a wrapper for position, color, and attenuation
 *
 */
public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	/**
	 * @param position
	 * @param colour
	 * 
	 * Constructor using position and color
	 */
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	/**
	 * @param position
	 * @param colour
	 * @param attenuation
	 * 
	 * Constructor using position, color, and attenuation
	 */
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	/**
	 * @return Vector3f		Attenuation of light
	 * 
	 * Gets the attenuation of the light instance
	 */
	public Vector3f getAttenuation(){
		return attenuation;
	}

	/**
	 * @return Vector3f		Position of light
	 * 
	 * Gets the position of the light instance
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return Vector3f 	Color of light
	 * 
	 * Gets the color of the light instance
	 */
	public Vector3f getColour() {
		return colour;
	}
	
	/**
	 * Gets byte array associated with light
	 * 
	 * @return byte array associated with light
	 */
	public byte[] getBytes() {
		LObject object = new LObject("light");
		object.addArray(LArray.Float("position", new float[]{position.x, position.y, position.z}));
		object.addArray(LArray.Float("attenuation", new float[]{attenuation.x, attenuation.y, attenuation.z}));
		object.addArray(LArray.Float("color", new float[]{colour.x, colour.y, colour.z}));
		byte[] data = new byte[object.getSize()];
		object.getBytes(data, 0);
		return data;
	}

}

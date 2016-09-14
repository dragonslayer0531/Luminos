package tk.luminos.graphics.opengl.gameobjects;

import tk.luminos.filesystem.serialization.LArray;
import tk.luminos.filesystem.serialization.LDatabase;
import tk.luminos.filesystem.serialization.LObject;
import tk.luminos.maths.vector.Vector;
import tk.luminos.maths.vector.Vector3f;

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
	 * Increases the position of the GameObject by delta
	 * 
	 * @param delta		amount to increase
	 */
	public void increasePosition(Vector delta) {
		Vector3f d = (Vector3f) delta;
		position.x += d.x;
		position.y += d.y;
		position.z += d.z;
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
	
	/**
	 * Gets the light as a luminos object
	 * 
	 * @return	Gets the luminos object
	 */
	public LObject getLuminosObject() {
		LObject object = new LObject("light");
		object.addArray(LArray.Float("pos", new float[]{position.x, position.y, position.z}));
		object.addArray(LArray.Float("col", new float[]{color.x, color.y, color.z}));
		object.addArray(LArray.Float("att", new float[]{attenuation.x, attenuation.y, attenuation.z}));
		return object;
	}
	
	/**
	 * Gets bytes of the light
	 *  
	 * @return	bytes of the light
	 */
	public byte[] getBytes() {
		LObject object = getLuminosObject();
		byte[] data = new byte[object.getSize()];
		object.getBytes(data, 0);
		return data;
	}
	
	/**
	 * Attaches the light to a Luminos Database
	 * 
	 * @param database		Database to be added to
	 */
	public void attachToLuminosDatabase(LDatabase database) {
		database.addObject(getLuminosObject());
	}

}

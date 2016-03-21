package luminoscore.graphics.entities;

import luminoscore.util.math.vector.Vector3f;

public class Light {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Constructor Fields
	private Vector3f position, color, attenuation = new Vector3f(1, 0, 0);
	
	/*
	 * @param position Defines position of the light
	 * @param color Defines the color of the light
	 * 
	 * Constructor One
	 */
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	/*
	 * @param position Defines the position of the light
	 * @param color Defines the color of the light
	 * @param attenuation Defines the color of the light
	 * 
	 * Constructor Two
	 */
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	//Getter-Setter Methods
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}

}

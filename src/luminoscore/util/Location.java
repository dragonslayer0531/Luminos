package luminoscore.util;

import luminoscore.util.math.Vector2f;

public class Location {
	
	//Constructor data fields
	private float theta, phi;

	/*
	 * @param polarAngles Defines angles
	 * 
	 * Constructor
	 */
	public Location(Vector2f polarAngles) {
		this.theta = polarAngles.x;
		this.phi = polarAngles.y;
	}

	//Getter-Setter Methods
	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = theta;
	}

	public float getPhi() {
		return phi;
	}

	public void setPhi(float phi) {
		this.phi = phi;
	}

}

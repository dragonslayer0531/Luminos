package tk.luminos.graphics;

import tk.luminos.maths.Vector3;

/**
 * 
 * Creates Spot Light for lighting the scene
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class SpotLight implements Light {

	private PointLight pointLight;
	private Vector3 direction;
	private float angle;
	
	/**
	 * Creates spot light
	 * 
	 * @param pointLight	Point light to be base of lighting
	 * @param direction		Direction the spot light should face
	 * @param angle			How large the code of the light is
	 */
	public SpotLight(PointLight pointLight, Vector3 direction, float angle) {
		this.pointLight = pointLight;
		this.direction = direction;
		this.angle = angle;
	}

	/**
	 * Gets the base point light
	 * 
	 * @return		Point light of spot light
	 */
	public PointLight getPointLight() {
		return pointLight;
	}

	/**
	 * Sets the base point light
	 * 
	 * @param pointLight		Base point light
	 */
	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}

	/**
	 * Gets direction of light
	 * 
	 * @return	direction of light
	 */
	public Vector3 getDirection() {
		return direction;
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
	 * Gets the angle of the light
	 * 
	 * @return		angle of light cone
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Sets the angle of the cone
	 * 
	 * @param angle		Angle of cone
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	private String id;
		
	/**
	 *	Gets the ID of the light
	 *	
	 *	@return 		ID of light
	 */
	@Override
	public String getID() {
		return id;
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
	 * Gets the color of the light
	 * 
	 * @return	Color of light
	 */
	@Override
	public Vector3 getColor() {
		return pointLight.getColor();
	}	
	
}

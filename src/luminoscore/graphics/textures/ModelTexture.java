package luminoscore.graphics.textures;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Texture Used in Model
 *
 */

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	/**
	 * @param texture	GPU ID
	 * 
	 * Constructor
	 */
	public ModelTexture(int texture){
		this.textureID = texture;
	}
		
	/**
	 * @return int	Row count
	 * 
	 * Get row count
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @param numberOfRows	Number of rows
	 * 
	 * Set number of rows
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	/**
	 * @return boolean	Has transparency
	 * 
	 * Get if uses transparency
	 */
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	/**
	 * @return boolean Uses fake lighting
	 * 
	 * Gets if uses fake lighting
	 */
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * @param useFakeLighting	Use fake lighting
	 * 
	 * Sets fake lighting usage
	 */
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	/**
	 * @param hasTransparency	Transparent
	 * 
	 * Sets transparency usage
	 */
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * @return int	VAO ID
	 * 
	 * Gets VAO ID
	 */
	public int getID(){
		return textureID;
	}

	/**
	 * @return float	Shine Damper
	 * 
	 * Gets the power of the shine damper
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * @param shineDamper	Shine Damper
	 * 
	 * Sets the power of the shine damper
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * @return reflectivity	Power of reflection
	 * 
	 * Gets the power of reflection
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * @param reflectivity	Power of reflection
	 * 
	 * Sets the power of reflection
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}

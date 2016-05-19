package tk.luminos.luminoscore.graphics.textures;

/**
 * 
 * Texture Used in Model
 * 
 * @author Nick Clark
 * @version 1.0
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
	 * Constructor
	 * 
	 * @param texture	GPU ID
	 */
	public ModelTexture(int texture){
		this.textureID = texture;
	}
		
	/**
	 * Get row count
	 * 
	 * @return	Row count
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * Set number of rows
	 * 
	 * @param numberOfRows	Number of rows
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	/**
	 * Get if uses transparency
	 * 
	 * @return	Has transparency
	 */
	public boolean hasTransparency() {
		return hasTransparency;
	}

	/**
	 * Gets if uses fake lighting
	 * 
	 * @return Uses fake lighting
	 */
	public boolean usesFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * Sets fake lighting usage
	 * 
	 * @param useFakeLighting	Use fake lighting
	 */
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	/**
	 * Sets transparency usage
	 * 
	 * @param hasTransparency	Transparent
	 */
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * Gets VAO ID
	 * 
	 * @return VAO ID
	 */
	public int getID(){
		return textureID;
	}

	/**
	 * Gets the power of the shine damper
	 * 
	 * @return Shine Damper
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * Sets the power of the shine damper
	 * 
	 * @param shineDamper	Shine Damper
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * Gets the power of reflection
	 * 
	 * @return reflectivity	Power of reflection
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * Sets the power of reflection
	 * 
	 * @param reflectivity	Power of reflection
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}

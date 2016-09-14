package tk.luminos.graphics.opengl.textures;

import tk.luminos.graphics.opengl.AssetHolder;
import tk.luminos.graphics.opengl.loaders.Loader;

/**
 * 
 * Texture Used in Model
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ModelTexture implements Texture {
	
	private int textureID;
	private int normalID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasNormal = false;
	private boolean useDoubleSided = false;
	
	private int numberOfRows = 1;
	
	/**
	 * Constructor
	 * 
	 * @param texture		Location of image to load
	 * @param loader		Loader to use to load image
	 */
	public ModelTexture(String texture, Loader loader){
		this.textureID = loader.loadTexture(texture);
		AssetHolder.add(textureID, texture);
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
	
	/**
	 * Gets the normal map's ID
	 * 
	 * @return	normal map's ID
	 */
	public int getNormal() {
		return normalID;
	}
	
	/**
	 * Sets the normal map's ID
	 * 
	 * @param normal		Location of normal map
	 * @param loader		Loader to load the texture
	 */
	public void setNormal(String normal, Loader loader) {
		this.normalID = loader.loadTexture(normal);
		AssetHolder.add(normalID, normal);
	}
	
	/**
	 * Evaluates if the model texture has a normal map associated with it
	 * 
	 * @return	if the model texture has an associated normal map
	 */
	public boolean hasNormal() {
		return hasNormal;
	}
	
	/**
	 * Evaluates if the model texture is to be mapped on both sides
	 * 
	 * @return	if the model texture is mapped to both sides
	 */
	public boolean isDoubleSided() {
		return useDoubleSided;
	}
	
	/**
	 * Sets if the model texture is to be mapped on both sides
	 * 
	 * @param isDoubleSided		mapping value
	 */
	public void setDoubleSided(boolean isDoubleSided) {
		this.useDoubleSided = isDoubleSided;
	}

}

package tk.luminos.graphics;

import tk.luminos.maths.Vector4;

/**
 * Creates new material
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Material {
	
	private int diffuse;
	private int specular;
	private int normal;
	private int bump;
	
	private Vector4 color = new Vector4(0, 0, 0, 1);
	
	private int useDiffuse = 0;
	private int useSpecular = 0;
	private int useNormal = 0;
	private int useBump = 0;
	
	private int rows = 1;
	private int transparency = 0;
	private int fakeLighting = 0;
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private int twoSided = 0;
	
	/**
	 * Attaches diffuse texture
	 * 
	 * @param id		texture id
	 */
	public void attachDiffuse(int id) {
		this.diffuse = id;
		useDiffuse = 1;
	}
	
	/**
	 * Attaches specular texture
	 * 
	 * @param id		texture id
	 */
	public void attachSpecular(int id) {
		this.specular = id;
		useSpecular = 1;
	}
	
	/**
	 * Attaches normal texture
	 * 
	 * @param id		texture id
	 */
	public void attachNormal(int id) {
		this.normal = id;
		useNormal = 1;
	}
	
	/**
	 * Attaches bump texture
	 * 
	 * @param id		texture id
	 */
	public void attachBump(int id) {
		this.bump = id;
		useBump = 1;
	}
	
	/**
	 * Sets render double sided
	 * 
	 * @param doubleSided		if double sided
	 */
	public void setRenderDoubleSided(int doubleSided) {
		this.twoSided = doubleSided;
	}
	
	/**
	 * Sets color
	 * 
	 * @param color		new color
	 */
	public void setColor(Vector4 color) {
		this.color = color;
	}
	
	/**
	 * Sets transparency
	 * 
	 * @param transparent		if transparent
	 */
	public void setTransparency(int transparent) {
		this.transparency = transparent;
	}
	
	/**
	 * Gets diffuse id
	 * 
	 * @return id
	 */
	public int getDiffuseID() {
		return diffuse;
	}
	
	/**
	 * Gets specular id
	 * 
	 * @return id
	 */
	public int getSpecularID() {
		return specular;
	}
	
	/**
	 * Gets normal id
	 * 
	 * @return id
	 */
	public int getNormalID() {
		return normal;
	}
	
	/**
	 * Gets bump id
	 * 
	 * @return id
	 */
	public int getBumpID() {
		return bump;
	}
	
	/**
	 * Gets default color
	 * 
	 * @return color
	 */
	public Vector4 getColor() {
		return color;
	}	
	
	/**
	 * Get has diffuse
	 * 
	 * @return	has diffuse
	 */
	public boolean hasDiffuse() {
		return useDiffuse == 1;
	}
	
	/**
	 * Get has specular
	 * 
	 * @return	has specular
	 */
	public boolean hasSpecular() {
		return useSpecular == 1;
	}
	
	/**
	 * Get has normal
	 * 
	 * @return	has normal
	 */
	public boolean hasNormal() {
		return useNormal == 1;
	}
	
	/**
	 * Get has bump
	 * 
	 * @return	has bump
	 */
	public boolean hasBump() {
		return useBump == 1;
	}
	
	/**
	 * Renders double sided
	 * 
	 * @return	is double sided
	 */
	public boolean isRenderDoubleSided() {
		return twoSided == 1;
	}
	
	/**
	 * Gets if there is transparency
	 * 
	 * @return	has transparency
	 */
	public boolean hasTransparency() {
		return transparency == 1;
	}
	
	/**
	 * Gets row count
	 * 
	 * @return	row count
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Gets if uses fake lighting
	 * 
	 * @return	fake lighting
	 */
	public boolean useFakeLighting() {
		return fakeLighting == 1;
	}
	
	/**
	 * Gets shine damper
	 * 
	 * @return damper
	 */
	public float getShineDamper() {
		return shineDamper;
	}
	
	/**
	 * Gets reflectivity
	 * 
	 * @return	reflectivity
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * Sets fake lighting
	 * 
	 * @param fakeLighting	use fake lighting
	 */
	public void setFakeLighting(int fakeLighting) {
		this.fakeLighting = fakeLighting;		
	}

}

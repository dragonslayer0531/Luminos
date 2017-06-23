package tk.luminos.graphics;

import tk.luminos.maths.Vector4;

/**
 * Creates new material
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Material {
	
	private int normal;
	private int bump;
	private Texture texture;
	
	private Vector4 diffuse = new Vector4(1, 1, 1, 1);
	private Vector4 specular = new Vector4(1, 1, 1, 1);
	private Vector4 ambient = new Vector4(1, 1, 1, 1);
	
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
	public void attachTexture(Texture texture) {
		this.texture = texture;
	}
	
	/**
	 * Attaches specular texture
	 * 
	 * @param id		texture id
	 */
	public void attachSpecular(Vector4 specular) {
		this.specular = specular;
	}
	
	/**
	 * Attaches normal texture
	 * 
	 * @param id		texture id
	 */
	public void attachNormal(int normal) {
		this.normal = normal;
	}
	
	/**
	 * Attaches bump texture
	 * 
	 * @param id		texture id
	 */
	public void attachBump(int id) {
		this.bump = id;
	}
	
	/**
	 * Sets render double sided
	 * 
	 * @param doubleSided		if double sided
	 * @return					reference to itself
	 */
	public Material setRenderDoubleSided(int doubleSided) {
		this.twoSided = doubleSided;
		return this;
	}
	
	/**
	 * Sets transparency
	 * 
	 * @param transparent		if transparent
	 * @return					reference to itself
	 */
	public Material setTransparency(int transparent) {
		this.transparency = transparent;
		return this;
	}
	
	/**
	 * Gets texture id
	 * 
	 * @return		texture
	 */
	public Texture getTexture() {
		return texture;
	}
	
	/**
	 * Sets diffuse color
	 * 
	 * @param diffuse 		diffuse color
	 */
	public void attachDiffuse(Vector4 diffuse) {
		this.diffuse = diffuse;
	}
	
	/**
	 * Gets diffuse id
	 * 
	 * @return id
	 */
	public Vector4 getDiffuse() {
		return diffuse;
	}
	
	/**
	 * Gets specular id
	 * 
	 * @return id
	 */
	public Vector4 getSpecular() {
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
	 * @return				reference to itself
	 */
	public Material setFakeLighting(int fakeLighting) {
		this.fakeLighting = fakeLighting;
		return this;
	}
	
	/**
	 * Checks to see if material ids are equivalent 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Material)) 
			return false;
		Material other = (Material) obj;
		return other.getDiffuse() == this.getDiffuse() && other.getSpecular() == this.getSpecular() && other.getBumpID() == this.getBumpID() && other.getNormalID() == this.getNormalID();
	}

	/**
	 * Gets the ambient color
	 * 
	 * @return		ambient color
	 */
	public Vector4 getAmbient() {
		return ambient;
	}

	/**
	 * Sets the ambient color
	 * 
	 * @param ambient		ambient color
	 */
	public void attachAmbient(Vector4 ambient) {
		this.ambient = ambient;
	}

}

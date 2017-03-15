package tk.luminos.graphics;

import tk.luminos.maths.vector.Vector4f;

public class Material {
	
	private int diffuse;
	private int specular;
	private int normal;
	private int bump;
	
	private Vector4f color = new Vector4f(0, 0, 0, 1);
	
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
	
	public void attachDiffuse(int id) {
		this.diffuse = id;
		useDiffuse = 1;
	}
	
	public void attachSpecular(int id) {
		this.specular = id;
		useSpecular = 1;
	}
	
	public void attachNormal(int id) {
		this.normal = id;
		useNormal = 1;
	}
	
	public void attachBump(int id) {
		this.bump = id;
		useBump = 1;
	}
	
	public void setRenderDoubleSided(int doubleSided) {
		this.twoSided = doubleSided;
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public void setTransparency(int transparent) {
		this.transparency = transparent;
	}
	
	public int getDiffuseID() {
		return diffuse;
	}
	
	public int getSpecularID() {
		return specular;
	}
	
	public int getNormalID() {
		return normal;
	}
	
	public int getBumpID() {
		return bump;
	}
	
	public Vector4f getColor() {
		return color;
	}	
	
	public boolean hasDiffuse() {
		return useDiffuse == 1;
	}
	
	public boolean hasSpecular() {
		return useSpecular == 1;
	}
	
	public boolean hasNormal() {
		return useNormal == 1;
	}
	
	public boolean hasBump() {
		return useBump == 1;
	}
	
	public boolean isRenderDoubleSided() {
		return twoSided == 1;
	}
	
	public boolean hasTransparency() {
		return transparency == 1;
	}
	
	public int getRows() {
		return rows;
	}
	
	public boolean useFakeLighting() {
		return fakeLighting == 1;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}

	public void setFakeLighting(int fakeLighting) {
		this.fakeLighting = fakeLighting;		
	}

}

package tk.luminos.graphics.models;

import tk.luminos.graphics.Material;

/**
 * 
 * Wraps raw model and model texture
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TexturedModel {
	
	private RawModel rawModel;
	private Material material;

	/**
	 * Constructor wrapping models and textures
	 * 
	 * @param model		{@link RawModel} describing the vertices
	 * @param material	{@link Material} describing the texture
	 */
	public TexturedModel(RawModel model, Material material){
		this.rawModel = model;
		this.material = material;
	}

	/**
	 * Gets the instance's {@link RawModel}
	 * 
	 * @return Model of the TexturedModel
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * Gets the TexturedModel's Material
	 * 
	 * @return Material for the model to use
	 */
	public Material getMaterial() {
		return material;
	}
	
	/**
	 * Checks if TexturedModels have the same material and raw model
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof TexturedModel)) 
			return false;
		TexturedModel other = (TexturedModel) obj;
		return other.getRawModel().equals(this.getRawModel()) && other.getMaterial().equals(this.getMaterial());
	}

}

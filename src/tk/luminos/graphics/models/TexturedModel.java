package tk.luminos.graphics.models;

import tk.luminos.graphics.Material;
import tk.luminos.graphics.VertexArray;

/**
 * 
 * Wraps raw model and model texture
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TexturedModel {
	
	private VertexArray rawModel;
	private Material material;
	private ModelData md;

	/**
	 * Constructor wrapping models and textures
	 * 
	 * @param model		{@link VertexArray} describing the vertices
	 * @param material	{@link Material} describing the texture
	 */
	public TexturedModel(VertexArray model, Material material) {
		this.rawModel = model;
		this.material = material;
	}
	
	public TexturedModel(VertexArray model, Material material, ModelData md) {
		this(model, material);
		this.md = md;
	}

	/**
	 * Gets the instance's {@link RawModel}
	 * 
	 * @return Model of the TexturedModel
	 */
	public VertexArray getVertexArray() {
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
		return other.getVertexArray().equals(this.getVertexArray()) && other.getMaterial().equals(this.getMaterial());
	}
	
	public ModelData getModelData() {
		return md;
	}

}

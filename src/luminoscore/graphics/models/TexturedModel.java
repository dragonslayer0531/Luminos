package luminoscore.graphics.models;

import luminoscore.graphics.textures.ModelTexture;

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
	private ModelTexture texture;

	/**
	 * Constructor wrapping models and textures
	 * 
	 * @param model		{@link RawModel} describing the vertices
	 * @param texture	{@link ModelTexture} describing the texture
	 */
	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
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
	 * Gets the TexturedModel's ModelTexture
	 * 
	 * @return Texture for the model to use
	 */
	public ModelTexture getTexture() {
		return texture;
	}

}

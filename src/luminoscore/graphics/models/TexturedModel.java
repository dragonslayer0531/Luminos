package luminoscore.graphics.models;

import luminoscore.graphics.textures.ModelTexture;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Wraps raw model and model texture
 *
 */

public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;

	/**
	 * @param model		RawModel describing the vertices
	 * @param texture	ModelTexture describing the texture
	 * 
	 * Constructor wrapping models and textures
	 */
	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}

	/**
	 * @return RawModel		Model of the TexturedModel
	 * 
	 * Gets the TexturedModel's RawModel
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * @return ModelTexture	Texture for the model to use
	 * 
	 * Gets the TexturedModel's ModelTexture
	 */
	public ModelTexture getTexture() {
		return texture;
	}

}

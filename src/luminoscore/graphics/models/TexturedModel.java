package luminoscore.graphics.models;

import luminoscore.graphics.textures.ModelTexture;

public class TexturedModel {

	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Constructor Fields
	private RawModel rm;
	private ModelTexture mt;
	
	//Constructor
	public TexturedModel(RawModel rm, ModelTexture mt) {
		this.rm = rm;
		this.mt = mt;
	}
	
	//Getter Methods
	public ModelTexture getModelTexture() {
		return mt;
	}
	
	public RawModel getRawModel() {
		return rm;
	}

}

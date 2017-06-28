package tk.luminos.gameobjects;

import tk.luminos.graphics.models.TexturedModel;

/**
 * Protected model
 * 
 * @author Nick Clark
 * @version 1.0
 */
class Model extends Component<TexturedModel> {
	
	/**
	 * Wraps textured model into a model
	 * 
	 * @param model		model
	 */
	Model(TexturedModel model) {
		super(model);
	}
	
}

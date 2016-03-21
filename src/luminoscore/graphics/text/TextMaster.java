package luminoscore.graphics.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luminoscore.graphics.loaders.VAOLoader;

public class TextMaster {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */
	
	//Constructor Data
	private VAOLoader loader;
	public Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	
	public TextMaster(VAOLoader loader) {
		this.loader = loader;
	}
	
	public void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

}

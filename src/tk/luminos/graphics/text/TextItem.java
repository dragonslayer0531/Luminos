package tk.luminos.graphics.text;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.Texture;
import tk.luminos.graphics.VertexArray;
import tk.luminos.loaders.Loader;

public class TextItem {

	private static final float ZPOS = 0.0f;
	private static final int VERTICES_PER_QUAD = 4;
	private String text;
	private final int numCols;
	private final int numRows;

	private VertexArray vao;
	private Texture texture;

	public TextItem(String text, String fontName, int numCols, int numRows) throws Exception {
		this.text = text;
		this.numCols = numCols;
		this.numRows = numRows;
		this.texture = new Texture(fontName);
		this.vao = build(texture, numCols, numRows);
	}

	private VertexArray build(Texture texture, int numCols2, int numRows2) {
		byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
		int numChars = chars.length;
		List<Float> positions = new ArrayList<Float>();
		List<Float> textures = new ArrayList<Float>();
		List<Integer> indices = new ArrayList<Integer>();

		float tileWidth = (float) texture.getWidth() / (float) numCols;
		float tileHeight = (float) texture.getHeight() / (float) numRows;

		for(int i=0; i<numChars; i++) {
			byte currChar = chars[i];
			int col = currChar % numCols;
			int row = currChar / numCols;

			// Build a character tile composed by two triangles

			// Left Top vertex
			positions.add((float)i*tileWidth); // x
			positions.add(0.0f); //y
			positions.add(ZPOS); //z
			textures.add((float)col / (float)numCols );
			textures.add((float)row / (float)numRows );
			indices.add(i*VERTICES_PER_QUAD);

			// Left Bottom vertex
			positions.add((float)i*tileWidth); // x
			positions.add(tileHeight); //y
			positions.add(ZPOS); //z
			textures.add((float)col / (float)numCols );
			textures.add((float)(row + 1) / (float)numRows );
			indices.add(i*VERTICES_PER_QUAD + 1);

			// Right Bottom vertex
			positions.add((float)i*tileWidth + tileWidth); // x
			positions.add(tileHeight); //y
			positions.add(ZPOS); //z
			textures.add((float)(col + 1)/ (float)numCols );
			textures.add((float)(row + 1) / (float)numRows );
			indices.add(i*VERTICES_PER_QUAD + 2);

			// Right Top vertex
			positions.add((float)i*tileWidth + tileWidth); // x
			positions.add(0.0f); //y
			positions.add(ZPOS); //z
			textures.add((float)(col + 1)/ (float)numCols );
			textures.add((float)row / (float)numRows );
			indices.add(i*VERTICES_PER_QUAD + 3);

			// Add indices for left top and bottom right vertices
			indices.add(i*VERTICES_PER_QUAD);
			indices.add(i*VERTICES_PER_QUAD + 2);
		}
		
		float[] pos = new float[positions.size()];
		for (int i = 0; i < pos.length; i++)
			pos[i] = positions.get(i);
		
		float[] tex = new float[textures.size()];
		for (int i = 0; i < tex.length; i++)
			tex[i] = textures.get(i);
		
		int[] ind = new int[indices.size()];
		for (int i = 0; i < ind.length; i++)
			ind[i] = indices.get(i);
		
		return Loader.getInstance().load(pos, tex, ind);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		vao = build(texture, numCols, numRows);
	}

}

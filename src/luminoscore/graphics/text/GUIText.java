package luminoscore.graphics.text;

import luminoscore.util.math.vector.Vector2f;
import luminoscore.util.math.vector.Vector3f;

public class GUIText {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */
	
	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f color = new Vector3f(0f, 0f, 0f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;

	/*
	 * @param text Text to be rendered
	 * @param fontSize Sets size of font
	 * @param font Font to be used
	 * @param position Sets where to render on screen
	 * @param maxLineLength defines the maximum length of the line as a ratio of the screen size
	 * @param centered Is text centered
	 * 
	 * Constructor
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
	}

	//Remove text from screen
	public void remove() {
		// remove text
	}

	//Getter-Setter Methods
	public FontType getFont() {
		return font;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getColor() {
		return color;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public Vector2f getPosition() {
		return position;
	}

	public int getMesh() {
		return textMeshVao;
	}

	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	public float getFontSize() {
		return fontSize;
	}

	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	protected boolean isCentered() {
		return centerText;
	}

	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	protected String getTextString() {
		return textString;
	}

}

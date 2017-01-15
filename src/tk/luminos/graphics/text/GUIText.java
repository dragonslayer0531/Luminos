package tk.luminos.graphics.text;

import tk.luminos.tools.maths.vector.Vector2f;
import tk.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * GUI Text
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUIText {
	
	private String textString;
    private float fontSize;
 
    private int textMeshVao;
    private int vertexCount;
    private Vector3f colour = new Vector3f(1f, 1f, 1f);
 
    private Vector2f position;
    private float lineMaxSize;
    private int numberOfLines;
 
    private FontType font;
 
    public boolean centerText = false;
 
    /**
     * Constructor
     * 
     * @param text				String defining rendered text
     * @param fontSize			Float of font size (1 Default)
     * @param font				Font to be used
     * @param position			Position of text in screen space
     * @param maxLineLength		Maximum length of line (Screen Space)
     * @param centered			Centered on screen
     */
    public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
    }

	/**
	 * Gets {@link FontType} of GUIText
	 * 
     * @return Font used to render GUI Text instance
     */
    public FontType getFont() {
        return font;
    }
 
    /**
     * Sets color of GUI Text
     * 
     * @param r	R value
     * @param g	G value
     * @param b B value
     */
    public void setColor(float r, float g, float b) {
        colour.x = r;
        colour.y = g;
        colour.z = b;
    }
 
    /**
     * Gets color of text
     * 
     * @return Text color
     */
    public Vector3f getColor() {
        return colour;
    }
 
    /**
     * Gets number of lines of text
     * 
     * @return Number lines of text
     */
    public int getNumberOfLines() {
        return numberOfLines;
    }
 
    /**
     * Gets screen position
     * 
     * @return Screen position (top left corner)
     */
    public Vector2f getPosition() {
        return position;
    }
 
    /**
     * Gets mesh's GPU ID
     * 
     * @return GPU ID of mesh 
     */
    public int getMesh() {
        return textMeshVao;
    }
 
    /**
     * Sets mesh data
     * 
     * @param vao			VAO ID of Mesh
     * @param verticesCount	Vertices of mesh
     */
    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }
 
    /**
     * Gets vertex count
     * 
     * @return Vertex Count
     */
    public int getVertexCount() {
        return this.vertexCount;
    }
 
    /**
     * Gets font size
     * 
     * @return Font Size
     */
    public float getFontSize() {
        return fontSize;
    }
 
    /**
     * Sets number of lines in mesh
     * 
     * @param number	Number of line in mesh
     */
    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }
 
    /**
     * Gets if the mesh is centered
     * 
     * @return Text center alignment
     */
    protected boolean isCentered() {
        return centerText;
    }
 
    /**
     * Gets max line size
     * 
     * @return Max line size
     */
    public float getMaxLineSize() {
        return lineMaxSize;
    }
 
    /**
     * Gets text string of GUI Text
     * 
     * @return Text to be rendered
     */
    protected String getTextString() {
        return textString;
    }

}

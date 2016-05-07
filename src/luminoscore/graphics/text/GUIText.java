package luminoscore.graphics.text;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * GUI Text
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
 
    private boolean centerText = false;
 
    /**
     * @param text				String defining rendered text
     * @param fontSize			Float of font size (1 Default)
     * @param font				Font to be used
     * @param position			Position of text in screen space
     * @param maxLineLength		Maximum length of line (Screen Space)
     * @param centered			Centered on screen
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

	/**
     * @return FontType 	Font used to render GUI Text instance
     * 
     * Gets font type of GUIText
     */
    public FontType getFont() {
        return font;
    }
 
    /**
     * @param r	R value
     * @param g	G value
     * @param b B value
     * 
     * Sets color of GUI Text
     */
    public void setColor(float r, float g, float b) {
        colour.set(r, g, b);
    }
 
    /**
     * @return Vector3f		Text color
     * 
     * Gets color of text
     */
    public Vector3f getColor() {
        return colour;
    }
 
    /**
     * @return int	Number lines of text
     * 
     * Gets number of lines of text
     */
    public int getNumberOfLines() {
        return numberOfLines;
    }
 
    /**
     * @return Vector2f	Screen position (top left corner)
     * 
     * Gets screen position
     */
    public Vector2f getPosition() {
        return position;
    }
 
    /**
     * @return int GPU ID of mesh
     * 
     * Gets mesh GPU ID
     */
    public int getMesh() {
        return textMeshVao;
    }
 
    /**
     * @param vao			VAO ID of Mesh
     * @param verticesCount	Vertices of mesh
     * 
     * Sets mesh data
     */
    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }
 
    /**
     * @return int	Vertex Count
     * 
     * Gets vertex count
     */
    public int getVertexCount() {
        return this.vertexCount;
    }
 
    /**
     * @return float	Font Size
     * 
     * Gets font size
     */
    protected float getFontSize() {
        return fontSize;
    }
 
    /**
     * @param number	Number of line in mesh
     * 
     * Sets number of lines in mesh
     */
    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }
 
    /**
     * @return boolean	Text center alignment
     * 
     * Gets if the mesh is centered
     */
    protected boolean isCentered() {
        return centerText;
    }
 
    /**
     * @return float	Max line size
     * 
     * Gets max line size
     */
    protected float getMaxLineSize() {
        return lineMaxSize;
    }
 
    /**
     * @return String	Text to be rendered
     * 
     * Gets text string of GUI Text
     */
    protected String getTextString() {
        return textString;
    }

}

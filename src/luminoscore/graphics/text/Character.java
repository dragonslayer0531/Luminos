package luminoscore.graphics.text;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Character data for GUI Text
 *
 */

public class Character {

	private int id;
    private double xTextureCoord;
    private double yTextureCoord;
    private double xMaxTextureCoord;
    private double yMaxTextureCoord;
    private double xOffset;
    private double yOffset;
    private double sizeX;
    private double sizeY;
    private double xAdvance;
 
    /**
     * @param id				ASCII ID of Character
     * @param xTextureCoord		X Texture Coordinate of Character
     * @param yTextureCoord		Y Texture Coordinate of Character
     * @param xTexSize			Width of Character Texture
     * @param yTexSize			Height of Character Texture
     * @param xOffset			X Offset of Character Texture
     * @param yOffset			Y Offset of Character Texture
     * @param sizeX				Size of Character, X
     * @param sizeY				Size of Character, Y
     * @param xAdvance			X distance between characters
     * 
     * Constructor
     */
    protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize,
            double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
        this.id = id;
        this.xTextureCoord = xTextureCoord;
        this.yTextureCoord = yTextureCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xMaxTextureCoord = xTexSize + xTextureCoord;
        this.yMaxTextureCoord = yTexSize + yTextureCoord;
        this.xAdvance = xAdvance;
    }
 
    /**
     * @return int	ID
     * 
     * Gets character ID
     */
    protected int getId() {
        return id;
    }
 
    /**
     * @return double 	X Texture Coordinate
     * 
     * Gets the X Texture Coordinate
     */
    protected double getxTextureCoord() {
        return xTextureCoord;
    }
 
    /**
     * @return double	Y Texture Coordinate
     * 
     * Gets the Y Texture Coordinate
     */
    protected double getyTextureCoord() {
        return yTextureCoord;
    }
 
    /**
     * @return double	X Maximum Texture Coordinate
     * 
     * Gets the maximum texture coordinate on the X axis
     */
    protected double getXMaxTextureCoord() {
        return xMaxTextureCoord;
    }
 
    /**
     * @return double	Y Maximum Texture Coordinate
     * 
     * Gets the maximum texture coordinate on the Y axis
     */
    protected double getYMaxTextureCoord() {
        return yMaxTextureCoord;
    }
 
    /**
     * @return double	X Offset
     * 
     * Gets the x offset of texture
     */
    protected double getxOffset() {
        return xOffset;
    }
 
    /**
     * @return double	Y Offset
     * 
     * Gets the y offset of texture
     */
    protected double getyOffset() {
        return yOffset;
    }
 
    /**
     * @return double	Size of texture, Width
     * 
     * Gets width of texture
     */
    protected double getSizeX() {
        return sizeX;
    }
 
    /**
     * @return double	Size of texture, height
     * 
     * Gets height of texture
     */
    protected double getSizeY() {
        return sizeY;
    }
 
    /**
     * @return double	X Advance
     * 
     * Gets the X Advance of texture
     */
    protected double getxAdvance() {
        return xAdvance;
    }
	
}

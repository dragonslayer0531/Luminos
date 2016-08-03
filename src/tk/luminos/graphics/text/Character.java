package tk.luminos.graphics.text;

/**
 * 
 * Character data for GUI Text
 * 
 * @author Nick Clark
 * @version 1.0
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
     * Constructor
     * 
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
     * Gets character ID
     * 
     * @return ASCII ID
     */
    protected int getId() {
        return id;
    }
 
    /**
     * Gets the X Texture Coordinate
     * 
     * @return 	X Texture Coordinate 
     */
    protected double getxTextureCoord() {
        return xTextureCoord;
    }
 
    /**
     * Gets the Y Texture Coordinate
     * 
     * @return Y Texture Coordinate 
     */
    protected double getyTextureCoord() {
        return yTextureCoord;
    }
 
    /**
     * Gets the maximum texture coordinate on the X axis
     * 
     * @return X Maximum Texture Coordinate
     */
    protected double getXMaxTextureCoord() {
        return xMaxTextureCoord;
    }
 
    /**
     * Gets the maximum texture coordinate on the Y axis
     * 
     * @return Y Maximum Texture Coordinate
     */
    protected double getYMaxTextureCoord() {
        return yMaxTextureCoord;
    }
 
    /**
     * Gets the x offset of texture
     * 
     * @return	X Offset
     */
    protected double getxOffset() {
        return xOffset;
    }
 
    /**
     * Gets the y offset of texture
     * 
     * @return 	Y Offset
     */
    protected double getyOffset() {
        return yOffset;
    }
 
    /**
     * Gets width of texture
     * 
     * @return Size of texture, Width
     */
    protected double getSizeX() {
        return sizeX;
    }
 
    /**
     * Gets height of texture
     * 
     * @return Size of texture, height
     */
    protected double getSizeY() {
        return sizeY;
    }
 
    /**
     * Gets the X Advance of texture
     * 
     * @return X Advance
     */
    protected double getxAdvance() {
        return xAdvance;
    }
	
}

package tk.luminos.luminoscore.graphics.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Creates Mesh of Text
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TextMeshCreator {
	
	protected static final double LINE_HEIGHT = 0.03f;
    protected static final int SPACE_ASCII = 32;
 
    private MetaFile metaData;
 
    /**
     * Constructor
     * 
     * @param metaFile	Meta File to build with
     */
    protected TextMeshCreator(File metaFile) {
        metaData = new MetaFile(metaFile);
    }
 
    /**
     * Creates text mesh
     * 
     * @param text			Text to have meshed
     * @return Mesh Data of text
     * 
     */
    protected TextMeshData createTextMesh(GUIText text) {
        List<Line> lines = createStructure(text);
        TextMeshData data = createQuadVertices(text, lines);
        return data;
    }

//*****************************************Private Methods**********************************************//    
    
    /**
     * Creates structure of mesh
     * 
     * @param text			Text to create structure of
     * @return {@link Line}s in structure
     */
    private List<Line> createStructure(GUIText text) {
        char[] chars = text.getTextString().toCharArray();
        List<Line> lines = new ArrayList<Line>();
        Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        Word currentWord = new Word(text.getFontSize());
        for (char c : chars) {
            int ascii = (int) c;
            if (ascii == SPACE_ASCII) {
                boolean added = currentLine.attemptToAddWord(currentWord);
                if (!added) {
                    lines.add(currentLine);
                    currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    currentLine.attemptToAddWord(currentWord);
                }
                currentWord = new Word(text.getFontSize());
                continue;
            }
            Character character = metaData.getCharacter(ascii);
            currentWord.addCharacter(character);
        }
        completeStructure(lines, currentLine, currentWord, text);
        return lines;
    }
 
    /**
     * Completes the structure of mesh
     * 
     * @param lines			Lines to render
     * @param currentLine	Current line of creation
     * @param currentWord	Current word of creation
     * @param text			Text to be added
     */
    private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text) {
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
            currentLine.attemptToAddWord(currentWord);
        }
        lines.add(currentLine);
    }
 
    /**
     * Creates quad for text mesh
     * 
     * @param text			Text to create quad for
     * @param lines			Lines to draw quad on
     * @return Quad for text mesh
     */
    private TextMeshData createQuadVertices(GUIText text, List<Line> lines) {
        text.setNumberOfLines(lines.size());
        double curserX = 0f;
        double curserY = 0f;
        List<Float> vertices = new ArrayList<Float>();
        List<Float> textureCoords = new ArrayList<Float>();
        for (Line line : lines) {
            if (text.isCentered()) {
                curserX = (line.getMaxLength() - line.getLineLength()) / 2;
            }
            for (Word word : line.getWords()) {
                for (Character letter : word.getCharacters()) {
                    addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
                    addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
                            letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    curserX += letter.getxAdvance() * text.getFontSize();
                }
                curserX += metaData.getSpaceWidth() * text.getFontSize();
            }
            curserX = 0;
            curserY += LINE_HEIGHT * text.getFontSize();
        }       
        return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
    }
 
    /**
     * Adds vertices for character
     * 
     * @param cursorX		X Cursor Position
     * @param cursorY		Y Cursor Position
     * @param character		Character rendered
     * @param fontSize		Font size of character
     * @param vertices		Vertices of entity
     */
    private void addVerticesForCharacter(double cursorX, double cursorY, Character character, double fontSize,
            List<Float> vertices) {
        double x = cursorX + (character.getxOffset() * fontSize);
        double y = cursorY + (character.getyOffset() * fontSize);
        double maxX = x + (character.getSizeX() * fontSize);
        double maxY = y + (character.getSizeY() * fontSize);
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        addVertices(vertices, properX, properY, properMaxX, properMaxY);
    }
 
    /**
     * Adds vertices to mesh
     * 
     * @param vertices	Vertices of quad
     * @param x			X value of quad
     * @param y			Y value of quad
     * @param maxX		X Max value of quad
     * @param maxY		Y Max value of quadd
     */
    private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        vertices.add((float) x);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) y);
    }
 
    /**
     * Adds Texture Coordinates to mesh
     * 
     * @param texCoords	Texture Coords to add
     * @param x			X Coords to add
     * @param y			Y Coords to add
     * @param maxX		X Max coords to add
     * @param maxY		Y Max coords to add
     */
    private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
        texCoords.add((float) x);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) y);
    }
 
    /**
     * Converts list of floats to float array
     * 
     * @param listOfFloats	Float List
     * @return Float array converted
     * 
     */
    private static float[] listToArray(List<Float> listOfFloats) {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listOfFloats.get(i);
        }
        return array;
    }

}

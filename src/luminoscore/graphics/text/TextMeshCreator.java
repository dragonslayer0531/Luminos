package luminoscore.graphics.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import luminoscore.graphics.display.GLFWwindow;

public class TextMeshCreator {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */
	
	//Protected global variables
	protected static final double LINE_HEIGHT = 0.03f;
	protected static final int SPACE_ASCII = 32;
	
	//Constructor Data
	private MetaFile metaData;
	
	/*
	 * @param metaFile File containing font data
	 * @param display Display to be rendered to
	 * 
	 * Constructor
	 */
	protected TextMeshCreator(File metaFile, GLFWwindow display) {
		this.metaData = new MetaFile(metaFile, display);
	}
	
	/*
	 * @param text Defines GUIText to create Mesh for
	 * @return TextMeshData
	 * 
	 * Creates text mesh for the GUIText
	 */
	protected TextMeshData createTextMesh(GUIText text) {
		List<Line> lines = createStructure(text);
		TextMeshData data = createQuadVertices(text, lines);
		return data;
	}

	/*
	 * @param GUIText Defines GUIText to create structure for
	 * @return List<Line>
	 * 
	 * Creates structure of lines for GUIText
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
	
	/*
	 * @param lines Defines lines
	 * @param currentLine Defines the currentLine
	 * @param currentWord Defines the word on the line
	 * @param text Defines the GUIText
	 * 
	 * Completes the structure of the text mesh
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

	/*
	 * @param text Defines the GUIText
	 * @param lines Defines the list of lines
	 * @return TextMeshData
	 * 
	 * Creates quad vertices
	 */
	private TextMeshData createQuadVertices(GUIText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double cursorX = 0f;
		double cursorY = 0f;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		for (Line line : lines) {
			if (text.isCentered()) {
				cursorX = (line.getMaxLength() - line.getLineLength()) / 2;
			}
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
					addVerticesForCharacter(cursorX, cursorY, letter, text.getFontSize(), vertices);
					addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
							letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
					cursorX += letter.getxAdvance() * text.getFontSize();
				}
				cursorX += metaData.getSpaceWidth() * text.getFontSize();
			}
			cursorX = 0;
			cursorY += LINE_HEIGHT * text.getFontSize();
		}		
		return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
	}

	/*
	 * @param cursorX Sets X position for cursor
	 * @param cursorY Sets Y position for cursor
	 * @param character Defines character to add
	 * @param fontSize Defines font size of character
	 * @param vertices Defines vertices of quad
	 * 
	 * Adds vertices to quad
	 */
	private void addVerticesForCharacter(double cursorX, double cursorY, Character character, double fontSize, List<Float> vertices) {
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

	/*
	 * @param vertices Defines list of vertices
	 * @param x Defines the left bound
	 * @param y Defines the top bound
	 * @param maxX Defines the right bound
	 * @param maxY Defines the bottomBound
	 * 
	 * Adds the vertices to the list of vertices
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

	/*
	 * @param texCoords Defines the list of texture coordinates
	 * @param x Defines the left bound
	 * @param y Defines the top bound
	 * @param maxX Defines the right bound
	 * @param maxY Defines the bottom bound
	 * 
	 * Adds texture coordinates to list of texture coordinates
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

	/*
	 * @param listOfFloats Defines list to be converted
	 * 
	 * Converts List<Float> to float[]
	 */
	private static float[] listToArray(List<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}
	
}

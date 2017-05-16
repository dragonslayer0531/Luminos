package tk.luminos.filesystem.xml;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.luminos.util.File;

/**
 * 
 * Parser for XML style files, such as Collada, CFG, and more.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class XMLParser {
	
	private static final Pattern DATA = Pattern.compile(">(.+?)<");
	private static final Pattern START_TAG = Pattern.compile("<(.+?)>");
	private static final Pattern ATTR_NAME = Pattern.compile("(.+?)=");
	private static final Pattern ATTR_VAL = Pattern.compile("\"(.+?)\"");
	private static final Pattern CLOSED = Pattern.compile("(</|/>)");
	
	/**
	 * Loads an XML file and returns the main node of the file
	 * 
	 * @param file				File name
	 * @return					Main node of file
	 * @throws Exception		Thrown if file cannot be found or opened
	 */
	public static XMLNode loadXMLFile(File file) throws Exception {
		BufferedReader reader = file.getReader();
		reader.readLine();
		XMLNode node = loadNode(reader);
		reader.close();
		return node;
	}
	
	private static XMLNode loadNode(BufferedReader reader) throws Exception {
		String line = reader.readLine().trim();
		if (line.startsWith("</"))
			return null;
		String[] startTags = getStartTag(line).split(" ");
		XMLNode node = new XMLNode(startTags[0].replace("/", ""));
		addAttributes(startTags, node);
		addData(line, node);
		if (CLOSED.matcher(line).find())
			return node;
		XMLNode child = null;
		while ((child = loadNode(reader)) != null)
			node.addChild(child);
		return node;
	}
	
	private static void addAttributes(String[] title, XMLNode node) {
		for (int i = 0; i < title.length; i++) {
			if (title[i].contains("=")) 
				addAttribute(title[i], node);
		}
	}
	
	private static void addData(String line, XMLNode node) {
		Matcher matcher = DATA.matcher(line);
		if (matcher.find()) {
			node.setData(matcher.group(1));
		}
	}
	
	private static void addAttribute(String attributeLine, XMLNode node) {
		Matcher nameMatch = ATTR_NAME.matcher(attributeLine);
		nameMatch.find();
		Matcher valMatch = ATTR_VAL.matcher(attributeLine);
		valMatch.find();
		node.addAttribute(nameMatch.group(1), valMatch.group(1));
	}
	
	private static String getStartTag(String line) {
		Matcher match = START_TAG.matcher(line);
		match.find();
		return match.group(1);
	}

}

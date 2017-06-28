package tk.luminos.filesystem.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Holds XML style file node information, including the name,
 * attributes, data, and children nodes
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class XMLNode {
	
	private String name;
	private Map<String, String> attributes;
	private String data;
	private Map<String, List<XMLNode>> children;
	
	/**
	 * Constructs a new node
	 * 
	 * @param name		Node name
	 */
	public XMLNode(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of the node
	 * 
	 * @return		Name of node
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the data of the node
	 * 
	 * @return		Data of node
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * Gets the attribute of the node given the attribute name
	 * 
	 * @param attr			Name of attribute
	 * @return				String data associated with given name
	 * 						If name has no data associated, returns null
	 */
	public String getAttribute(String attr) {
		if (attributes != null)
			return attributes.get(attr);
		return null;
	}
	
	/**
	 * Gets the child node given by a certain name
	 * 
	 * @param child		Name of child node
	 * @return			Node associated with given name, if no node
	 * 					is associated, returns null
	 */
	public XMLNode getChild(String child) {
		if (children != null) {
			List<XMLNode> nodes = children.get(child);
			if (nodes != null && !nodes.isEmpty())
				return nodes.get(0);
		}
		return null;
	}
	
	/**
	 * Gets child node with associated attribute
	 * 
	 * @param childName			Name of the child node	
	 * @param attr				Attribute name
	 * @param value				Attribute value
	 * @return					Node with matching name, attribute name, and
	 * 							attribute value.  If no node is found or the 
	 * 							map of nodes is null, returns null.
	 */
	public XMLNode getChildWithAttribute(String childName, String attr, String value) {
		List<XMLNode> children = getChildren(childName);
		if (children == null || children.isEmpty()) {
			return null;
		}
		for (XMLNode child : children) {
			String val = child.getAttribute(attr);
			if (value.equals(val)) {
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Gets a list of all child nodes with a given name
	 * 
	 * @param name		Name of node
	 * @return			List of nodes with given name, if no
	 * 					nodes found, returns blank list
	 */
	public List<XMLNode> getChildren(String name) {
		if (children != null) {
			List<XMLNode> childNodes = children.get(name);
			if (childNodes != null) {
				return childNodes;
			}
		}
		return new ArrayList<XMLNode>();
	}
	
	/**
	 * Adds an attribute to the node
	 * 
	 * @param attr		Attribute name
	 * @param value		Attribute value
	 */
	public void addAttribute(String attr, String value) {
		if (attributes == null) {
			attributes = new HashMap<String, String>();
		}
		attributes.put(attr, value);
	}
	
	/**
	 * Appends a child node to the map of children nodes
	 * 
	 * @param child		Child node to add
	 */
	public void addChild(XMLNode child) {
		if (children == null) {
			children = new HashMap<String, List<XMLNode>>();
		}
		List<XMLNode> list = children.get(child.name);
		if (list == null) {
			list = new ArrayList<XMLNode>();
			children.put(child.name, list);
		}
		list.add(child);
	}
	
	/**
	 * Sets data of the node
	 * 
	 * @param data		String representing the data stored
	 * 					by the node
	 */
	public void setData(String data) {
		this.data = data;
	}
	
}

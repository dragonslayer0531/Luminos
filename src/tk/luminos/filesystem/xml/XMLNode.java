package tk.luminos.filesystem.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLNode {
	
	private String name;
	private Map<String, String> attributes;
	private String data;
	private Map<String, List<XMLNode>> children;
	
	public XMLNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getData() {
		return data;
	}
	
	public String getAttribute(String attr) {
		if (attributes != null)
			return attributes.get(attr);
		return null;
	}
	
	public XMLNode getChild(String child) {
		if (children != null) {
			List<XMLNode> nodes = children.get(child);
			if (nodes != null && !nodes.isEmpty())
				return nodes.get(0);
		}
		return null;
	}
	
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
	
	public List<XMLNode> getChildren(String name) {
		if (children != null) {
			List<XMLNode> childNodes = children.get(name);
			if (childNodes != null) {
				return childNodes;
			}
		}
		return new ArrayList<XMLNode>();
	}
	
	public void addAttribute(String attr, String value) {
		if (attributes == null) {
			attributes = new HashMap<String, String>();
		}
		attributes.put(attr, value);
	}
	
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
	
	public void setData(String data) {
		this.data = data;
	}
	
}

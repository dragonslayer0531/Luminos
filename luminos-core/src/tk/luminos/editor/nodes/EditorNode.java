package tk.luminos.editor.nodes;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

public class EditorNode {
	
	public int id;
	public String name;
	public NkRect bounds;
	public int inputCount;
	public int outputCount;
	
	public EditorNode next;
	public EditorNode prev;
	
	private Map<Integer, EditorNode> input_nodes;
	private Map<Integer, EditorNode> output_nodes;
	
	public EditorNode() {
		input_nodes = new HashMap<Integer, EditorNode>();
		output_nodes = new HashMap<Integer, EditorNode>();
	}
	
	public void layout(NkContext ctx) {
		
	}
	
	public boolean addInput(int in_position, EditorNode node) {
		if (input_nodes.containsKey(in_position)) {
			return false;
		}
		
		output_nodes.put(in_position, node);
		
		return true;
	}
	
	public void removeInput(int in_position, EditorNode node) {
		input_nodes.remove(in_position, node);
	}
	
	public boolean addOutput(int out_position, EditorNode node) {
		if (output_nodes.containsKey(out_position)) {
			return false;
		}
		
		output_nodes.put(out_position, node);
		
		return true;
	}
	
	public void removeOutput(int out_position, EditorNode node) {
		output_nodes.remove(out_position, node);
	}

}

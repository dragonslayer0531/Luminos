package tk.luminos.editor.nodes;

import org.lwjgl.nuklear.NkVec2;

public class EditorNodeLink {
	
	public int input_id;
	public int input_slot;
	public int output_id;
	public int output_slot;
	public EditorNode input, output;
	
	public NkVec2 in;
	public NkVec2 out;
	
	public EditorNodeLink() {
		
	}

}

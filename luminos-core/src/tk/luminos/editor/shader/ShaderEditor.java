package tk.luminos.editor.shader;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.system.MemoryStack;

import tk.luminos.editor.nodes.NodeEditor;

public class ShaderEditor extends NodeEditor {

	public ShaderEditor(int x, int y, int width, int height, String title) {
		super(x, y, width, height, title);
	}
	
	public void layout(NkContext ctx, MemoryStack stack) {
		super.layout(ctx, stack);
	}

}

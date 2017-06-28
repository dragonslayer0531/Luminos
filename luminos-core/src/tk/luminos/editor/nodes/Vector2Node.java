package tk.luminos.editor.nodes;

import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_propertyf;

import org.lwjgl.nuklear.NkContext;

import tk.luminos.maths.Vector2;

public class Vector2Node extends EditorNode {

	public Vector2 value;

	public void layout(NkContext ctx) {
		nk_layout_row_dynamic(ctx, 25, 1);
		value.x = nk_propertyf(ctx, "X: \0", Float.MIN_VALUE, value.x, Float.MAX_VALUE, 0.1f, 1);
		value.y = nk_propertyf(ctx, "Y: \0", Float.MIN_VALUE, value.y, Float.MAX_VALUE, 0.1f, 1);
	}

}

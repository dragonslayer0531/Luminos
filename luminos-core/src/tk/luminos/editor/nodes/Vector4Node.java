package tk.luminos.editor.nodes;

import org.lwjgl.nuklear.NkContext;
import tk.luminos.maths.Vector4;

import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_propertyf;

public class Vector4Node extends EditorNode {

    public Vector4 value;

    public void layout(NkContext ctx) {
        nk_layout_row_dynamic(ctx, 25, 1);
        value.x = nk_propertyf(ctx, "X: \0", 0, value.x, 100, 0.1f, 1);
        value.y = nk_propertyf(ctx, "Y: \0", 0, value.y, 100, 0.1f, 1);
        value.z = nk_propertyf(ctx, "Z: \0", 0, value.z, 100, 0.1f, 1);
        value.w = nk_propertyf(ctx, "W: \0", 0, value.w, 100, 0.1f, 1);
    }
}

package tk.luminos.graphics.ui;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.system.MemoryStack;

public interface NuklearObject {
	
	public void layout(NkContext ctx, MemoryStack stack);

}

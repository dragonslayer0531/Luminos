package tk.luminos.graphics.ui;

import tk.luminos.filesystem.ResourceLoader;
import tk.luminos.loaders.Loader;

public class AlphaMask {
	
	private static int circle;
	private static int faded_circle;
	
	public static int getFadedCircleMask() {
		try {
			if (faded_circle == 0) {
				faded_circle = Loader.getInstance().loadTexture(ResourceLoader.loadImage("/circle_faded_mask.png"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return faded_circle;
	}
	
	public static int getCircleMask() {
		try {
			if (circle == 0) {
				circle = Loader.getInstance().loadTexture(ResourceLoader.loadImage("/circle_mask.png"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return circle;
	}

}

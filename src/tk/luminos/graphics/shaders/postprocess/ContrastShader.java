package tk.luminos.graphics.shaders.postprocess;

import tk.luminos.graphics.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram implements PostProcess {
	
	public static String VERT = "contrast.vert";
	public static String FRAG = "contrast.frag";
	
	private int location_contrast;

	public ContrastShader() {
		super(VERT, FRAG);
	}

	@Override
	public void getAllUniformLocations() {
		location_contrast = super.getUniformLocation("contrast");
	}

	@Override
	public void bindAttributes() {

	}
	
	public void setContrast(float contrast) {
		super.loadFloat(location_contrast, contrast);
	}

}

package tk.luminos.graphics.shaders;

public class GUIShader extends ShaderProgram {
	
	public static String vert = "gui.vert";
	public static String frag = "gui.frag";
	
	public GUIShader() throws Exception {
		super(vert, frag);
	}

	@Override
	public void getAllUniformLocations() throws Exception {
		super.createUniform("transformationMatrix");
		super.createUniform("color");
	}

	@Override
	public void bindAttributes() {
		
	}

}

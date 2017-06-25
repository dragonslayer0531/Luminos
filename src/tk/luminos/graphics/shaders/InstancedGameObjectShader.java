package tk.luminos.graphics.shaders;

public class InstancedGameObjectShader extends ShaderProgram {
	
	public static String VERT = "instanceGameObject.vert";
	public static String FRAG = "instanceGameObject.frag";

	public InstancedGameObjectShader() throws Exception {
		super(VERT, FRAG);
	}

	@Override
	public void getAllUniformLocations() throws Exception {
		super.createUniform("projectionMatrix");
	}

	@Override
	public void bindAttributes() {
		// TODO Auto-generated method stub

	}

}

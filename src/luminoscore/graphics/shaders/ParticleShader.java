package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {
	
	private static final String VERT = "res/shaders/particle.vert";
	private static final String FRAG = "res/shaders/particle.frag";
	
	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERT, FRAG);
	}

	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadModelViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_modelViewMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

}

package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ShadowShader extends ShaderProgram {

    private int location_mvpMatrix;
	
	public ShadowShader(String VERT, String FRAG) {
        super(VERT, FRAG);
    }
 
    protected void getAllUniformLocations() {
        location_mvpMatrix = super.getUniformLocation("mvpMatrix");
         
    }
     
    public void loadMvpMatrix(Matrix4f mvpMatrix){
        super.loadMatrix(location_mvpMatrix, mvpMatrix);
    }
 
    protected void bindAttributes() {
        super.bindAttribute(0, "in_position");
    }
	
}

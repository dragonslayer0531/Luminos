package luminoscore.graphics.shaders;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Camera;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Skybox Shader for Skybox Renderer
 *
 */

public class SkyboxShader extends ShaderProgram {
    
    private static final float ROTATE_SPEED = 0f;
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColour;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;
    
    private float rotation = 0;
     
    /**
     * @param vert	Vertex shader file
     * @param frag	Fragment shader file
     * 
     * Constructor
     */
    public SkyboxShader(String vert, String frag) {
        super(vert, frag);
    }
    
    /**
     * @param matrix	Projection Matrix
     * 
     * Loads projection matrix to shader
     */
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    
    /**
     * @param camera	Camera to create view matrix of
     * 
     * Loads view matrix to shader
     */
    public void loadViewMatrix(Camera camera, GLFWWindow window){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATE_SPEED * window.getFrameTime();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
    
    /**
     * @param r	R value of fog
     * @param g	G value of fog
     * @param b	B value of fog
     * 
     * Loads fog to shader
     */
    public void loadFogColour(float r, float g, float b){
    	super.loadVector(location_fogColour, new Vector3f(r, g, b));
    }
    
    /**
     * Connects texture units
     */
    public void connectTextureUnits(){
    	super.loadInt(location_cubeMap, 0);
    	super.loadInt(location_cubeMap2, 1);
    }
    
    /**
     * @param blend	Blend Factor
     * 
     * Loads blend factor to shader
     */
    public void loadBlendFactor(float blend){
    	super.loadFloat(location_blendFactor, blend);
    }
     
    /*
     * (non-Javadoc)
     * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
     */
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColour = super.getUniformLocation("fogColour");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
    }
 
    /*
     * (non-Javadoc)
     * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
     */
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}
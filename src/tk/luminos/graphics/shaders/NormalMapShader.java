package tk.luminos.graphics.shaders;

import java.util.List;

import tk.luminos.graphics.gameobjects.Light;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector2f;
import tk.luminos.maths.vector.Vector3f;
import tk.luminos.maths.vector.Vector4f;

/**
 * 
 * Normal Map Shader for Normal Map Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class NormalMapShader extends ShaderProgram {
	
	public static String VERT = "normal.vert";
	public static String FRAG = "normal.frag";
	
	private static final int MAX_LIGHTS = 20;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPositionEyeSpace[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_modelTexture;
	private int location_normalMap;
	private int location_maxLights;
	
	public NormalMapShader() {
		super(VERT, FRAG);
	}

	public void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColor = super.getUniformLocation("skyColour");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");
        location_plane = super.getUniformLocation("plane");
        location_modelTexture = super.getUniformLocation("modelTexture");
        location_normalMap = super.getUniformLocation("normalMap");
        location_lightPositionEyeSpace = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            location_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
        location_maxLights = super.getUniformLocation("maxLights");

	}

	public void bindAttributes() {
		super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
	}
	
	public void connectTextureUnits(){
        super.loadInt(location_modelTexture, 0);
        super.loadInt(location_normalMap, 1);
    }
     
	public void loadClipPlane(Vector4f plane){
        super.loadVector4f(location_plane, plane);
    }
     
	public void loadNumberOfRows(int numberOfRows){
        super.loadFloat(location_numberOfRows, numberOfRows);
    }
     
    public void loadOffset(float x, float y){
        super.loadVector2f(location_offset, new Vector2f(x,y));
    }
     
    public void loadSkyColour(float r, float g, float b){
        super.loadVector3f(location_skyColor, new Vector3f(r,g,b));
    }
     
    public void loadShineVariables(float damper,float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }
     
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
     
    public void loadLights(List<Light> lights, Matrix4f viewMatrix){
        for(int i=0;i<MAX_LIGHTS;i++){
            if(i<lights.size()){
                super.loadVector3f(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
                super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
                super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
            }else{
                super.loadVector3f(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
                super.loadVector3f(location_lightColor[i], new Vector3f(0, 0, 0));
                super.loadVector3f(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }
     
    public void loadViewMatrix(Matrix4f viewMatrix){
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
     
    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }
     
    private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix){
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
        Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
        return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
    }
    
    public void loadMaxLights(int maxLights) {
    	super.loadInt(location_maxLights, maxLights);
    }

}

package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.List;
import java.util.Map;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.Camera;
import tk.luminos.graphics.DirectionalLight;
import tk.luminos.graphics.ShadowBox;
import tk.luminos.graphics.ShadowFrameBuffer;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.shaders.ShadowShader;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;

/**
 * 
 * Master renderer for all shadows
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowMapMasterRenderer {
	
	public static int SHADOW_MAP_SIZE = 8192;
	 
    private ShadowFrameBuffer shadowFbo;
    private ShadowShader shader;
    private ShadowBox shadowBox;
    private Matrix4 projectionMatrix = new Matrix4();
    private Matrix4 lightViewMatrix = new Matrix4();
    private Matrix4 projectionViewMatrix = new Matrix4();
    private Matrix4 offset = createOffset();
 
    private ShadowMapEntityRenderer entityRenderer;

    /**
     * Constructor
     * 
     * @param shader	Defines shader to be rendered with
     * @param camera	Camera to be passed to shadow box
     */
    public ShadowMapMasterRenderer(ShadowShader shader, Camera camera) {
        this.shader = shader;
        shadowBox = new ShadowBox(lightViewMatrix, camera);
        shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
        entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
    }

    /**
     * Renders shadow map to buffer
     * 
     * @param entities		List of all rendered entities
     * @param terrains		List of all rendered terrains
     * @param sun			Focal light to render to shadow map
     */
    public void render(Map<TexturedModel, List<GameObject>> entities, List<Terrain> terrains, DirectionalLight sun) {
        shadowBox.update();
        Vector3 lightDirection = new Vector3(-sun.getDirection().x, -sun.getDirection().y, -sun.getDirection().z);
        prepare(lightDirection, shadowBox);
        entityRenderer.render(entities, terrains);
        finish();
    }

    /**
     * Gets the shadow map space matrix
     * 
     * @return ShadowMapSpaceMatrix
     */
    public Matrix4 getToShadowMapSpaceMatrix() {
        return Matrix4.mul(offset, projectionViewMatrix, null);
    }

    /**
     * Cleans up shader and fbo
     */
    public void cleanUp() {
        shader.cleanUp();
        shadowFbo.cleanUp();
    }
    
    /**
     * Gets the index of the shadow map on the GPU
     * 
     * @return Shadow Map GPU index
     */
    public int getShadowMap() {
        return shadowFbo.getShadowMap();
    }

    /**
     * Returns the light view matrix
     * 
     * @return PointLight Space Transformation Matrix
     */
    public Matrix4 getPointLightSpaceTransform() {
        return lightViewMatrix;
    }

//***************************Private Methods****************************//
    
    /**
     * Prepares entities for rendering to shadow buffer
     * 
     * @param lightDirection	Direction the light is from the point
     * @param box				ShadowBox that the entities are inside of
     */
    private void prepare(Vector3 lightDirection, ShadowBox box) {
        updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
        updateDirectionalLightViewMatrix(lightDirection, box.getCenter());
        Matrix4.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
        shadowFbo.bindFrameBuffer();
        glEnable(GL_DEPTH_TEST);
        glClear(GL_DEPTH_BUFFER_BIT);
        shader.start();
    }

    /** 
     * Stops shader and unbinds FBO
     */
    private void finish() {
        shader.stop();
        shadowFbo.unbindFrameBuffer();
    }

    /**
     * Updates the orientation and position of the light view matrix
     * 
     * @param direction		Direction the light view matrix is facing
     * @param center		Center of the light view matrix
     */
    private void updateDirectionalLightViewMatrix(Vector3 direction, Vector3 center) {
        direction.normalize();
        center.negate();
        lightViewMatrix.setIdentity();
        float pitch = (float) Math.acos(new Vector2(direction.x, direction.z).magnitude());
        Matrix4.rotate(pitch, new Vector3(1, 0, 0), lightViewMatrix, lightViewMatrix);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
        yaw = direction.z > 0 ? yaw - 180 : yaw;
        Matrix4.rotate((float) -Math.toRadians(yaw), new Vector3(0, 1, 0), lightViewMatrix,
                lightViewMatrix);
        Matrix4.translate(center, lightViewMatrix, lightViewMatrix);
    }

    /**
     * Updates the shadow map's orthographic projection matrix
     * 
     * @param width		Width of orthographic projection matrix
     * @param height	Height of orthographic projection matrix
     * @param length	Length of orthographic projection matrix 
     */
    private void updateOrthoProjectionMatrix(float width, float height, float length) {
        projectionMatrix.setIdentity();
        projectionMatrix.m00 = 2f / width;
        projectionMatrix.m11 = 2f / height;
        projectionMatrix.m22 = -2f / length;
        projectionMatrix.m33 = 1;
    }

    /**
     * Adds offset to view matrix
     * 
     * @return Offset of orthographic projection matrix
     */
    private static Matrix4 createOffset() {
        Matrix4 offset = new Matrix4();
        Matrix4.translate(new Vector3(0.5f, 0.5f, 0.5f), offset, offset);
        Matrix4.scale(new Vector3(0.5f, 0.5f, 0.5f), offset, offset);
        return offset;
    }

}

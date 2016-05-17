package luminoscore.graphics.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.entities.Light;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.shaders.ShadowShader;
import luminoscore.graphics.shadows.ShadowBox;
import luminoscore.graphics.shadows.ShadowFrameBuffer;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Master renderer for all shadows
 *
 */

public class ShadowMapMasterRenderer {
	
	private static final int SHADOW_MAP_SIZE = 2048;
	 
    private ShadowFrameBuffer shadowFbo;
    private ShadowShader shader;
    private ShadowBox shadowBox;
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f lightViewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private Matrix4f offset = createOffset();
 
    private ShadowMapEntityRenderer entityRenderer;

    /**
     * @param camera	Camera to be passed to shadow box
     * @param window	Window to be used for creation of FBO and Shadow Box
     * 
     * Constructor
     */
    public ShadowMapMasterRenderer(Camera camera) {
        shader = new ShadowShader();
        shadowBox = new ShadowBox(lightViewMatrix, camera);
        shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
        entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
    }

    /**
     * @param entities		List of all rendered entities
     * @param sun			Focal light to render to shadow map
     * 
     * Renders shadow map to buffer
     */
    public void render(Map<TexturedModel, List<Entity>> entities, Light sun, GLFWWindow window) {
        shadowBox.update();
        Vector3f sunPosition = sun.getPosition();
        Vector3f lightDirection = new Vector3f(-sunPosition.x, -sunPosition.y, -sunPosition.z);
        prepare(lightDirection, shadowBox);
        entityRenderer.render(entities);
        finish();
    }

    /**
     * @return Matrix4f ShadowMapSpaceMatrix
     * 
     * Gets the shadow map space matrix
     */
    public Matrix4f getToShadowMapSpaceMatrix() {
        return Matrix4f.mul(offset, projectionViewMatrix, null);
    }

    /**
     * Cleans up shader and fbo
     */
    public void cleanUp() {
        shader.cleanUp();
        shadowFbo.cleanUp();
    }
    
    /**
     * @return int Shadow Map GPU index
     * 
     * Gets the index of the shadow map on the GPU
     */
    public int getShadowMap() {
        return shadowFbo.getShadowMap();
    }

    /**
     * @return Matrix4f	Light Space Transformation Matrix
     * 
     * Returns the light view matrix
     */
    public Matrix4f getLightSpaceTransform() {
        return lightViewMatrix;
    }

//***************************Private Methods****************************//
    
    /**
     * @param lightDirection	Direction the light is from the point
     * @param box				ShadowBox that the entities are inside of
     * 
     * Prepares entities for rendering to shadow buffer
     */
    private void prepare(Vector3f lightDirection, ShadowBox box) {
        updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
        updateLightViewMatrix(lightDirection, box.getCenter());
        Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
        shadowFbo.bindFrameBuffer();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
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
     * @param direction		Direction the light view matrix is facing
     * @param center		Center of the light view matrix
     * 
     * Updates the orientation and position of the light view matrix
     */
    private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
        direction.normalise();
        center.negate();
        lightViewMatrix.setIdentity();
        float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
        Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
        yaw = direction.z > 0 ? yaw - 180 : yaw;
        Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix,
                lightViewMatrix);
        Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
    }

    /**
     * @param width		Width of orthographic projection matrix
     * @param height	Height of orthographic projection matrix
     * @param length	Length of orthographic projection matrix
     * 
     * Updates the shadow map's orthographic projection matrix
     */
    private void updateOrthoProjectionMatrix(float width, float height, float length) {
        projectionMatrix.setIdentity();
        projectionMatrix.m00 = 2f / width;
        projectionMatrix.m11 = 2f / height;
        projectionMatrix.m22 = -2f / length;
        projectionMatrix.m33 = 1;
    }

    /**
     * @return Matrix4f		Offset of orthographic projection matrix
     * 
     * Adds offset to view matrix
     */
    private static Matrix4f createOffset() {
        Matrix4f offset = new Matrix4f();
        offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
        offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
        return offset;
    }

}

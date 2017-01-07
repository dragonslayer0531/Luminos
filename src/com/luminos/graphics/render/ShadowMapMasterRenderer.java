package com.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.List;
import java.util.Map;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.DirectionalLight;
import com.luminos.graphics.gameobjects.GameObject;
import com.luminos.graphics.models.TexturedModel;
import com.luminos.graphics.shaders.ShadowShader;
import com.luminos.graphics.shadows.ShadowBox;
import com.luminos.graphics.shadows.ShadowFrameBuffer;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector2f;
import com.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * Master renderer for all shadows
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowMapMasterRenderer {
	
	public static int SHADOW_MAP_SIZE = 4096;
	 
    private ShadowFrameBuffer shadowFbo;
    private ShadowShader shader;
    private ShadowBox shadowBox;
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f lightViewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private Matrix4f offset = createOffset();
 
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
     * @param sun			Focal light to render to shadow map
     */
    public void render(Map<TexturedModel, List<GameObject>> entities, List<Terrain> terrains, DirectionalLight sun) {
        shadowBox.update();
        Vector3f lightDirection = new Vector3f(-sun.getDirection().x, -sun.getDirection().y, -sun.getDirection().z);
        prepare(lightDirection, shadowBox);
        entityRenderer.render(entities, terrains);
        finish();
    }

    /**
     * Gets the shadow map space matrix
     * 
     * @return ShadowMapSpaceMatrix
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
    public Matrix4f getPointLightSpaceTransform() {
        return lightViewMatrix;
    }

//***************************Private Methods****************************//
    
    /**
     * Prepares entities for rendering to shadow buffer
     * 
     * @param lightDirection	Direction the light is from the point
     * @param box				ShadowBox that the entities are inside of
     */
    private void prepare(Vector3f lightDirection, ShadowBox box) {
        updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
        updateDirectionalLightViewMatrix(lightDirection, box.getCenter());
        Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
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
    private void updateDirectionalLightViewMatrix(Vector3f direction, Vector3f center) {
        direction.normalize();
        center.negate();
        lightViewMatrix.setIdentity();
        float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).magnitude());
        Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
        yaw = direction.z > 0 ? yaw - 180 : yaw;
        Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix,
                lightViewMatrix);
        Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
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
    private static Matrix4f createOffset() {
        Matrix4f offset = new Matrix4f();
        Matrix4f.translate(new Vector3f(0.5f, 0.5f, 0.5f), offset, offset);
        Matrix4f.scale(new Vector3f(0.5f, 0.5f, 0.5f), offset, offset);
        return offset;
    }

}

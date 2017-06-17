package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.List;
import java.util.Map;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.graphics.Material;
import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.shaders.NormalMapShader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;

/**
 * 
 * Allows for rendering of normal mapped entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class NormalMapRenderer {

	private NormalMapShader shader;
	 
	/**
	 * Constructor
	 * 
	 * @param projectionMatrix		Projection matrix to use
	 * @throws Exception 			Thrown if shader cannot be loaded
	 */
    public NormalMapRenderer(Matrix4 projectionMatrix) throws Exception {
        this.shader = new NormalMapShader();
        shader.start();
        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }
 
    /**
     * Renders entities to current Frame Buffer
     * 
     * @param entities		Map of entities to render
     */
    public void render(Map<TexturedModel, List<GameObject>> entities) {
        shader.start();
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<GameObject> batch = entities.get(model);
            for (GameObject entity : batch) {
                prepareInstance(entity);
                glDrawElements(GL_TRIANGLES, model.getVertexArray().getIndexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel(model);
        }
        shader.stop();
    }
     
    /**
     * Cleans up the shader
     */
    public void dispose(){
        shader.dispose();
    }
 
    private void prepareTexturedModel(TexturedModel model) {
        VertexArray vao = model.getVertexArray();
        vao.bind();
        Material texture = model.getMaterial();
        shader.setUniform("numberOfRows", texture.getRows());
        if (texture.isRenderDoubleSided()) {
            SceneRenderer.disableCulling();
        }
        shader.setUniform("shineDamper", texture.getShineDamper()); 
        shader.setUniform("reflectivity", texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getMaterial().getTexture().getId());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, model.getMaterial().getNormalID());
    }
 
    private void unbindTexturedModel(TexturedModel model) {
        SceneRenderer.enableCulling();
        model.getVertexArray().unbind();
    }
 
    private void prepareInstance(GameObject entity) {
        Matrix4 transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.setUniform("transformationMatrix", transformationMatrix);
        shader.setUniform("offset", new Vector2(0, 0));
    }
	
}

package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;
import java.util.Map;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.graphics.Material;
import tk.luminos.graphics.models.RawModel;
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
	 * @param nms					Shader to use
	 * @param projectionMatrix		Projection matrix to use
	 */
    public NormalMapRenderer(NormalMapShader nms, Matrix4 projectionMatrix) {
        this.shader = nms;
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
                glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
        shader.stop();
    }
     
    /**
     * Cleans up the shader
     */
    public void cleanUp(){
        shader.cleanUp();
    }
 
    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        Material texture = model.getMaterial();
        shader.setUniform("numberOfRows", texture.getRows());
        if (texture.hasTransparency()) {
            MasterRenderer.disableCulling();
        }
        shader.setUniform("shineDamper", texture.getShineDamper()); 
        shader.setUniform("reflectivity", texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getMaterial().getDiffuseID());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, model.getMaterial().getNormalID());
    }
 
    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);
    }
 
    private void prepareInstance(GameObject entity) {
        Matrix4 transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.setUniform("transformationMatrix", transformationMatrix);
        shader.setUniform("offset", new Vector2(0, 0));
    }
	
}

package luminoscore.graphics.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.entities.Light;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.shaders.EntityShader;
import luminoscore.graphics.shaders.TerrainShader;
import luminoscore.graphics.terrains.Terrain;
import luminoscore.tools.DateUtils;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Renders Terrains and Entities
 *
 */

public class MasterRenderer {
	
	public static final float FOV = 60;
	public static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 600f;
	private static final float ENTITY_FAR_PLANE = 500f;
	
	private static final float RED = 0.1f;
	private static final float GREEN = 0.4f;
	private static final float BLUE = 0.2f;
	
	private static final String ENTITY_VERT = "res/shaders/entity.vert";
	private static final String ENTITY_FRAG = "res/shaders/entity.frag";
	private static final String TERRAIN_VERT = "res/shaders/terrain.vert";
	private static final String TERRAIN_FRAG = "res/shaders/terrain.frag";
	
	private Matrix4f projectionMatrix;
	private Matrix4f entityProjectionMatrix;
	
	private EntityShader shader = new EntityShader(ENTITY_VERT, ENTITY_FRAG);
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader(TERRAIN_VERT, TERRAIN_FRAG);
	
	private SkyboxRenderer skyboxRenderer;
	
	private ShadowMapMasterRenderer shadowRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private DateUtils du;
	
	/**
	 * @param loader	Passes loader used for rendering
	 * @param camera	Camera used to create projection matrix of
	 * @param display	Window used to get frame time of
	 * 
	 * Constructor used to create a Master Renderer
	 */
	public MasterRenderer(Loader loader, Camera camera, GLFWWindow display){
		enableCulling();
		createProjectionMatrix(display);
		createEntityProjectionMatrix(display);
		renderer = new EntityRenderer(shader, entityProjectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		du = new DateUtils();
		skyboxRenderer.prepare(du);
		this.shadowRenderer = new ShadowMapMasterRenderer(camera, display);		
	}
	
	/**
	 * @param entities		Entities to be rendered
	 * @param terrains		Terrains to be rendered
	 * @param lights		Lights to be passed into shader
	 * @param player		Player to be rendered
	 * @param camera		Camera to be renderer
	 * 
	 * Renders the entire 3D scene
	 */
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Entity player, Camera camera, GLFWWindow window) {
		for(Entity entity : entities) {
			if(Maths.getDistance(entity.getPosition(), player.getPosition()) < 200) {
				processEntity(entity);
			}
		}
		
		for(Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		
		processEntity(player);
		render(lights, camera, window);
	}
	
	/**
	 * Prepares rendering
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	/**
	 * 
	 * @param lights	Passes lights to shaders
	 * @param camera	Camera to create transformation matrix of
	 * 
	 * Renders TexturedModel
	 */
	public void render(List<Light> lights,Camera camera, GLFWWindow window){
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE, window);
		terrains.clear();
		entities.clear();
	}
	
	/**
	 * @param terrain		Terrain to be processed
	 * 
	 * Adds terrain to list of terrains
	 */
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	/**
	 * @param entity 		Entity to be processed
	 * 
	 * Processed entity
	 */
	public void processEntity(Entity entity){
		List<TexturedModel> entityModels = entity.getModels();
		for(TexturedModel entityModel : entityModels) {
			List<Entity> batch = entities.get(entityModel);
			if(batch!=null){
				batch.add(entity);
			}else{
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				entities.put(entityModel, newBatch);		
			}
		}
	}
	
	/**
	 * @param entityList	Entities to have shadows
	 * @param sun			Focal light
	 * @param display		Window to get PoV and Aspect Ratio of
	 * 
	 * Render a shadow map
	 */
	public void renderShadowMap(List<Entity> entityList, Light sun, GLFWWindow display) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		shadowRenderer.render(entities, sun, display);
		entities.clear();
	}
	
	/**
	 * @return int	ID of shadow map
	 * 
	 * Gets the ID of the shadow map
	 */
	public int getShadowMapTexture() {
		return shadowRenderer.getShadowMap();
	}
	
	/**
	 * Cleans up all shaders used
	 */
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		shadowRenderer.cleanUp();
	}
	
	/**
	 * Enables back face culling
	 */
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);		
	}
	
	/**
	 * Disables back face culling
	 */
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * @return Matrix4f	Contains Projection Matrix
	 * 
	 * Gets projection matrix of rendering
	 */
	public Matrix4f getProjectionMatrix(){
		return this.projectionMatrix;
	}

//*******************************Private Methods*************************************//
	
	/**
	 * @param display	Used to calculate aspect ratio
	 * 
	 * Creates projection matrix for entities
	 */
	private void createEntityProjectionMatrix(GLFWWindow display) {
		float aspectRatio = (float) display.getWidth() / (float) display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = ENTITY_FAR_PLANE - NEAR_PLANE;

		entityProjectionMatrix = new Matrix4f();
		entityProjectionMatrix.m00 = x_scale;
		entityProjectionMatrix.m11 = y_scale;
		entityProjectionMatrix.m22 = -((ENTITY_FAR_PLANE + NEAR_PLANE) / frustum_length);
		entityProjectionMatrix.m23 = -1;
		entityProjectionMatrix.m32 = -((2 * NEAR_PLANE * ENTITY_FAR_PLANE) / frustum_length);
		entityProjectionMatrix.m33 = 0;
	}
	
	/**
	 * @param display  Used to calculate aspect ratio
	 * 
	 * Creates projection matrix for terrains and skybox
	 */
	private void createProjectionMatrix(GLFWWindow display) {
		float aspectRatio = (float) display.getWidth() / (float) display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
}

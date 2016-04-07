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

public class MasterRenderer {
	
	public static final float FOV = 60;
	public static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 500f;
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
	
	public Matrix4f getProjectionMatrix(){
		return this.projectionMatrix;
	}
	
	public void render(List<Light> lights,Camera camera){
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
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);		
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
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
	
	public void renderShadowMap(List<Entity> entityList, Light sun, GLFWWindow display) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		shadowRenderer.render(entities, sun, display);
		entities.clear();
	}
	
	public int getShadowMapTexture() {
		return shadowRenderer.getShadowMap();
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		shadowRenderer.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
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
	
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Entity player, Camera camera) {
		for(Entity entity : entities) {
			if(Maths.getDistance(entity.getPosition(), player.getPosition()) < 200) {
				processEntity(entity);
			}
		}
		
		for(Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		
		processEntity(player);
		render(lights, camera);
	}
	
}

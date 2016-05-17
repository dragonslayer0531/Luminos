package luminoscore.graphics.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import luminoscore.GlobalLock;
import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.entities.Light;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.particles.Particle;
import luminoscore.graphics.particles.ParticleMaster;
import luminoscore.graphics.shaders.EntityShader;
import luminoscore.graphics.shaders.TerrainShader;
import luminoscore.graphics.shaders.WaterShader;
import luminoscore.graphics.terrains.Terrain;
import luminoscore.graphics.text.GUIText;
import luminoscore.graphics.textures.GuiTexture;
import luminoscore.graphics.water.WaterFrameBuffers;
import luminoscore.graphics.water.WaterTile;
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
	public static final float NEAR_PLANE = 0.15f;
	private static final float FAR_PLANE = 600f;
	private static final float ENTITY_FAR_PLANE = 1000f;
	
	private static final float RED = 0.1f;
	private static final float GREEN = 0.4f;
	private static final float BLUE = 0.2f;
	
	private Matrix4f projectionMatrix;
	private Matrix4f entityProjectionMatrix;
	
	private EntityRenderer entityRenderer;
	private EntityShader entityShader = new EntityShader();
	private GuiRenderer guiRenderer;
	private ParticleRenderer particleRenderer;
	private ShadowMapMasterRenderer shadowRenderer;
	private SkyboxRenderer skyboxRenderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private TextRenderer textRenderer;
	private WaterRenderer waterRenderer;
	private WaterShader waterShader = new WaterShader();
	
	private WaterFrameBuffers buffers;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private DateUtils du;
	
	/**
	 * @param loader	Passes loader used for rendering
	 * @param camera	Camera used to create projection matrix of
	 * 
	 * Constructor used to create a Master Renderer
	 */
	public MasterRenderer(Loader loader, Camera camera){
		enableCulling();
		createProjectionMatrix();
		createEntityProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, entityProjectionMatrix);
		guiRenderer = new GuiRenderer(loader);
		particleRenderer = new ParticleRenderer(loader, entityProjectionMatrix);
		shadowRenderer = new ShadowMapMasterRenderer(camera);		
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		textRenderer = new TextRenderer(loader);
		buffers = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, buffers, "res/textures/waterdudv.png", "res/textures/waternormal.png");
		
		du = new DateUtils();
		skyboxRenderer.prepare(du);
	}
	
	/**
	 * Renders the entire 3D scene
	 * 
	 * @param entities		Entities to be rendered
	 * @param terrains		Terrains to be rendered
	 * @param lights		Lights to be passed into shader
	 * @param player		Player to be rendered
	 * @param camera		Camera to be renderer
	 */
	public void renderScene(Iterator<Entity> entities, Iterator<Terrain> terrains, List<Light> lights, Vector3f focalPoint, Camera camera, Vector4f clipPlane, GLFWWindow window) {
		if(entities != null) {
			while(entities.hasNext()) {
				Entity entity = entities.next();
				if(Maths.getDistance(entity.getPosition(), focalPoint) < 300) {
					processEntity(entity);
				}
			}
		}
		
		if(terrains != null) {
			while(terrains.hasNext()) {
				Terrain terrain = terrains.next();
				if(Maths.getDistance(new Vector3f(terrain.getX(), focalPoint.y, terrain.getZ()), focalPoint) < 500) {
					processTerrain(terrain);
				}
			}
		}
		
		render(lights, camera, clipPlane, window);
	}
	
	/**
	 * Renders GUI Textures to screen
	 * 
	 * @param guiTextures	GUI Textures to be rendered
	 */
	public void renderGUI(List<GuiTexture> guiTextures) {
		guiRenderer.render(guiTextures);
	}
	
	/**
	 * Renders particles through screen
	 * 
	 * @param camera		{@link Camera} to render with
	 * @param particles		{@link Particle}s to be rendered
	 */
	public void renderParticles(Camera camera, GLFWWindow window) {
		ParticleMaster.update(window);
		particleRenderer.render(ParticleMaster.particles, camera);
	}
	
	/**
	 * Adds particle to ParticleMaster
	 * 
	 * @param particle		{@link Particle} to be added
	 */
	public void addParticle(Particle particle) {
		ParticleMaster.addParticle(particle);
	}
	
	/**
	 * Adds particle list to ParticleMaster
	 * 
	 * @param particles		{@link Particle}s to be added
	 */
	public void addParticles(List<Particle> particles) {
		ParticleMaster.addAllParticles(particles);
	}
	
	/**
	 * Renders text to screen
	 */
	public void renderGuiText() {
		textRenderer.render();
	}
	
	/**
	 * Loads {@link GUIText} to renderer
	 * 
	 * @param text	GUIText to be loaded 
	 */
	public void addText(GUIText text) {
		textRenderer.loadText(text);
	}
	
	/**
	 * Removes {@link GUIText} from renderer
	 * 
	 * @param text	GUIText to be removed
	 */
	public void removeText(GUIText text) {
		textRenderer.removeText(text);
	}
	
	/**
	 * Prepares water for rendering
	 * 
	 * @param entities	Passed to renderScene
	 * @param terrains	Passed to renderScene
	 * @param lights	Passed to renderScene
	 * @param focalPoint	Passed to renderScene
	 * @param camera	Calculates FBOs and passed to renderScene
	 * @param window	Passed to renderScene
	 */
	public void prepareWater(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Vector3f focalPoint, Camera camera, GLFWWindow window) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		buffers.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y);
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderScene(entities.iterator(), terrains.iterator(), lights, focalPoint, camera, new Vector4f(0, 1, 0, 1), window);
		camera.getPosition().y += distance;
		camera.invertPitch();
		buffers.bindRefractionFrameBuffer();
		renderScene(entities.iterator(), terrains.iterator(), lights, focalPoint, camera, new Vector4f(0, -1, 0, 0), window);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.unbindCurrentFrameBuffer();
	}
	
	/**
	 * Renders {@link WaterTile}s
	 * 
	 * @param tiles		Tiles to be rendered
	 * @param camera	Camera to use
	 * @param light		Light to reflect
	 */
	public void renderWater(List<WaterTile> tiles, Camera camera, Light light) {
		waterRenderer.render(tiles, camera, light);
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
	 * Renders {@link Entity}
	 * 
	 * @param lights	Passes lights to shaders
	 * @param camera	Camera to create transformation matrix of
	 */
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane, GLFWWindow window){
		prepare();
		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadSkyColour(RED, GREEN, BLUE);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		entityShader.stop();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
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
	 * Adds {@link Terrain} to list of terrains
	 * 
	 * @param terrain		Terrain to be processed 
	 */
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	/**
	 * Processes {@link Entity}
	 * 
	 * @param entity 		Entity to be processed 
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
	 * Render a shadow map
	 * 
	 * @param entityList	Entities to have shadows
	 * @param sun			Focal light
	 * @param display		Window to get PoV and Aspect Ratio of
	 */
	public void renderShadowMap(List<Entity> entityList, Light sun, GLFWWindow display) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		shadowRenderer.render(entities, sun, display);
		entities.clear();
	}
	
	/**
	 * Gets the ID of the shadow map
	 * 
	 * @return ID of shadow map
	 */
	public int getShadowMapTexture() {
		return shadowRenderer.getShadowMap();
	}
	
	/**
	 * Cleans up all shaders used
	 */
	public void cleanUp(){
		entityShader.cleanUp();
		guiRenderer.cleanUp();
		particleRenderer.cleanUp();
		shadowRenderer.cleanUp();
		terrainShader.cleanUp();
		textRenderer.cleanUp();
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
	 * Creates projection matrix for entities
	 * 
	 * @param display	Used to calculate aspect ratio
	 */
	private void createEntityProjectionMatrix() {
		float aspectRatio = (float) GlobalLock.WIDTH / (float) GlobalLock.HEIGHT;
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
	 * Creates projection matrix for terrains and skybox
	 * 
	 * @param display  Used to calculate aspect ratio
	 */
	private void createProjectionMatrix() {
		float aspectRatio = (float) GlobalLock.WIDTH / (float) GlobalLock.HEIGHT;
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


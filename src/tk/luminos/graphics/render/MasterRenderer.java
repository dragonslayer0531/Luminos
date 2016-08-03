package tk.luminos.graphics.render;

import static tk.luminos.ConfigData.HEIGHT;
import static tk.luminos.ConfigData.WIDTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import tk.luminos.graphics.display.GLFWWindow;
import tk.luminos.graphics.gameobjects.Camera;
import tk.luminos.graphics.gameobjects.Entity;
import tk.luminos.graphics.gameobjects.Light;
import tk.luminos.graphics.gui.GUIObject;
import tk.luminos.graphics.loaders.Loader;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.particles.Particle;
import tk.luminos.graphics.particles.ParticleMaster;
import tk.luminos.graphics.shaders.EntityShader;
import tk.luminos.graphics.shaders.NormalMapShader;
import tk.luminos.graphics.shaders.TerrainShader;
import tk.luminos.graphics.shaders.WaterShader;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.graphics.text.GUIText;
import tk.luminos.graphics.textures.GUITexture;
import tk.luminos.graphics.water.WaterFrameBuffers;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector3f;
import tk.luminos.maths.vector.Vector4f;
import tk.luminos.tools.DateUtils;
import tk.luminos.tools.Maths;

/**
 * 
 * Renders Terrains and Entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class MasterRenderer {

	public static final float FOV = 60;
	public static final float NEAR_PLANE = .15f;
	public static final float FAR_PLANE = 500f;
	public static final float SKYBOX_PLANE = 1500f;

	static final float RED = 0.1f;
	static final float GREEN = 0.4f;
	static final float BLUE = 0.2f;

	static final Vector3f SKY_COLOR = new Vector3f(RED, GREEN, BLUE);

	private Matrix4f projectionMatrix;
	private Matrix4f skyboxMatrix;

	private EntityRenderer entityRenderer;
	private EntityShader entityShader = new EntityShader();
	private GuiRenderer guiRenderer;
	private NormalMapRenderer normalMapRenderer;
	private NormalMapShader normalMapShader = new NormalMapShader();
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
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	private DateUtils du;

	/**
	 * Constructor used to create a Master Renderer
	 * 
	 * @param loader	Passes loader used for rendering
	 * @param camera	Camera used to create projection matrix of
	 */
	public MasterRenderer(Loader loader, Camera camera){
		enableCulling();
		projectionMatrix = createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
		skyboxMatrix = createProjectionMatrix(FOV, SKYBOX_PLANE, NEAR_PLANE);
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		guiRenderer = new GuiRenderer(loader);
		normalMapRenderer = new NormalMapRenderer(normalMapShader, projectionMatrix);
		particleRenderer = new ParticleRenderer(loader, projectionMatrix);
		shadowRenderer = new ShadowMapMasterRenderer(camera);		
		skyboxRenderer = new SkyboxRenderer(loader, skyboxMatrix);
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
	 * @param focalPoint	Location of camera focus
	 * @param camera		Camera to be renderer
	 * @param clipPlane		Plane to clip all rendering beyond
	 * @param window		{@link GLFWWindow} to get frame time of
	 */
	public void renderScene(Iterator<Entity> entities, Iterator<Terrain> terrains, List<Light> lights, Vector3f focalPoint, Camera camera, Vector4f clipPlane, GLFWWindow window) {

		if(entities != null) {
			while(entities.hasNext()) {
				Entity entity = entities.next();
				if(Maths.getDistance((Vector3f) entity.getPosition(), focalPoint) < 500) {
					processEntity(entity);
				}
			}
		}

		if(terrains != null) {
			while(terrains.hasNext()) {
				Terrain terrain = terrains.next();
				if(Maths.getDistance((Vector3f) terrain.getPosition(), focalPoint) < 800) {
					processTerrain(terrain);
				}
			}
		}

		render(lights, camera, clipPlane, window);
	}

	/**
	 * Renders GUI Textures to screen
	 * 
	 * @param objects	GUIObjects to be rendered
	 */	
	public void renderGUI(List<GUIObject> objects) {
		for(GUIObject object : objects) {
			guiRenderer.render(object.getTextures());
		}
	}

	public void renderGUI(ArrayList<GUITexture> textures) {
		guiRenderer.render(textures);
	}

	/**
	 * Renders particles through screen
	 * 
	 * @param camera		{@link Camera} to render with
	 * @param window		{@link GLFWWindow} to get frame time of
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
	 * Updates the text's value
	 * 
	 * @param text		Text to be updated
	 * @param value		String of 
	 */
	public void updateTextValue(GUIText text, String value) {
		textRenderer.updateText(text, value);
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
		List<Entity> ents = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity.getPosition().y < 0) ents.add(entity);
		}
		renderScene(ents.iterator(), terrains.iterator(), lights, focalPoint, camera, new Vector4f(0, -1, 0, 0), window);
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
	 * @param clipPlane	Plane to clip all rendering beyond
	 * @param window	{@link GLFWWindow} to get frame time of
	 */
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane, GLFWWindow window){
		prepare();
		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadSkyColor(RED, GREEN, BLUE);
		entityShader.loadMaxLights(1);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		normalMapRenderer.render(normalMapEntities);
		normalMapShader.loadClipPlane(clipPlane);
		normalMapShader.loadSkyColour(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE);
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		normalMapShader.loadMaxLights(lights.size());
		normalMapShader.loadLights(lights, viewMatrix);
		normalMapShader.loadViewMatrix(viewMatrix);
		entityShader.stop();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(SKY_COLOR);
		terrainShader.loadMaxLights(1);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		skyboxRenderer.render(camera, SKY_COLOR, window);
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
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
		if(entity.getModelTexture().hasNormal()) {
			TexturedModel entityModel = entity.getCurrentModel();
			List<Entity> batch = normalMapEntities.get(entityModel);
			if(batch!=null){
				batch.add(entity);
			}else{
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				normalMapEntities.put(entityModel, newBatch);		
			}
		} else {
			TexturedModel entityModel = entity.getCurrentModel();
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
	 * Processes {@link Entity} with a normal map
	 * 
	 * @param entity		Entity to be processed
	 */
	public void processNormalMapEntity(Entity entity){
		List<TexturedModel> entityModels = entity.getModels();
		for(TexturedModel entityModel : entityModels) {
			List<Entity> batch = normalMapEntities.get(entityModel);
			if(batch!=null){
				batch.add(entity);
			}else{
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				normalMapEntities.put(entityModel, newBatch);		
			}
		}
	}

	/**
	 * Render a shadow map
	 * 
	 * @param entityList	Entities to have shadows
	 * @param sun			Focal light
	 */
	public void renderShadowMap(List<Entity> entityList, Light sun, int width, int height) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		shadowRenderer.render(entities, sun, width, height);
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
		entityRenderer.cleanUp();
		guiRenderer.cleanUp();
		normalMapRenderer.cleanUp();
		particleRenderer.cleanUp();
		shadowRenderer.cleanUp();
		terrainRenderer.cleanUp();
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
	 * Creates projection matrix
	 * 
	 * @param fov			Field of view
	 * @param farPlane		Far view plane
	 * @param nearPlane		Near view plane
	 * @return				Projection matrix
	 */
	private Matrix4f createProjectionMatrix(float fov, float farPlane, float nearPlane) {
		float aspectRatio = (float) WIDTH / (float) HEIGHT;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = farPlane - nearPlane;

		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

}


package com.luminos.graphics.render;

import static com.luminos.ConfigData.HEIGHT;
import static com.luminos.ConfigData.WIDTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.luminos.graphics.display.GLFWWindow;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.Entity;
import com.luminos.graphics.gameobjects.Light;
import com.luminos.graphics.gui.GUIObject;
import com.luminos.graphics.loaders.Loader;
import com.luminos.graphics.models.TexturedModel;
import com.luminos.graphics.particles.Particle;
import com.luminos.graphics.particles.ParticleMaster;
import com.luminos.graphics.shaders.EntityShader;
import com.luminos.graphics.shaders.GuiShader;
import com.luminos.graphics.shaders.NormalMapShader;
import com.luminos.graphics.shaders.ParticleShader;
import com.luminos.graphics.shaders.ShadowShader;
import com.luminos.graphics.shaders.SkyboxShader;
import com.luminos.graphics.shaders.TerrainShader;
import com.luminos.graphics.shaders.TextShader;
import com.luminos.graphics.shaders.WaterShader;
import com.luminos.graphics.shadows.ShadowBox;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.graphics.text.GUIText;
import com.luminos.graphics.textures.GUITexture;
import com.luminos.graphics.water.WaterFrameBuffers;
import com.luminos.graphics.water.WaterTile;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.maths.vector.Vector4f;
import com.luminos.tools.Maths;

/**
 * 
 * Renders Terrains and Entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class MasterRenderer {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = .15f;
	public static final float FAR_PLANE = 500f;
	public static final float SKYBOX_PLANE = 1500f;

	static final float RED = 0.5f;
	static final float GREEN = 0.5f;
	static final float BLUE = 0.5f;

	static final Vector3f SKY_COLOR = new Vector3f(RED, GREEN, BLUE);

	private Matrix4f projectionMatrix;
	private Matrix4f skyboxMatrix;

	private EntityRenderer entityRenderer;
	private EntityShader entityShader = new EntityShader();
	private GuiRenderer guiRenderer;
	private GuiShader guiShader = new GuiShader();
	private NormalMapRenderer normalMapRenderer;
	private NormalMapShader normalMapShader = new NormalMapShader();
	private ParticleRenderer particleRenderer;
	private ParticleShader particleShader = new ParticleShader();
	private ShadowMapMasterRenderer shadowRenderer;
	private ShadowShader shadowShader = new ShadowShader();
	private SkyboxRenderer skyboxRenderer;
	private SkyboxShader skyboxShader = new SkyboxShader();
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private TextRenderer textRenderer;
	private TextShader textShader = new TextShader();
	private WaterRenderer waterRenderer;
	private WaterShader waterShader = new WaterShader();

	private WaterFrameBuffers buffers;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

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
		guiRenderer = new GuiRenderer(guiShader, loader);
		normalMapRenderer = new NormalMapRenderer(normalMapShader, projectionMatrix);
		particleRenderer = new ParticleRenderer(particleShader, loader, projectionMatrix);
		shadowRenderer = new ShadowMapMasterRenderer(shadowShader, camera);		
		skyboxRenderer = new SkyboxRenderer(skyboxShader, loader, skyboxMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		textRenderer = new TextRenderer(textShader, loader);
		buffers = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, buffers, "res/textures/waterdudv.png", "res/textures/waternormal.png");
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
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Vector3f focalPoint, Camera camera, Vector4f clipPlane, GLFWWindow window) {
		if (entities != null) {
			for (Entity entity : entities) {
				if (entity.isRenderable())
					processEntity(entity);
			}
		}
		if (terrains != null) {
			for (Terrain terrain : terrains) {
				if (terrain.isRenderable())
					processTerrain(terrain);
			}
		}

		if (lights == null) {
			lights = new ArrayList<Light>();
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
		renderScene(entities, terrains, lights, focalPoint, camera, new Vector4f(0, 1, 0, 1), window);
		camera.getPosition().y += distance;
		camera.invertPitch();
		buffers.bindRefractionFrameBuffer();
		List<Entity> ents = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity.getPosition().y < 0) ents.add(entity);
		}
		renderScene(ents, terrains, lights, focalPoint, camera, new Vector4f(0, -1, 0, 0), window);
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.unbindCurrentFrameBuffer();
	}

	/**
	 * Renders {@link WaterTile}s
	 * 
	 * @param tiles		Tiles to be rendered
	 * @param camera	Camera to use
	 * @param lights		Light to reflect
	 */
	public void renderWater(List<WaterTile> tiles, Camera camera, List<Light> lights) {
		waterShader.loadTiling(6);
		waterRenderer.render(tiles, camera, lights);
	}

	/**
	 * Prepares rendering
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
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
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadSkyColor(RED, GREEN, BLUE);
		entityShader.loadMaxLights(1);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities, shadowRenderer.getToShadowMapSpaceMatrix());
		normalMapRenderer.render(normalMapEntities);
		normalMapShader.loadClipPlane(clipPlane);
		normalMapShader.loadSkyColour(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE);
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		normalMapShader.loadMaxLights(lights.size());
		normalMapShader.loadLights(lights, viewMatrix);
		normalMapShader.loadViewMatrix(viewMatrix);
		entityShader.stop();
		terrainShader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
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
		for(TexturedModel model : entity.getModels()) {
			if(model.getTexture().hasNormal()) {
				TexturedModel entityModel = model;
				List<Entity> batch = normalMapEntities.get(entityModel);
				if(batch!=null){
					batch.add(entity);
				}else{
					List<Entity> newBatch = new ArrayList<Entity>();
					newBatch.add(entity);
					normalMapEntities.put(entityModel, newBatch);		
				}
			} else {
				TexturedModel entityModel = model;
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
	 * @param ents	Entities to have shadows
	 * @param focalPoint	Central rendering point
	 * @param sun			Focal light
	 */
	public void renderShadowMap(List<Entity> ents, Vector3f focalPoint, Light sun) {
		for(Entity entity : ents) {
			if(Maths.getDistance(entity.getPosition(), focalPoint) < 2 * ShadowBox.SHADOW_DISTANCE) {
				processEntity(entity);
			}
		}
		shadowRenderer.render(entities, sun);
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

	/**
	 * Creates projection matrix
	 * 
	 * @param fov			Field of view
	 * @param farPlane		Far view plane
	 * @param nearPlane		Near view plane
	 * @return				Projection matrix
	 */
	public static Matrix4f createProjectionMatrix(float fov, float farPlane, float nearPlane) {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) WIDTH / (float) HEIGHT;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = farPlane - nearPlane;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

}



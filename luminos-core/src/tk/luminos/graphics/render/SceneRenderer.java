package tk.luminos.graphics.render;

import tk.luminos.Application;
import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.*;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.water.WaterFrameBuffers;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

/**
 * 
 * Renders Terrains and Entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class SceneRenderer {

	private static final boolean STREAMS = Application.getValue("STREAMS") == 1;

	public static boolean WIREFRAME = Application.getValue("WIREFRAME") == 1;
	public static boolean FRUSTUM_CULLING = Application.getValue("FRUSTUM_CULLING") == 1;
	public static boolean RENDER_SKYBOX = Application.getValue("RENDER_SKYBOX") ==  1;

	public static float FOV = 70;
	public static float NEAR_PLANE = .15f;
	public static float FAR_PLANE = 1500f;
	public static float SKYBOX_PLANE = 1500 * 1.8f;

	public static float RED = 135.0f / 255.0f;
	public static float GREEN = 206.0f / 255.0f;
	public static float BLUE = 235.0f / 255.0f;

	public static Vector3 SKY_COLOR = new Vector3(RED, GREEN, BLUE);

	private static Matrix4 projectionMatrix;
	private Matrix4 skyboxMatrix;
	
	private GameObjectRenderer gameObjectRenderer;
	private NormalMapRenderer normalMapRenderer;
	private ShadowMapMasterRenderer shadowRenderer;
	private SkyboxRenderer skyboxRenderer;
	private TerrainRenderer terrainRenderer;
	private WaterRenderer waterRenderer;

	private FrustumIntersectionFilter fis;

	private WaterFrameBuffers buffers;
	private Map<TexturedModel, List<GameObject>> entities = new ConcurrentHashMap<TexturedModel,List<GameObject>>();
	private Map<TexturedModel, List<GameObject>> normalMapEntities = new ConcurrentHashMap<TexturedModel,List<GameObject>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	private static SceneRenderer instance;
	
	public static SceneRenderer create(Camera camera) throws Exception {
		if (instance != null) {
			System.err.println("ERROR: MINOR - MasterRenderer already created.");
			return instance;
		}
		return (instance = new SceneRenderer(camera));
	}
	
	public static SceneRenderer getInstance() {
		if (instance == null)
			throw new NullPointerException("Master Renderer is not initialized!");
		return instance;
	}

	/**
	 * Constructor used to create a Master Renderer
	 * 
	 * @param camera		Camera used to create projection matrix of
	 * @throws Exception	Exception for if file isn't found or cannot be handled
	 */
	private SceneRenderer(Camera camera) throws Exception {
		enableCulling();
		cullFace(GL_BACK);
		
		projectionMatrix = createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
		skyboxMatrix = createProjectionMatrix(FOV, SKYBOX_PLANE, NEAR_PLANE);
		gameObjectRenderer = new GameObjectRenderer(projectionMatrix);
		normalMapRenderer = new NormalMapRenderer(projectionMatrix);
		shadowRenderer = new ShadowMapMasterRenderer(camera);		
		skyboxRenderer = new SkyboxRenderer(Loader.getInstance(), skyboxMatrix);
		terrainRenderer = new TerrainRenderer(projectionMatrix);
		buffers = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(projectionMatrix, buffers, "resources/textures/waterdudv.png", "resources/textures/waternormal.png");
		
		fis = new FrustumIntersectionFilter();
	}

	/**
	 * Renders the entire 3D scene
	 * 
	 * @param entities		Entities to be rendered
	 * @param terrains		Terrains to be rendered
	 * @param lights		Lights to be passed into shader
	 * @param sun 			Light used as main source
	 * @param focalPoint	Location of camera focus
	 * @param camera		Camera to be renderer
	 * @param clipPlane		Plane to clip all rendering beyond
	 */
	public void renderScene(List<GameObject> entities, List<Terrain> terrains, List<PointLight> lights, DirectionalLight sun, Vector3 focalPoint, Camera camera, Vector4 clipPlane) {
		fis.update(projectionMatrix, MathUtils.createViewMatrix(camera));
		
		Runnable filter = () -> {
			entities.parallelStream().forEach(entity -> entity.setRenderable(fis.inside(entity.getPosition(), 10)));
			entities.parallelStream().forEach(entity -> entity.setRenderable(MathUtils.getDistance(entity.getPosition(), camera.getPosition()) < entity.getRenderDistance()));
		};
		Thread filterRunner = new Thread(filter);
		filterRunner.start();
		
		if (terrains == null) 
			terrains = new ArrayList<Terrain>();
		Iterator<Terrain> terrainIterator = terrains.iterator();
		while (terrainIterator.hasNext()) {
			Terrain terrain = terrainIterator.next();
			if (terrain.isRenderable())
				processTerrain(terrain);
		}

		if (lights == null) {
			lights = new ArrayList<PointLight>();
		}
		
		while (filterRunner.isAlive()) {};
		if (!STREAMS) {
			Iterator<GameObject> gameObjectIterator = entities.iterator();
			while (gameObjectIterator.hasNext()) {
				GameObject entity = gameObjectIterator.next();
				if (entity.isRenderable() && MathUtils.getDistance(entity.getPosition(), camera.getPosition()) < entity.getRenderDistance())
					processGameObject(entity);
			}	
		}
		else {			
			this.entities = entities
					.stream()
					.parallel()
					.filter(entity -> entity.isRenderable())
					.collect(Collectors.groupingBy(GameObject::getModel));
		}
		if (WIREFRAME)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		render(lights, sun, camera, clipPlane);
		if (WIREFRAME)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		glFlush();
	}

	/**
	 * Prepares water for rendering
	 * 
	 * @param gameObjects		Passed to render scene
	 * @param terrains			Passed to render scene
	 * @param lights			Passed to render scene
	 * @param sun				Passed to render scene
	 * @param focalPoint		Passed to render scene
	 * @param camera			Passed to render scene
	 */
	public void prepareWater(List<GameObject> gameObjects, List<Terrain> terrains, List<PointLight> lights, DirectionalLight sun, Vector3 focalPoint, Camera camera) {
		glEnable(GL_CLIP_DISTANCE0);
		buffers.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y);
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderScene(gameObjects, terrains, lights, sun, focalPoint, camera, new Vector4(0, 1, 0, 0.01f));
		camera.getPosition().y += distance;
		camera.invertPitch();
		buffers.bindRefractionFrameBuffer();
		terrainRenderer.getShader().start();
		terrainRenderer.getShader().setUniform("useWater", 0);
		terrainRenderer.getShader().stop();
		List<GameObject> ents = new ArrayList<GameObject>();
		if (!STREAMS) {
			for(GameObject entity : gameObjects) {
				if(entity.getPosition().y < 0) ents.add(entity);
			}
		}
		else {
			ents = gameObjects.stream().parallel()
					                   .filter(entity -> entity.getPosition().y < 0)
					                   .filter(entity -> MathUtils.getDistance(camera.getPosition(), entity.getPosition()) < entity.getRenderDistance())
									   .collect(Collectors.toList());
		}
		renderScene(ents, terrains, lights, sun, focalPoint, camera, new Vector4(0, -1, 0, 0));
		terrainRenderer.getShader().start();
		terrainRenderer.getShader().setUniform("useWater", 1);
		terrainRenderer.getShader().stop();
		buffers.unbindCurrentFrameBuffer();
		glDisable(GL_CLIP_DISTANCE0);
	}

	/**
	 * Renders {@link WaterTile}s
	 * 
	 * @param tiles		Tiles to be rendered
	 * @param camera	Camera to use
	 * @param lights		Light to reflect
	 */
	public void renderWater(List<WaterTile> tiles, Camera camera, List<PointLight> lights) {
		if (WIREFRAME)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		waterRenderer.render(tiles, camera, lights);
		if (WIREFRAME)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		glFlush();
	}

	/**
	 * Prepares rendering
	 */
	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 1);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
	}

	/**
	 * Renders {@link GameObject}
	 * 
	 * @param lights	Passes lights to shaders
	 * @param sun 		Main light source of scene
	 * @param camera	Camera to create transformation matrix of
	 * @param clipPlane	Plane to clip all rendering beyond
	 */
	public void render(List<PointLight> lights, DirectionalLight sun, Camera camera, Vector4 clipPlane){
		prepare();
		Matrix4 viewMatrix = MathUtils.createViewMatrix(camera);
		gameObjectRenderer.getShader().start();
		gameObjectRenderer.getShader().setUniform(gameObjectRenderer.getShader().getLocation("skyColor"), new Vector3(RED, GREEN, BLUE));
		gameObjectRenderer.getShader().setUniformPointLights("pointLights", lights);
		gameObjectRenderer.getShader().setUniformDirectionalLight("sun", sun);
		gameObjectRenderer.getShader().setUniform(gameObjectRenderer.getShader().getLocation("viewMatrix"), viewMatrix);
		gameObjectRenderer.getShader().setUniform("numPointLights", 4);
		gameObjectRenderer.render(entities);
		gameObjectRenderer.getShader().stop();
		terrainRenderer.getShader().start();
		terrainRenderer.getShader().setUniform("skyColor", SKY_COLOR);
		terrainRenderer.getShader().setUniformPointLights("pointLights", lights);
		terrainRenderer.getShader().setUniformDirectionalLight("sun", sun);
		terrainRenderer.getShader().setUniform("viewMatrix", viewMatrix);
		terrainRenderer.getShader().setUniform("numPointLights", 4);
		terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix(), getShadowMapTexture());
		terrainRenderer.getShader().stop();
		if (RENDER_SKYBOX)
			skyboxRenderer.render(viewMatrix, SKY_COLOR);
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
	 * Processes {@link GameObject}
	 * 
	 * @param entity 		GameObject to be processed 
	 */
	public void processGameObject(GameObject entity){
		TexturedModel entityModel = entity.getModel();
		List<GameObject> batch = entities.get(entityModel);
		if(batch!=null)
		{
			batch.add(entity);
		}
		else
		{
			List<GameObject> newBatch = new ArrayList<GameObject>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);		
		}
	}

	/**
	 * Render a shadow map
	 * 
	 * @param ents			Entities to have shadows
	 * @param ters			Terrains to have shadows
	 * @param focalPoint	Central rendering point
	 * @param sun			Focal light
	 */
	public void renderShadowMap(List<GameObject> ents, List<Terrain> ters, Vector3 focalPoint, DirectionalLight sun) {
		if (!WIREFRAME) {
			if (ents == null) 
				ents = new ArrayList<GameObject>();
			if (!STREAMS) {
				Iterator<GameObject> gameObjects = ents.iterator();
				while (gameObjects.hasNext()) {
					GameObject entity = gameObjects.next();
					if(entity.isRenderable() && MathUtils.getDistance(entity.getPosition(), focalPoint) < (entity.getRenderDistance() < ShadowBox.SHADOW_DISTANCE ? entity.getRenderDistance() : ShadowBox.SHADOW_DISTANCE)) {
						processGameObject(entity);
					}
				}
			}
			else {
				entities = ents.stream().parallel()
						.filter(entity -> entity.isRenderable() && MathUtils.getDistance(entity.getPosition(), focalPoint) < (entity.getRenderDistance() < ShadowBox.SHADOW_DISTANCE ? entity.getRenderDistance() : ShadowBox.SHADOW_DISTANCE * 2))
						.collect(Collectors.groupingBy(GameObject::getModel));
			}
			if (ters == null)
				ters = new ArrayList<Terrain>();
			Iterator<Terrain> terrains = ters.iterator();
			while (terrains.hasNext()) {
				Terrain terrain = terrains.next();
				if (MathUtils.getDistance((Vector3) terrain.getPosition(), focalPoint) < ShadowBox.SHADOW_DISTANCE + ShadowBox.OFFSET) 
					processTerrain(terrain);
			}
			shadowRenderer.render(this.entities, this.terrains, sun);
			this.entities.clear();
			this.terrains.clear();
		}
	}

	public static void resize() {
		projectionMatrix = createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
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
	public void dispose(){
		gameObjectRenderer.dispose();
		normalMapRenderer.dispose();
		shadowRenderer.dispose();
		skyboxRenderer.dispose();
		terrainRenderer.dispose();
		waterRenderer.dispose();
	}

	/**
	 * Enables back face culling
	 */
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
	}

	/**
	 * Set which face to cull
	 * 
	 * @param faceID		face to cull
	 */
	public static void cullFace(int faceID) {
		glCullFace(faceID);		
	}

	/**
	 * Disables back face culling
	 */
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}

	/**
	 * @return Matrix4f	Contains Projection Matrix
	 * 
	 * Gets projection matrix of rendering
	 */
	public Matrix4 getProjectionMatrix() {
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
	public static Matrix4 createProjectionMatrix(float fov, float farPlane, float nearPlane) {
		Matrix4 projectionMatrix = new Matrix4();
		float aspectRatio = (float) Application.getValue("WIDTH") / (float) Application.getValue("HEIGHT");
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

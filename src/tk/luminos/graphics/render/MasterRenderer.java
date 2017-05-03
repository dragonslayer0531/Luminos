package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

import tk.luminos.Application;
import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.Camera;
import tk.luminos.graphics.DirectionalLight;
import tk.luminos.graphics.FrustumIntersectionFilter;
import tk.luminos.graphics.PointLight;
import tk.luminos.graphics.ShadowBox;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.shaders.GameObjectShader;
import tk.luminos.graphics.shaders.GuiShader;
import tk.luminos.graphics.shaders.NormalMapShader;
import tk.luminos.graphics.shaders.ShadowShader;
import tk.luminos.graphics.shaders.SkyboxShader;
import tk.luminos.graphics.shaders.TerrainShader;
import tk.luminos.graphics.shaders.TextShader;
import tk.luminos.graphics.shaders.WaterShader;
import tk.luminos.graphics.text.GUIText;
import tk.luminos.graphics.ui.GUITexture;
import tk.luminos.graphics.water.WaterFrameBuffers;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

/**
 * 
 * Renders Terrains and Entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class MasterRenderer {

	private static final boolean STREAMS = !false;

	public static boolean WIREFRAME = Application.getValue("WIREFRAME") == 1;
	public static boolean FRUSTUM_CULLING = Application.getValue("FRUSTUM_CULLING") == 1;

	public static float FOV = 70;
	public static float NEAR_PLANE = .15f;
	public static float FAR_PLANE = 800f;
	public static float SKYBOX_PLANE = 1500f;

	public static float RED = 135.0f / 255.0f;
	public static float GREEN = 206.0f / 255.0f;
	public static float BLUE = 235.0f / 255.0f;

	public static Vector3 SKY_COLOR = new Vector3(RED, GREEN, BLUE);

	private Matrix4 projectionMatrix;
	private Matrix4 skyboxMatrix;

	private GameObjectRenderer gameObjectRenderer;
	private GameObjectShader gameObjectShader;
	private GuiRenderer guiRenderer;
	private GuiShader guiShader;
	private NormalMapRenderer normalMapRenderer;
	private NormalMapShader normalMapShader;
	private ShadowMapMasterRenderer shadowRenderer;
	private ShadowShader shadowShader;
	private SkyboxRenderer skyboxRenderer;
	private SkyboxShader skyboxShader;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader;
	private TextRenderer textRenderer;
	private TextShader textShader;
	private WaterRenderer waterRenderer;
	private WaterShader waterShader;

	private FrustumIntersectionFilter fis;

	private WaterFrameBuffers buffers;
	private Map<TexturedModel, List<GameObject>> entities = new HashMap<TexturedModel,List<GameObject>>();
	private Map<TexturedModel, List<GameObject>> normalMapEntities = new HashMap<TexturedModel,List<GameObject>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	/**
	 * Constructor used to create a Master Renderer
	 * 
	 * @param loader		Passes loader used for rendering
	 * @param camera		Camera used to create projection matrix of
	 * @throws Exception	Exception for if file isn't found or cannot be handled
	 */
	public MasterRenderer(Loader loader, Camera camera) throws Exception {
		enableCulling();
		cullFace(GL_BACK);
		gameObjectShader = new GameObjectShader();
		guiShader = new GuiShader();
		normalMapShader = new NormalMapShader();
		shadowShader = new ShadowShader();
		skyboxShader = new SkyboxShader();
		terrainShader = new TerrainShader();
		textShader = new TextShader();
		waterShader = new WaterShader();

		projectionMatrix = createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
		skyboxMatrix = createProjectionMatrix(FOV, SKYBOX_PLANE, NEAR_PLANE);
		gameObjectRenderer = new GameObjectRenderer(gameObjectShader, projectionMatrix);
		guiRenderer = new GuiRenderer(guiShader, loader);
		normalMapRenderer = new NormalMapRenderer(normalMapShader, projectionMatrix);
		shadowRenderer = new ShadowMapMasterRenderer(shadowShader, camera);		
		skyboxRenderer = new SkyboxRenderer(skyboxShader, loader, skyboxMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		textRenderer = new TextRenderer(textShader, loader);
		buffers = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, buffers, "res/textures/waterdudv.png", "res/textures/waternormal.png");

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
		if (entities == null)
			entities = new ArrayList<GameObject>();
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
		if (!STREAMS) {
			Iterator<GameObject> gameObjectIterator = entities.iterator();
			while (gameObjectIterator.hasNext()) {
				GameObject entity = gameObjectIterator.next();
				if (entity.isRenderable() && MathUtils.getDistance(entity.getPosition(), camera.getPosition()) < entity.getRenderDistance())
					processGameObject(entity);
			}	
		}
		else {			
			this.entities = entities.stream()
					.parallel()
					.filter(entity -> MathUtils.getDistance(entity.getPosition(), camera.getPosition()) < entity.getRenderDistance())
					.collect(Collectors.groupingBy(GameObject::getModel));
		}
		if (WIREFRAME)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		render(lights, sun, camera, clipPlane);
		if (WIREFRAME)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	/**
	 * Renders GUI Textures to screen
	 * 
	 * @param textures	GUITextures to be rendered
	 */	

	public void renderGUI(List<GUITexture> textures) {
		guiRenderer.render(textures);
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
		renderScene(gameObjects, terrains, lights, sun, focalPoint, camera, new Vector4(0, 1, 0, 1));
		camera.getPosition().y += distance;
		camera.invertPitch();
		buffers.bindRefractionFrameBuffer();
		List<GameObject> ents = new ArrayList<GameObject>();
		if (!STREAMS) {
			for(GameObject entity : gameObjects) {
				if(entity.getPosition().y < 0) ents.add(entity);
			}
		}
		else {
			ents = gameObjects.stream().parallel()
					                   .filter(entity -> entity.getPosition().y > 0)
									   .filter(entity -> MathUtils.getDistance(camera.getPosition(), entity.getPosition()) < entity.getRenderDistance())
									   .collect(Collectors.toList());
		}
		renderScene(ents, terrains, lights, sun, focalPoint, camera, new Vector4(0, -1, 0, 0));
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
		gameObjectShader.start();
		gameObjectShader.setUniform(gameObjectShader.getLocation("skyColor"), new Vector3(RED, GREEN, BLUE));
		gameObjectShader.setUniformPointLights("pointLights", lights);
		gameObjectShader.setUniformDirectionalLight("sun", sun);
		gameObjectShader.setUniform(gameObjectShader.getLocation("viewMatrix"), viewMatrix);
		gameObjectShader.setUniform("numPointLights", 4);
		gameObjectRenderer.render(entities);
		gameObjectShader.stop();
		terrainShader.start();
		terrainShader.setUniform("skyColor", SKY_COLOR);
		terrainShader.setUniformPointLights("pointLights", lights);
		terrainShader.setUniformDirectionalLight("sun", sun);
		terrainShader.setUniform("viewMatrix", viewMatrix);
		terrainShader.setUniform("numPointLights", 4);
		terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix(), getShadowMapTexture());
		terrainShader.stop();
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
						.filter(entity -> entity.isRenderable() && MathUtils.getDistance(entity.getPosition(), focalPoint) < (entity.getRenderDistance() < ShadowBox.SHADOW_DISTANCE ? entity.getRenderDistance() : ShadowBox.SHADOW_DISTANCE))
						.collect(Collectors.groupingBy(GameObject::getModel));
			}
			if (ters == null)
				ters = new ArrayList<Terrain>();
			Iterator<Terrain> terrains = ters.iterator();
			while (terrains.hasNext()) {
				Terrain terrain = terrains.next();
				if (MathUtils.getDistance((Vector3) terrain.getPosition(), focalPoint) < ShadowBox.SHADOW_DISTANCE) 
					processTerrain(terrain);
			}
			shadowRenderer.render(this.entities, this.terrains, sun);
			this.entities.clear();
			this.terrains.clear();
		}
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
		gameObjectRenderer.cleanUp();
		guiRenderer.cleanUp();
		normalMapRenderer.cleanUp();
		shadowRenderer.cleanUp();
		terrainRenderer.cleanUp();
		textRenderer.cleanUp();
	}

	/**
	 * Enables back face culling
	 */
	public static void enableCulling(){
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
	public static void disableCulling(){
		glDisable(GL_CULL_FACE);
	}

	/**
	 * @return Matrix4f	Contains Projection Matrix
	 * 
	 * Gets projection matrix of rendering
	 */
	public Matrix4 getProjectionMatrix(){
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



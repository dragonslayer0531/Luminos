package tk.luminos.graphics;

import java.util.List;

import tk.luminos.Application;
import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.render.PostProcessRenderer;
import tk.luminos.graphics.shaders.ImageShader;
import tk.luminos.graphics.shaders.PostProcess;
import tk.luminos.graphics.ui.GUITexture;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

/**
 * Generates scene manager
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class SceneManager {

	private MasterRenderer masterRenderer;
	private FrameBufferObject input, output;
	private PostProcessRenderer postProcessRenderer;
	
	private static int WIDTH = Application.getValue("WIDTH");
	private static int HEIGHT = Application.getValue("HEIGHT");
	private static boolean POSTPROCESS = Application.getValue("POSTPROCESS") == 1;

	/**
	 * Creates new scene manager
	 * 
	 * @param masterRenderer		controls rendering
	 * @param loader				used to load objects
	 * @throws Exception			thrown if image shader cannot be created
	 */
	public SceneManager(MasterRenderer masterRenderer, Loader loader) throws Exception {
		this.masterRenderer = masterRenderer;
		this.input = new FrameBufferObject(WIDTH, HEIGHT);
		this.output = new FrameBufferObject(WIDTH, HEIGHT, FrameBufferObject.DEPTH_TEXTURE);
		postProcessRenderer = new PostProcessRenderer(loader);
		postProcessRenderer.loadShader(new ImageShader());
	}

	/**
	 * Renders world 
	 * 
	 * @param gameObjects		game objects
	 * @param terrains			terrain
	 * @param lights			point lights
	 * @param sun				directional light
	 * @param waterTiles		water tiles
	 * @param focalPoint		focal point
	 * @param camera			camera
	 */
	public void renderWorld(List<GameObject> gameObjects, List<Terrain> terrains, List<PointLight> lights, DirectionalLight sun, List<WaterTile> waterTiles, Vector3 focalPoint, Camera camera) {
		masterRenderer.prepareWater(gameObjects, terrains, lights, sun, focalPoint, camera);
		if(POSTPROCESS) 
			input.bindFrameBuffer();
		masterRenderer.renderShadowMap(gameObjects, terrains, camera.getPosition(), sun);
		masterRenderer.renderScene(gameObjects, terrains, lights, sun, focalPoint, camera, new Vector4(0, 1, 0, Float.POSITIVE_INFINITY));
		masterRenderer.renderWater(waterTiles, camera, lights);
		masterRenderer.renderGuiText();
		if(POSTPROCESS) {
			input.unbindFrameBuffer();
			input.resolveToFBO(output);
			postProcessRenderer.render(output.getColorTexture());
		}
	}

	/**
	 * Renders GUI overlay
	 * 
	 * @param guiTextures	gui textures
	 */
	public void renderOverlay(List<GUITexture> guiTextures)	{
		if(guiTextures != null) masterRenderer.renderGUI(guiTextures);
	}

	/**
	 * Disposes of manager
	 */
	public void dispose() {
		masterRenderer.dispose();
		postProcessRenderer.dispose();
		input.cleanUp();
		output.cleanUp();
	}

	/**
	 * Loads post process to pipeline
	 * 
	 * @param process	process to add
	 */
	public void loadPostProcess(PostProcess process) {
		postProcessRenderer.loadShader(process);
	}

	/**
	 * Removes post process from pipeline
	 * 
	 * @param process	removes post process from pipeline
	 */
	public void removePostProcess(PostProcess process) {
		postProcessRenderer.removeShader(process);
	}

	/**
	 * Clears all post processes
	 */
	public void clearPostProcesses() {
		postProcessRenderer.clearAllShaders();
	}

}
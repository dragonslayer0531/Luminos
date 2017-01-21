package tk.luminos.utilities;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.ConfigData;
import tk.luminos.graphics.FrameBufferObject;
import tk.luminos.graphics.gameobjects.Camera;
import tk.luminos.graphics.gameobjects.DirectionalLight;
import tk.luminos.graphics.gameobjects.GameObject;
import tk.luminos.graphics.gameobjects.PointLight;
import tk.luminos.graphics.gui.GUIObject;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.render.PostProcessRenderer;
import tk.luminos.graphics.shaders.ImageShader;
import tk.luminos.graphics.shaders.postprocess.PostProcess;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.graphics.textures.GUITexture;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.vector.Vector3f;
import tk.luminos.maths.vector.Vector4f;

public class SceneManager {

	private MasterRenderer masterRenderer;
	private FrameBufferObject input, output;
	private PostProcessRenderer postProcessRenderer;

	public SceneManager(MasterRenderer masterRenderer, Loader loader) throws Exception {
		this.masterRenderer = masterRenderer;
		this.input = new FrameBufferObject(ConfigData.WIDTH, ConfigData.HEIGHT);
		this.output = new FrameBufferObject(ConfigData.WIDTH, ConfigData.HEIGHT, FrameBufferObject.DEPTH_TEXTURE);
		postProcessRenderer = new PostProcessRenderer(loader);
		postProcessRenderer.loadShader(new ImageShader());
	}

	public void renderWorld(List<GameObject> entities, List<Terrain> terrains, List<PointLight> lights, DirectionalLight sun, List<WaterTile> waterTiles, Vector3f focalPoint, Camera camera) {
		masterRenderer.prepareWater(entities, terrains, lights, sun, focalPoint, camera);
		if(ConfigData.POSTPROCESS) 
			input.bindFrameBuffer();
		masterRenderer.renderScene(entities, terrains, lights, sun, focalPoint, camera, new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY));
		masterRenderer.renderWater(waterTiles, camera, lights);
		masterRenderer.renderGuiText();
		if(ConfigData.POSTPROCESS) {
			input.unbindFrameBuffer();
			input.resolveToFBO(output);
			postProcessRenderer.render(output.getColorTexture());
		}
	}
	
	public void renderShadowMap(List<GameObject> entities, List<Terrain> terrains, Camera camera, DirectionalLight sun) {
		masterRenderer.renderShadowMap(entities, terrains, camera.getPosition(), sun);
	}

	public void renderOverlay(ArrayList<GUITexture> guiTextures, ArrayList<GUIObject> guiObjects)	{
		if(guiTextures != null) masterRenderer.renderGUI(guiTextures);
		if(guiObjects != null) masterRenderer.renderGUI(guiObjects);
	}

	public void dispose() {
		masterRenderer.cleanUp();
		postProcessRenderer.cleanUp();
		input.cleanUp();
		output.cleanUp();
	}

	public void loadPostProcess(PostProcess process) {
		postProcessRenderer.loadShader(process);
	}

	public void removePostProcess(PostProcess process) {
		postProcessRenderer.removeShader(process);
	}

	public void clearPostProcesses() {
		postProcessRenderer.clearAllShaders();
	}

}
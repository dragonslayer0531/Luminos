package com.luminos.tools;

import java.util.ArrayList;
import java.util.List;

import com.luminos.ConfigData;
import com.luminos.graphics.FrameBufferObject;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.GameObject;
import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.graphics.gui.GUIObject;
import com.luminos.graphics.render.MasterRenderer;
import com.luminos.graphics.render.PostProcessRenderer;
import com.luminos.graphics.shaders.ImageShader;
import com.luminos.graphics.shaders.postprocess.PostProcess;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.graphics.textures.GUITexture;
import com.luminos.graphics.water.WaterTile;
import com.luminos.loaders.Loader;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.maths.vector.Vector4f;

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

	public void renderWorld(List<GameObject> entities, List<Terrain> terrains, List<PointLight> lights, List<WaterTile> waterTiles, Vector3f focalPoint, Camera camera) {
		masterRenderer.prepareWater(entities, terrains, lights, focalPoint, camera);
		if(ConfigData.POSTPROCESS) 
			input.bindFrameBuffer();
		masterRenderer.renderScene(entities, terrains, lights, focalPoint, camera, new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY));
		masterRenderer.renderWater(waterTiles, camera, lights);
		masterRenderer.renderGuiText();
		if(ConfigData.POSTPROCESS) {
			input.unbindFrameBuffer();
			input.resolveToFBO(output);
			postProcessRenderer.render(output.getColorTexture());
		}
	}
	
	public void renderShadowMap(List<GameObject> entities, Camera camera, PointLight sun) {
		masterRenderer.renderShadowMap(entities, camera.getPosition(), sun);
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
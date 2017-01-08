package com.luminos;

import java.util.List;

import com.luminos.graphics.display.Window;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.DirectionalLight;
import com.luminos.graphics.gameobjects.GameObject;
import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.graphics.render.MasterRenderer;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.graphics.water.WaterTile;
import com.luminos.tools.SceneManager;

/**
 * 
 * Interface which determines what the engine needs to render
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface Scene {

	/**
	 * Gets the terrains in a logic scenario
	 * 
	 * @return	The terrains in a scenario
	 */
	public List<Terrain> getTerrains();
	
	/**
	 * 
	 * Gets the GameObjects in a logic scenario
	 * 
	 * @return	Game Objects in scenario
	 */
	public List<GameObject> getGameObjects();
	
	/**
	 * 
	 * Gets the WaterTiles in a logic scenario
	 * 
	 * @return	Water tiles in scenario
	 */
	public List<WaterTile> getWaterTiles();
	
	/**
	 * 
	 * Gets the Lights in a logic scenario
	 * 
	 * @return	Lights in scenario
	 */
	public List<PointLight> getPointLights();
	
	public DirectionalLight getDirectionalLight();
	
	/**
	 * Renders logic to world
	 * 
	 * @param mangager		Abstracts the rendering process
	 * @param camera		Camera to view from
	 */
	public void render(SceneManager mangager, Camera camera);
	
	/**
	 * Manages inputs to logic
	 * 
	 * @param window	Window to render to
	 * @param entity	Entity that is focal to scene
	 * @param camera	Camera that is used for rendering
	 */
	public void input(Window window, GameObject entity, Camera camera);
	
	/**
	 * Updates physics for GameLogic
	 */
	public void update(float interval);
	
	public GameObject getFocalObject();
	
	public Camera getCamera();
	
	public MasterRenderer getRenderer();

}

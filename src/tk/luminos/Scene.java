package tk.luminos;

import java.util.List;

import tk.luminos.graphics.display.Window;
import tk.luminos.graphics.gameobjects.Camera;
import tk.luminos.graphics.gameobjects.DirectionalLight;
import tk.luminos.graphics.gameobjects.GameObject;
import tk.luminos.graphics.gameobjects.PointLight;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.tools.SceneManager;

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

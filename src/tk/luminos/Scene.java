package tk.luminos;

import java.util.List;

import tk.luminos.graphics.display.Window;
import tk.luminos.graphics.gameobjects.Camera;
import tk.luminos.graphics.gameobjects.DirectionalLight;
import tk.luminos.graphics.gameobjects.GameObject;
import tk.luminos.graphics.gameobjects.Light;
import tk.luminos.graphics.gameobjects.PointLight;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.utilities.SceneManager;

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
	 * 
	 * @param interval		Update interval
	 */
	public void update(float interval);
	
	/**
	 * Gets the focal object of the scene
	 * 
	 * @return		Scene's focal game object
	 */
	public GameObject getFocalObject();
	
	/**
	 * Gets the camera used to render the scene
	 * 
	 * @return		Camera used by scene
	 */
	public Camera getCamera();
	
	/**
	 * Gets the master renderer that renders the scene to the screen
	 * 
	 * @return		Renderer which renders to screen
	 */
	public MasterRenderer getRenderer();

	/**
	 * Adds game object to the scene
	 * 
	 * @param object		Object added to scene
	 */
	void addObject(GameObject object);

	/**
	 * Adds light to the scene
	 * 
	 * @param light			Light added to scene
	 */
	void addLight(Light light);

}

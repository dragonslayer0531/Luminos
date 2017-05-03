package tk.luminos;

import java.util.List;

import tk.luminos.display.Window;
import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.Camera;
import tk.luminos.graphics.DirectionalLight;
import tk.luminos.graphics.Light;
import tk.luminos.graphics.PointLight;
import tk.luminos.graphics.SceneManager;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.water.WaterTile;

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
	 * Gets the terrains in a scene 
	 * 
	 * @return	The terrains in a scene
	 */
	public List<Terrain> getTerrains();
	
	/**
	 * 
	 * Gets the GameObjects in a scene 
	 * 
	 * @return	Game Objects in scene
	 */
	public List<GameObject> getGameObjects();
	
	/**
	 * 
	 * Gets the WaterTiles in a scene scenario
	 * 
	 * @return	Water tiles in scene
	 */
	public List<WaterTile> getWaterTiles();
	
	/**
	 * 
	 * Gets the Lights in a scene
	 * 
	 * @return	Lights in scene
	 */
	public List<PointLight> getPointLights();
	
	/**
	 * Gets the scene's directional light
	 * 
	 * @return	directional light
	 */
	public DirectionalLight getDirectionalLight();
	
	/**
	 * Renders scene to world
	 * 
	 * @param mangager		Abstracts the rendering process
	 */
	public void render(SceneManager mangager);
	
	/**
	 * Manages inputs to scene
	 * 
	 * @param window	Window to render to
	 */
	public void input(Window window);
	
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
	void addGameObject(GameObject object);
	
	/**
	 * Adds terrain to the scene
	 * 
	 * @param terrain		Terrain added to scene
	 */
	void addTerrain(Terrain terrain);

	/**
	 * Adds light to the scene
	 * 
	 * @param light			Light added to scene
	 */
	void addLight(Light light);

}

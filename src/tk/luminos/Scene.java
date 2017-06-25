package tk.luminos;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.display.Window;
import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.Camera;
import tk.luminos.graphics.DirectionalLight;
import tk.luminos.graphics.Light;
import tk.luminos.graphics.PointLight;
import tk.luminos.graphics.SceneManager;
import tk.luminos.graphics.water.WaterTile;

/**
 * 
 * Abstract class which determines what the engine needs to render
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class Scene {
	
	private List<GameObject> objects;
	private List<Terrain> terrains;
	private List<WaterTile> tiles;
	private List<PointLight> pointLights;
	
	protected DirectionalLight directionalLight;
	protected Camera camera;
	protected GameObject gameObject;
	
	/**
	 * Creates new Scene object
	 */
	public Scene() {
		objects = new ArrayList<GameObject>();
		terrains = new ArrayList<Terrain>();
		tiles = new ArrayList<WaterTile>();
		pointLights = new ArrayList<PointLight>();
	}

	/**
	 * Gets the terrains in a scene 
	 * 
	 * @return	The terrains in a scene
	 */
	public List<Terrain> getTerrains() {
		return terrains;
	}
	
	/**
	 * 
	 * Gets the GameObjects in a scene 
	 * 
	 * @return	Game Objects in scene
	 */
	public List<GameObject> getGameObjects() {
		return objects;
	}
		
	/**
	 * 
	 * Gets the WaterTiles in a scene scenario
	 * 
	 * @return	Water tiles in scene
	 */
	public List<WaterTile> getWaterTiles() {
		return tiles;
	}
	
	/**
	 * 
	 * Gets the Lights in a scene
	 * 
	 * @return	Lights in scene
	 */
	public List<PointLight> getPointLights() {
		return pointLights;
	}
	
	/**
	 * Gets the scene's directional light
	 * 
	 * @return	directional light
	 */
	public DirectionalLight getDirectionalLight() {
		return directionalLight;
	}
	
	/**
	 * Renders scene to world
	 * 
	 * @param mangager		Abstracts the rendering process
	 */
	public void render(SceneManager mangager) {
		
	}
	
	/**
	 * Manages inputs to scene
	 * 
	 * @param window	Window to render to
	 */
	public void input(Window window) {
		
	}
	
	/**
	 * Gets the focal object of the scene
	 * 
	 * @return		Scene's focal game object
	 */
	public GameObject getFocalObject() {
		return gameObject;
	}
	
	/**
	 * Gets the camera used to render the scene
	 * 
	 * @return		Camera used by scene
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Adds game object to the scene
	 * 
	 * @param object		Object added to scene
	 */
	public void addGameObject(GameObject object) {
		object.setID("DEFAULT" + objects.size());
		objects.add(object);
	}
	
	/**
	 * Adds terrain to the scene
	 * 
	 * @param terrain		Terrain added to scene
	 */
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	/**
	 * Adds light to the scene
	 * 
	 * @param light			Light added to scene
	 */
	public void addLight(Light light) {
		if (light instanceof PointLight) 
			pointLights.add((PointLight) light);
	}
	
	/**
	 * Adds water tile to the scene
	 * 
	 * @param tile			Water tile added to scene
	 */
	public void addWaterTile(WaterTile tile) {
		tiles.add(tile);
	}

}

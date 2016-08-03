package tk.luminos;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.display.GLFWWindow;
import tk.luminos.graphics.gameobjects.Camera;
import tk.luminos.graphics.gameobjects.Entity;
import tk.luminos.graphics.gameobjects.Light;
import tk.luminos.graphics.loaders.Loader;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.maths.vector.Vector3f;
import tk.luminos.maths.vector.Vector4f;
import tk.luminos.physics.Body;
import tk.luminos.physics.Manager;

public class WorldManager {
	
	private MasterRenderer masterRenderer;
	
	public WorldManager(Loader loader, Camera camera) {
		this.masterRenderer = new MasterRenderer(loader, camera);
	}
	
	public void update(List<Body> bodies, List<Terrain> terrains, List<WaterTile> tiles,  List<Light> lights, Vector3f focalPoint, Camera camera, Vector4f clipPlane, GLFWWindow window) {
		List<Entity> entities = new ArrayList<Entity>();
		for(Body body : bodies) entities.add(body.getEntity());
		Manager.update(bodies, window);
		masterRenderer.prepareWater(entities, terrains, lights, focalPoint, camera, window);
		masterRenderer.renderWater(tiles, camera, lights.get(0));
		masterRenderer.renderScene(entities.iterator(), terrains.iterator(), lights, focalPoint, camera, clipPlane, window);
	}

}

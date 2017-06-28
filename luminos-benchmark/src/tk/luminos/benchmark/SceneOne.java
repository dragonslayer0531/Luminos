package tk.luminos.benchmark;

import tk.luminos.Application;
import tk.luminos.Scene;
import tk.luminos.display.Window;
import tk.luminos.filesystem.AssimpLoader;
import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.*;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.loaders.AssetCache;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class SceneOne extends Scene {

	public static boolean loadFromDatabase = true;

	static {
	    loadFromDatabase = new File("assets.lum").exists();
    }

	private DirectionalLight sun;
	public Camera camera;
	public GameObject entity;
	public int speedFactor;

	public SceneOne(Vector<GameObject> entities, List<Terrain> terrains, List<WaterTile> waterTiles, List<PointLight> lights, DirectionalLight sun) {
		super();
		entities.stream().forEach(ent -> super.addGameObject(ent));
		terrains.stream().forEach(ter -> super.addTerrain(ter));
		waterTiles.stream().forEach(tile -> super.addWaterTile(tile));
		lights.parallelStream().forEach(light -> super.addLight(light));
		this.sun = sun;
	}

	public static SceneOne init() throws Exception {
        Instant start = Instant.now();
		Random r = new Random(5);
		int terrainEdgeSize = 20;
		int xOffset = (int) -1000;
		int yOffset = (int) -1000;

		TexturedModel model;
		if (!loadFromDatabase) model = AssimpLoader.load("resources/models/person.obj", "resources/textures/playerTexture.png")[0];
		else model = AssetCache.getModel("person.obj");

		Entity entity = new Entity(model, new Vector3(xOffset + 1250, 50, yOffset + 1250), new Vector3(0, 0, 0), new Vector3(1, 1, 1));
		Vector<GameObject> entities = new Vector<GameObject>();
		Camera camera = new Camera(entity);

		List<Terrain> terrains = new Vector<Terrain>();

		if (!loadFromDatabase) {
            TerrainTexturePack tp = new TerrainTexturePack("main", new String[] {"resources/textures/grassy2.png", "resources/textures/sand.jpg", "resources/textures/pinkFlowers.png", "resources/textures/path.png"});
            AssetCache.addTerrainTexturePack(tp);
			for(int x = 0; x < 2 * (xOffset/Application.getValue("SIZE") + terrainEdgeSize); x++) {
				for(int y = 0; y < 2 * (yOffset / Application.getValue("SIZE") + terrainEdgeSize); y++) {
					Terrain terrain = new Terrain(x, y, 25, tp);
					terrains.add(terrain);
					Thread t = new Thread() {
						public void run() {
							AssetCache.addTerrain(terrain);
						}
					};
					t.start();
				}
			}
		}
		else
			terrains = AssetCache.getTerrains();

		TexturedModel grass;
		if (!loadFromDatabase) grass = AssimpLoader.load("resources/models/grass.obj", "resources/textures/grassTexture.png")[0];
		else grass = AssetCache.getModel("grass.obj");
		grass.getMaterial().setRenderDoubleSided(1)
						   .setFakeLighting(1)
						   .setTransparency(1);

		TexturedModel pine;
		if (!loadFromDatabase) pine = AssimpLoader.load("resources/models/pine.obj", "resources/textures/pine.png")[0];
		else pine = AssetCache.getModel("pine.obj");
		pine.getMaterial().setTransparency(1);

		for(Terrain terrain : terrains) {
			for(int i = 0; i < r.nextFloat() * 100; i++) {
				float x = terrain.getX() + Application.getValue("SIZE") * r.nextFloat();
				float z = terrain.getZ() + Application.getValue("SIZE") * r.nextFloat();

				float y = TerrainUtils.getTerrain(terrains, x, z).getHeightOfTerrain(x, z);
				if(y < 5) {continue;}
				float s = r.nextFloat() + 1;
				Entity e = new Entity(pine, new Vector3(x, y - 1, z), new Vector3(0, 360 * r.nextFloat(), 0), new Vector3(s, s, s));
				e.setRenderDistance(Float.MAX_VALUE);
				entities.add(e);
			}

			for(int i = 0; i < r.nextFloat() * 5000; i++) {
				float x = terrain.getX() + Application.getValue("SIZE") * r.nextFloat();
				float z = terrain.getZ() + Application.getValue("SIZE") * r.nextFloat();

				float y = TerrainUtils.getTerrain(terrains, x, z).getHeightOfTerrain(x, z);
				if(y < 5) {continue;}
				Entity e = new Entity(grass, new Vector3(x, y - .1f, z), new Vector3(0, 360 * r.nextFloat(), 0), new Vector3(2, 2, 2));
				e.setRenderDistance(100);
				entities.add(e);
			}
		}

		List<PointLight> lights = new Vector<PointLight>();
		PointLight l = new PointLight(new Vector3(xOffset, 100, yOffset), new Vector3(0, 0, 0));
		DirectionalLight sun = new DirectionalLight(new Vector3(1, 1, 1), new Vector3(-0.3f, 1, -0.6f), 1.0f);
		lights.add(0, l);


		List<WaterTile> waterTiles = new Vector<WaterTile>();
		for(int x = 0; x < (Application.getValue("SIZE") * terrainEdgeSize / 100); x++) {
			for(int y = 0; y < (Application.getValue("SIZE") * terrainEdgeSize / 100); y++) {
				WaterTile tile = new WaterTile(x * 100 + 50, y * 100 + 50, 0, new Vector2(50, 50));
				waterTiles.add(tile);
			}
		}
		List<WaterTile> removed = new Vector<WaterTile>();
		for(WaterTile tile : waterTiles) {
			Vector2 corner1 = new Vector2(tile.getX() - tile.getScale().x, tile.getZ() - tile.getScale().y);
			Vector2 corner2 = new Vector2(tile.getX() - tile.getScale().x, tile.getZ() + tile.getScale().y);
			Vector2 corner3 = new Vector2(tile.getX() + tile.getScale().x, tile.getZ() + tile.getScale().y);
			Vector2 corner4 = new Vector2(tile.getX() + tile.getScale().x, tile.getZ() - tile.getScale().y);

			try {
				if(TerrainUtils.getTerrain(terrains, corner1.x, corner1.y).getHeightOfTerrain(corner1.x, corner1.y) > 0) {
					if(TerrainUtils.getTerrain(terrains, corner2.x, corner2.y).getHeightOfTerrain(corner2.x, corner2.y) > 0) {
						if(TerrainUtils.getTerrain(terrains, corner3.x, corner3.y).getHeightOfTerrain(corner3.x, corner3.y) > 0) {
							if(TerrainUtils.getTerrain(terrains, corner4.x, corner4.y).getHeightOfTerrain(corner4.x, corner4.y) > 0) {
								removed.add(tile);
							}
						}
					}
				}
			}
			catch (NullPointerException e) {
				System.out.println("Terrain: " + TerrainUtils.getTerrain(terrains, corner1.x, corner1.y));
				e.printStackTrace();
			}
		}

		waterTiles.removeAll(removed);

		camera.setPitch(45f);
		entity.setPosition(new Vector3(xOffset + 1300, 50, yOffset + 1250));
		entity.setRotation(new Vector3(0, 90, 0));

		SceneOne logic = new SceneOne(entities, terrains, waterTiles, lights, sun);
		logic.camera = camera;
		logic.entity = entity;

		Terrain ter = TerrainUtils.getTerrain(terrains, entity.getPosition().x, entity.getPosition().z);
		float offset = 0;
		if (ter != null) {
			offset = ter.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
		}
		entity.getPosition().y = offset + 20;
		Instant end = Instant.now();

		System.out.println(Duration.between(start, end));
		return logic;
	}

	@Override
	public void input(Window window) {		
		Vector3 move = new Vector3(3, 0, 1);
		move.scale(window.getFrameTime());
		Terrain ter = TerrainUtils.getTerrain(this.getTerrains(), entity.getPosition().x, entity.getPosition().z);
		float offset = 0;
		if (ter != null) {
			offset = ter.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
			if (offset < 0)
				offset = 0;
		}
		
		move.scale(speedFactor);
		entity.getPosition().y = offset + 20;
		entity.getPosition().add(move);
		entity.getRotation().y += window.getFrameTime();
		camera.setPitch(camera.getPitch() - (window.getFrameTime() / 10));
		camera.move();
	}

	@Override
	public void render(SceneManager manager) {
		manager.renderWorld(this.getGameObjects(), this.getTerrains(), this.getPointLights(), sun, this.getWaterTiles(), camera.getPosition(), camera);
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public GameObject getFocalObject() {
		return entity;
	}

	@Override
	public DirectionalLight getDirectionalLight() {
		return sun;
	}

}

class TerrainUtils {

	public static Terrain getTerrain(List<Terrain> terrains, float worldX, float worldZ) {

		for(Terrain t : terrains) {
			if(MathUtils.isBetween(worldX, t.getX(), t.getX() + t.getScale())) {
				if(MathUtils.isBetween(worldZ, t.getZ(), t.getZ() + t.getScale())) {
					return t;
				}
			}
		}

		return null;
	}

}

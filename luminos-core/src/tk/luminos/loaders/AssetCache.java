package tk.luminos.loaders;

import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.Material;
import tk.luminos.graphics.TerrainTexturePack;
import tk.luminos.graphics.Texture;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.serialization.DBObject;
import tk.luminos.serialization.DBObjectType;
import tk.luminos.serialization.Database;
import tk.luminos.serialization.TerrainData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetCache {
	
	public static Database db;
	
	static {
		if (new File("assets.lum").exists())
			db = Database.deserialize("assets.lum");
		else
			db = new Database("assets.lum");
	}
	
	protected static Map<String, TexturedModel> models;
	protected static List<Terrain> terrains;
	protected static List<TerrainTexturePack> texturePacks;
	
	static {
		models = new HashMap<String, TexturedModel>();
		terrains = new ArrayList<Terrain>();
		texturePacks = new ArrayList<TerrainTexturePack>();
	}

	public static void save() {
	    db.serialize("assets.lum");
    }
	
	public static void load() {
		db.objects.stream().filter(obj -> obj.objectType == DBObjectType.MODEL).forEach(obj -> {
			
			float[] verts = obj.findArray("vertices").floatData;
			float[] texts = obj.findArray("textureCoords").floatData;
			float[] norms = obj.findArray("normals").floatData;
			int[] indices = obj.findArray("indices").intData;
			String texture = obj.findString("texture").getString();
			Material mat = new Material();
			try {
				mat.attachTexture(new Texture("resources/textures/" + texture, 1, 1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			TexturedModel m = new TexturedModel(Loader.getInstance().load(verts, texts, norms, indices), mat);
			models.put(obj.getName(), m);
		
		});

		for (DBObject dbo : db.objects) {
		    if (dbo.objectType == DBObjectType.TERRAINTEXTUREPACK) {
		        texturePacks.add(TerrainTexturePack.deserialize(dbo));
            }
        }

		List<TerrainData> td = new ArrayList<TerrainData>();
		List<Thread> threads = new ArrayList<Thread>();
        db.objects.forEach(dbo -> {
            if (dbo.objectType == DBObjectType.TERRAIN) {
                Thread thread = new Thread() {

                    public void run() {
                        TerrainData t = new TerrainData();
                        t.vertices = dbo.findArray("vertices").floatData;
                        t.textures = dbo.findArray("textureCoords").floatData;
                        t.normals = dbo.findArray("normals").floatData;
                        float[] pos = dbo.findArray("position").floatData;
                        t.indices = dbo.findArray("indices").intData;
                        t.x = pos[0];
                        t.z = pos[2];
                        t.texturePack = AssetCache.findTerrainTexturePack(dbo.findString("ttp").getString());
                        if (t == null) {
                            System.out.println("NULL");
                        }
                        td.add(t);
                    }

                };

                thread.start();
                threads.add(thread);
            }
        });

        while (threads.stream().anyMatch(thread -> thread.isAlive())) {
//            try {
//                Thread.sleep(100);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        for (TerrainData t : td) {
        	if (t == null)
            {
                System.out.println(td.size());
                continue;
            }
            try {
                terrains.add(t.toTerrain());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
	}
	
	public static TexturedModel getModel(String model) {
		if (models.containsKey(model)) {
			System.out.println("Cache Hit");
			return models.get(model);
		}
		else {
			System.out.println("Cache Miss");
			DBObject obj = db.findObject(model);
			if (obj == null)
				throw new RuntimeException("Model: " + model + " not found in files");
			float[] verts = obj.findArray("vertices").floatData;
			float[] texts = obj.findArray("textureCoords").floatData;
			float[] norms = obj.findArray("normals").floatData;
			int[] indices = obj.findArray("indices").intData;
			String texture = obj.findString("texture").getString();
			Material mat = new Material();
			try {
				mat.attachTexture(new Texture("resources/textures/" + texture, 1, 1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			TexturedModel m = new TexturedModel(Loader.getInstance().load(verts, texts, norms, indices), mat);
			models.put(model, m);
			return m;
		}
	}

	public static List<Terrain> getTerrains() {
		return terrains;
	}

	public static void addTerrainTexturePack(TerrainTexturePack ttp) {
	    db.addObject(ttp.serialize(ttp.name));
    }

	public static void addTerrain(Terrain terrain) {
	    db.addObject(terrain.serialize("terrain"));
    }

    public static TerrainTexturePack findTerrainTexturePack(String name) {
	    for (TerrainTexturePack texturePack : texturePacks) {
	        if (texturePack.name.equals(name))
	            return texturePack;
        }
	    return TerrainTexturePack.deserialize(db.findObject(name));
    }

}

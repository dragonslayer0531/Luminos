package tk.luminos.loaders;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tk.luminos.graphics.Material;
import tk.luminos.graphics.Texture;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.serialization.DBObject;
import tk.luminos.serialization.DBObjectType;
import tk.luminos.serialization.Database;

public class AssetCache {
	
	public static Database db;
	
	static {
		if (new File("assets.lum").exists())
			db = Database.deserialize("assets.lum");
		else
			db = new Database("assets.lum");
	}
	
	protected static Map<String, TexturedModel> models;
	
	static {
		models = new HashMap<String, TexturedModel>();
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
				mat.attachTexture(new Texture("res/textures/" + texture, 1, 1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			TexturedModel m = new TexturedModel(Loader.getInstance().load(verts, texts, norms, indices), mat);
			models.put(obj.getName(), m);
		
		});
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
				mat.attachTexture(new Texture("res/textures/" + texture, 1, 1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			TexturedModel m = new TexturedModel(Loader.getInstance().load(verts, texts, norms, indices), mat);
			models.put(model, m);
			return m;
		}
	}

}

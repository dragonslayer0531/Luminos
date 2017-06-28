package tk.luminos.graphics;

import tk.luminos.loaders.Loader;
import tk.luminos.serialization.DBObject;
import tk.luminos.serialization.DBObjectType;
import tk.luminos.serialization.DBString;
import tk.luminos.serialization.Serializable;

/**
 * 
 * Texture Pack for Terrains
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TerrainTexturePack implements Serializable<DBObject> {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	/**
	 * Constructor
	 * 
	 * @param backgroundTexture	Color: (0, 0, 0)
	 * @param rTexture			Color: (1, 0, 0)
	 * @param gTexture			Color: (0, 1, 0)
	 * @param bTexture			Color: (0, 0, 1)
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public String name;
	public String[] textures;

	public TerrainTexturePack(String name, String[] textures) {
		try {
			this.backgroundTexture = new TerrainTexture(Loader.getInstance().loadTexture(textures[0]));
			this.rTexture = new TerrainTexture(Loader.getInstance().loadTexture(textures[1]));
			this.gTexture = new TerrainTexture(Loader.getInstance().loadTexture(textures[2]));
			this.bTexture = new TerrainTexture(Loader.getInstance().loadTexture(textures[3]));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.name = name;
		this.textures = textures;
	}

	public DBObject serialize(String name) {
	    DBObject object = new DBObject(name, DBObjectType.TERRAINTEXTUREPACK);
	    object.addString(DBString.create("BG", textures[0]));
        object.addString(DBString.create("R", textures[1]));
        object.addString(DBString.create("G", textures[2]));
        object.addString(DBString.create("B", textures[3]));
	    return object;
    }

    public static TerrainTexturePack deserialize(DBObject obj) {
	    return new TerrainTexturePack(obj.getName(), new String[] {obj.findString("BG").getString(),obj.findString("R").getString(), obj.findString("G").getString(),obj.findString("B").getString()});
    }

	/**
	 * Gets background texture
	 * 
	 * @return	Black texture
	 */
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	/**
	 * Gets R Texture
	 * 
	 * @return	Red Texture
	 */
	public TerrainTexture getrTexture() {
		return rTexture;
	}

	/**
	 * Gets G Texture
	 * 
	 * @return	Green Texture
	 */
	public TerrainTexture getgTexture() {
		return gTexture;
	}

	/**
	 * Gets B Texture
	 * 
	 * @return	Blue Texture
	 */
	public TerrainTexture getbTexture() {
		return bTexture;
	}

}

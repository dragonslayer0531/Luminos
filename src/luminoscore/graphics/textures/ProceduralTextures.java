package luminoscore.graphics.textures;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.terrains.Terrain;

public class ProceduralTextures {
	
	private Terrain terrain;
	private TerrainTexturePack terrainTextures;
	
	public ProceduralTextures(Terrain terrain, TerrainTexturePack terrainTextures) {
		this.terrain = terrain;
		this.terrainTextures = terrainTextures;
	}
	
	public void calculate() {
		Map<Vector3f, Float> normMap = new HashMap<Vector3f, Float>();
		for(int i = 0; i < terrain.getNormals().length/3 - 1; i++) {
			Vector3f vec = new Vector3f(terrain.getNormals()[3*i], terrain.getNormals()[3*i + 1], terrain.getNormals()[3*i + 2]);
			float theta = (float) Math.atan2(Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2)), vec.z);
			theta = (float) (theta * 180/Math.PI);
			normMap.put(vec, theta);
		}		
	}
	
	public TerrainTexturePack getTerrainTexturePack() {
		return terrainTextures;
	}
	
}
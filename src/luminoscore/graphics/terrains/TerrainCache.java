package luminoscore.graphics.terrains;

import java.util.ArrayList;
import java.util.List;

import luminoscore.graphics.entities.Entity;

public class TerrainCache {
	
	private List<Terrain> terrains;
	private List<Terrain> set;
	
	public TerrainCache(List<Terrain> terrains) {
		this.terrains = terrains;
	}
	
	public void addTerrain(Terrain terrain) {
		this.terrains.add(terrain);
	}
	
	public Terrain getTerrain(int x, int z) {
		for(Terrain terrain : terrains) {
			if((terrain.getX() == x) && (terrain.getZ() == z)) {
				return terrain;
			}
		}
		return null;
	}
	
	public void removeTerrain(Terrain terrain) {
		terrains.remove(terrain);
	}
	
	public void removeTerrain(int x, int z) {
		for(Terrain terrain : terrains) {
			if((terrain.getX() == x) && (terrain.getZ() == z)) {
				terrains.remove(terrain);
				break;
			}
		}
	}
	
	public List<Terrain> getTerrainsInRadius(Entity entity, List<Terrain> terrains, int radius) {
		
		Terrain t = null;
		for(Terrain terrain : terrains) {
			if(terrain.isOnTerrain(entity)) t = terrain;
		}
		
		float x = t.getX();
		float z = t.getZ();
		
		radius *= Terrain.SIZE;
		
		int xMin = (int) (x - radius);
		int xMax = (int) (x + radius);
		
		int zMin = (int) (z - radius);
		int zMax = (int) (z + radius);
		
		set = new ArrayList<Terrain>();
		for(Terrain terrain : terrains) {
			if(terrain.getX() >= xMin && terrain.getX() <= xMax) {
				if(terrain.getZ() >= zMin && terrain.getZ() <= zMax) {
					set.add(terrain);
				}
			}
		}
		
		return set;
	}
	
	public void clear() {
		set.clear();
	}
	
}

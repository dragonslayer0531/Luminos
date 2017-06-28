package tk.luminos.benchmark;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.maths.Vector3;
import tk.luminos.physics.Collider;

public class Entity extends GameObject {
	
	public static int GRAVITY = -50;
	private String id = "";
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public Entity(TexturedModel model, Vector3 position, Vector3 rotation, Vector3 scale) {
		super(model, position, rotation, scale);
	}
	
	@Override
	public String getID() {
		return id;
	}
	
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
	public List<Terrain> getTerrains() {
		return terrains;
	}
	
	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}
	
	public Collider getCollider() {
		return null;
	}
	
}
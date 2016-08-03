package tk.luminos.physics;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.gameobjects.Entity;
import tk.luminos.physics.colliders.Collider;
import tk.luminos.physics.forces.Force;

public class Body {
	
	private Entity entity;
	private float mass;
	private List<Force> forces = new ArrayList<Force>();
	private Collider collider;
	
	public Body(Entity entity, float mass, Collider collider) {
		this.entity = entity;
		this.mass = mass;
		this.collider = collider;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public float getMass() {
		return mass;
	}
	
	public List<Force> getForces() {
		return forces;
	}
	
	public void addForce(Force f) {
		this.forces.add(f);
	}
	
	public Collider getCollider() {
		return collider;
	}

}

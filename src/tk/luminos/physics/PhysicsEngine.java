package tk.luminos.physics;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.EngineComponent;
import tk.luminos.Scene;

/**
 * Necessary functionality for physics engines 
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class PhysicsEngine extends EngineComponent {
	
	private List<Collider> colliders;	
	
	/**
	 * Creates physics engine
	 */
	public PhysicsEngine() {
		colliders = new ArrayList<Collider>();
	}
	
	/**
	 * Runs physics engine
	 */
	@Override
	public void run() {
		Thread.currentThread().setName("LUMINOS_ENGINE:_PHYSICS");
	}
	
	/**
	 * Updates engine
	 */
	@Override
	public void update(Scene scene) {
		for (Collider collider : colliders) {
			if (collider.isColliding())
				collider.response(1f / 30f);
		}
	}
	
	/**
	 * Attaches collider
	 * 
	 * @param collider		Collider to attach
	 */
	public void attachCollider(Collider collider) {
		this.colliders.add(collider);
	}

	/**
	 * Attaches colliders
	 * 
	 * @param colliders		Colliders to attach
	 */
	public void attachCollider(List<Collider> colliders) {
		this.colliders.addAll(colliders);
	}
	
	/**
	 * Attaches collider
	 * 
	 * @param collider		Collider to attach
	 * @param location		Attach location
	 */
	public void attachCollider(Collider collider, int location) {
		this.colliders.add(location, collider);
	}
	
	/**
	 * Attaches colliders
	 * 
	 * @param colliders		Colliders to attach
	 * @param location		Attach location
	 */
	public void attachCollider(List<Collider> colliders, int location) {
		this.colliders.addAll(location, colliders);
	}
	
	/**
	 * Removes collider from engine
	 * 
	 * @param collider		Collider to remove
	 * @return				Removed colllider
	 */
	public boolean removeCollider(Collider collider) {
		return this.colliders.remove(collider);
	}
	
	/**
	 * Removes colliders from engine
	 * 
	 * @param colliders		Colliders to remove
	 * @return				Removed collliders
	 */
	public boolean removeCollider(List<Collider> colliders) {
		return this.colliders.removeAll(colliders);
	}
	
	/**
	 * Removes collider from engine
	 * 
	 * @param location		Collider to remove
	 * @return				If collider was removed
	 */
	public boolean removeCollider(int location) {
		Collider collider = this.colliders.remove(location);
		return colliders.contains(collider);
	}

}

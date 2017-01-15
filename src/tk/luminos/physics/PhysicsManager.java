package tk.luminos.physics;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.Application;
import tk.luminos.ConfigData;
import tk.luminos.physics.colliders.Collider;

public class PhysicsManager extends Thread {
	
	private Application application;
	private List<Collider> colliders;
	
	public PhysicsManager(Application application) {
		this.application = application;
		colliders = new ArrayList<Collider>();
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("LUMINOS_ENGINE:_PHYSICS");
		while (!application.application_Close) {
			update();
		}
	}
	
	public void update() {
		float interval = 1 / ConfigData.UPS;
		for (Collider collider : colliders) {
			collider.checkIntersection();
			if (collider.isColliding())
				collider.response(interval);
		}
	}
	
	public void attachCollider(Collider collider) {
		this.colliders.add(collider);
	}
	
	public void attachCollider(List<Collider> colliders) {
		this.colliders.addAll(colliders);
	}
	
	public void attachCollider(Collider collider, int location) {
		this.colliders.add(location, collider);
	}
	
	public void attachCollider(List<Collider> colliders, int location) {
		this.colliders.addAll(location, colliders);
	}
	
	public boolean removeCollider(Collider collider) {
		return this.colliders.remove(collider);
	}
	
	public boolean removeCollider(List<Collider> colliders) {
		return this.colliders.removeAll(colliders);
	}
	
	public boolean removeCollider(int location) {
		Collider collider = this.colliders.remove(location);
		return colliders.contains(collider);
	}

}

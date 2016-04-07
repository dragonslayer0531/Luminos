package luminoscore.graphics.particles;

import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Entity;

public class Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	private float grav;
	private float life;
	private float rotation;
	private float scale;
	
	private float elapsedTime = 0;

	public Particle(Vector3f position, Vector3f velocity, float grav, float life, float rotation, float scale) {
		this.position = position;
		this.velocity = velocity;
		this.grav = grav;
		this.life = life;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public boolean update(GLFWWindow window) {
		velocity.y += Entity.GRAVITY * grav * window.getFrameTime();
		Vector3f change = new Vector3f(velocity);
		change.scale(window.getFrameTime());
		Vector3f.add(change, position, position);
		elapsedTime += window.getFrameTime();
		return elapsedTime < life;
	}

}

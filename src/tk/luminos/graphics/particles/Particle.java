package tk.luminos.graphics.particles;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.maths.Vector3;

public class Particle extends GameObject {
	
	private Vector3 velocity;
	private float life, elapsedTime;

	public Particle(TexturedModel model, Vector3 position, Vector3 rotation, Vector3 scale, Vector3 velocity, float life) {
		super(model, position, rotation, scale);
		
		this.velocity = velocity;
		this.life = life;
		this.elapsedTime = 0;
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public float getLife() {
		return life;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

}

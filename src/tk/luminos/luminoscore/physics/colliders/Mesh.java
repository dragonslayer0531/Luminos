package tk.luminos.luminoscore.physics.colliders;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.gameobjects.Entity;

/**
 * 
 * Creates mesh collider
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Mesh implements Collider {
	
	public List<Vector3f> vertices;
	public List<Vector3f> normals;
	
	/**
	 * Constructor
	 */
	public Mesh() {
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
	}

	/**
	 * Generates mesh collider for an {@link Entity}
	 * 
	 * @param entity		Entity to have collider formed for
	 */
	public void generate(Entity entity) {
		vertices = entity.getMesh().getVertices();
		normals = entity.getMesh().getNormals();
	}

}

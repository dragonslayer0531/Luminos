package tk.luminos.luminoscore.physics.colliders;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.gameobjects.Entity;
import tk.luminos.luminoscore.graphics.models.Mesh;
import tk.luminos.luminoscore.tools.Maths;

/**
 * 
 * Creates axis aligned bounding box collider.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class AABB implements Collider {
	
	private float xMin, xMax, yMin, yMax, zMin, zMax;
	
	/**
	 * Null constructor
	 */
	public AABB() {
		xMin = xMax = yMin = yMax = zMin = zMax = 0;
	}
	
	/**
	 * Creates an AABB of the entity
	 * 
	 * @param entity	Entity to create AABB of
	 * @return			AABB of mesh
	 */
	public void generate(Entity entity) {
		
		Mesh mesh = entity.getMesh();
		List<Vector3f> vertices = mesh.getVertices();
		int size = vertices.size();
		float[] x = new float[size];
		float[] y = new float[size];
		float[] z = new float[size];
		
		int i = 0;
		for(Vector3f vertex : vertices) {
			x[i] = vertex.x * entity.getScale();
			y[i] = vertex.y * entity.getScale();
			z[i] = vertex.z * entity.getScale();
			i++;
		}
		
		this.xMin = Maths.getMinimum(x);
		this.xMax = Maths.getMaximum(x);
		this.yMin = Maths.getMinimum(y);
		this.yMax = Maths.getMaximum(y);
		this.zMin = Maths.getMinimum(z);
		this.zMax = Maths.getMaximum(z);

	}

	/**
	 * Gets the X Minimum value
	 * 
	 * @return	X minimum value
	 */
	public float getXMinimum() {
		return xMin;
	}

	/**
	 * Gets the X Maximum value
	 * 
	 * @return	X maximum value
	 */
	public float getXMaximum() {
		return xMax;
	}

	/**
	 * Gets the Y Minimum value
	 * 
	 * @return	Y minimum value
	 */
	public float getYMinimum() {
		return yMin;
	}

	/**
	 * Gets the Y Maximum value
	 * 
	 * @return	Y maximum value
	 */
	public float getYMaximum() {
		return yMax;
	}

	/**
	 * Gets the Z Minimum value
	 * 
	 * @return	Z minimum value
	 */
	public float getZMinimum() {
		return zMin;
	}

	/**
	 * Gets the Z Maximum value
	 * 
	 * @return	Z maximum value
	 */
	public float getZMaximum() {
		return zMax;
	}

}

package luminoscore.graphics.models;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Mesh {
	
	private List<Vector3f> vertices;

	public Mesh(List<Vector3f> vertices) {
		this.vertices = vertices;
	}

	public List<Vector3f> getVertices() {
		return vertices;
	}

}

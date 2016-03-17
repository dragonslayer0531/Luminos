package luminoscore.graphics.models.basic;

import java.util.ArrayList;
import java.util.List;

import luminoscore.graphics.models.TexturelessModel;
import luminoscore.util.math.Conversions;
import luminoscore.util.math.Vector3f;

public class ThreeDimensional {
	
	public static TexturelessModel sphere(float radius) {
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		int pointer = 0;
		
		for(int polar = -90; polar < 90; polar++) for(int alpha = 0; alpha < 360; alpha++) {
			Vector3f vertex = Conversions.polarToCartesian(radius, polar, alpha);
			if(vertices.contains(vertex)) {
				indices.add(vertices.indexOf(vertex));
			} else {
				vertices.add(vertex);
				normals.add(vertex.normalise());
				indices.add(pointer);
				pointer++;
			}
		}
		
		return new TexturelessModel(indices, vertices, normals, 3);
		
	}
	
}

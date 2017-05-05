package tk.luminos.gameobjects;

import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

/**
 * Transformation matrix of entity
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Transformation extends Component<Matrix4> {
	
	Vector3 position;
	Vector3 rotation;
	Vector3 scale;
	
	/**
	 * Creates new transformation matrix
	 * 
	 * @param position		position of component entity
	 * @param rotation		rotation of component entity
	 * @param scale			scale of component entity
	 */
	public Transformation(Vector3 position, Vector3 rotation, Vector3 scale) {
		super(MathUtils.createTransformationMatrix(position, rotation, scale));
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	/**
	 * Reconstructs matrix
	 */
	public void constructModelMatrix() {
		obj = MathUtils.createTransformationMatrix(position, rotation, scale);
	}

}

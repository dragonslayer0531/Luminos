package tk.luminos.gameobjects;

import tk.luminos.maths.MathUtils;
import tk.luminos.maths.vector.Vector3f;

public class Transformation<Type> extends Component<Type> {
	
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	
	public Transformation(Vector3f position, Vector3f rotation, Vector3f scale) {
		super(MathUtils.createTransformationMatrix(position, rotation, scale));
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public void constructModelMatrix() {
		obj = MathUtils.createTransformationMatrix(position, rotation, scale);
	}

}

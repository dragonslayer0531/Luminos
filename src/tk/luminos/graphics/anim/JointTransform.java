package tk.luminos.graphics.anim;

import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Quaternion;
import tk.luminos.maths.Vector3;

public class JointTransform {
	
	private final Vector3 position;
	private final Quaternion rotation;
	
	public JointTransform(Vector3 position, Quaternion rotation) {
		this.rotation = rotation;
		this.position = position;
	}
	
	public Matrix4 getLocalTransform() {
		Matrix4 matrix = new Matrix4();
		Matrix4.translate(position, new Matrix4(), matrix);
		Matrix4.mul(matrix, rotation.toRotationMatrix(), matrix);
		return matrix;
	}
	
	public static JointTransform interpolate(JointTransform frameA, JointTransform frameB, float progression) {
		Vector3 pos = interpolate(frameA.position, frameB.position, progression);
		Quaternion rot = Quaternion.interpolate(frameA.rotation, frameB.rotation, progression);
		return new JointTransform(pos, rot);
	}
	
	private static Vector3 interpolate(Vector3 start, Vector3 end, float progression) {
		float x = start.x + (end.x - start.x) * progression;
		float y = start.y + (end.y - start.y) * progression;
		float z = start.z + (end.z - start.z) * progression;
		return new Vector3(x, y, z);
	}

}

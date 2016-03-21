package luminoscore.util.math.matrix;

import luminoscore.graphics.entities.components.Camera;
import luminoscore.util.math.vector.Vector2f;
import luminoscore.util.math.vector.Vector3f;

public class MatrixCreator {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	/*
	 * @param translation 2D Vector to Translate Matrix on XY plane
	 * @param scale 2D Vector to Scale Matrix on XY Plane
	 * @return Matrix4f
	 * 
	 * Creates a 2 Dimensional transformation matrix for the XY plane.  Best used for 2D rendering, such as text and GUIs
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1), matrix, matrix);
		return matrix;
	}
	
	/*
	 * @param translation 3D Vector to Translate Matrix
	 * @param rotation 3D Vector to Rotate Matrix
	 * @param scale float to scale matrix uniformly
	 * @return Matrix4f
	 * 
	 * Creates a 3 Dimensional transformation matrix.
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	/*
	 * @param translation 3D Vector to Translate Matrix
	 * @param rotation 3D Vector to Rotate Matrix
	 * @param scale 3D Vector to Scale Matrix
	 * @return Matrix4f
	 * 
	 * Creates a 3 Dimensional transformation matrix.
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	
	/*
	 * @param camera Camera to create a view matrix for
	 * @return Matrix4f
	 * 
	 * Creates view matrix for a camera's position and rotation
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPosition = camera.getPosition();
		Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
		Matrix4f.translate(negativeCameraPosition, viewMatrix, viewMatrix);
		return viewMatrix;
	}

}

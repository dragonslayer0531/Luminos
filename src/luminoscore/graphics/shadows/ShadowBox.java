package luminoscore.graphics.shadows;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import luminoscore.GlobalLock;
import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.render.MasterRenderer;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Box defining where shadows are rendered in the scene
 *
 */

public class ShadowBox {
	
	private static final float OFFSET = 10;
    private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
    private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);
    private static final float SHADOW_DISTANCE = 100;
 
    private float minX, maxX;
    private float minY, maxY;
    private float minZ, maxZ;
    private Matrix4f lightViewMatrix;
    private Camera cam;
 
    private float farHeight, farWidth, nearHeight, nearWidth;
 
    /**
     * @param lightViewMatrix	view matrix in the light position
     * @param camera			Camera to render with
     * 
     * Constructor		
     */
    public ShadowBox(Matrix4f lightViewMatrix, Camera camera) {
        this.lightViewMatrix = lightViewMatrix;
        this.cam = camera;
        calculateWidthsAndHeights();
    }
 
    /**
     * Update the ShadowBox's position and orientation
     */
    public void update() {
        Matrix4f rotation = calculateCameraRotationMatrix();
        Vector3f forwardVector = new Vector3f(Matrix4f.transform(rotation, FORWARD, null));
 
        Vector3f toFar = new Vector3f(forwardVector);
        toFar.scale(SHADOW_DISTANCE);
        Vector3f toNear = new Vector3f(forwardVector);
        toNear.scale(MasterRenderer.NEAR_PLANE);
        Vector3f centerNear = Vector3f.add(toNear, cam.getPosition(), null);
        Vector3f centerFar = Vector3f.add(toFar, cam.getPosition(), null);
        Vector4f[] points = calculateFrustumVertices(rotation, forwardVector, centerNear,
                centerFar);
 
        boolean first = true;
        for (Vector4f point : points) {
            if (first) {
                minX = point.x;
                maxX = point.x;
                minY = point.y;
                maxY = point.y;
                minZ = point.z;
                maxZ = point.z;
                first = false;
                continue;
            }
            if (point.x > maxX) {
                maxX = point.x;
            } else if (point.x < minX) {
                minX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            } else if (point.y < minY) {
                minY = point.y;
            }
            if (point.z > maxZ) {
                maxZ = point.z;
            } else if (point.z < minZ) {
                minZ = point.z;
            }
        }
        maxZ += OFFSET;
 
    }
 
    /**
     * @return Vector3f		Center of ShadowBox
     * 
     * Gets and calculates the center of the shadow box
     */
    public Vector3f getCenter() {
        float x = (minX + maxX) / 2f;
        float y = (minY + maxY) / 2f;
        float z = (minZ + maxZ) / 2f;
        Vector4f cen = new Vector4f(x, y, z, 1);
        Matrix4f invertedLight = new Matrix4f();
        Matrix4f.invert(lightViewMatrix, invertedLight);
        return new Vector3f(Matrix4f.transform(invertedLight, cen, null));
    }
 
    /**
     * @return float	width
     * 
     * Gets width of box
     */
    public float getWidth() {
        return maxX - minX;
    }
 
    /**
     * @return float	height
     * 
     * Gets height of box
     */
    public float getHeight() {
        return maxY - minY;
    }
 
    /**
     * @return float	length
     * 
     * Gets length of box
     */
    public float getLength() {
        return maxZ - minZ;
    }

//***************************************Private Methods**************************************//
    
    /**
     * @param rotation		Defines rotation of frustum
     * @param forwardVector	Defines forward vector of box
     * @param centerNear	Defines the center near position of box
     * @param centerFar		Defines the center far position of box
     * @return Vector4f[] 	Array of Vector4f defining frustum vertices
     * 
     * Gets the frustum vertices
     */
    private Vector4f[] calculateFrustumVertices(Matrix4f rotation, Vector3f forwardVector,
            Vector3f centerNear, Vector3f centerFar) {
        Vector3f upVector = new Vector3f(Matrix4f.transform(rotation, UP, null));
        Vector3f rightVector = Vector3f.cross(forwardVector, upVector, null);
        Vector3f downVector = new Vector3f(-upVector.x, -upVector.y, -upVector.z);
        Vector3f leftVector = new Vector3f(-rightVector.x, -rightVector.y, -rightVector.z);
        Vector3f farTop = Vector3f.add(centerFar, new Vector3f(upVector.x * farHeight,
                upVector.y * farHeight, upVector.z * farHeight), null);
        Vector3f farBottom = Vector3f.add(centerFar, new Vector3f(downVector.x * farHeight,
                downVector.y * farHeight, downVector.z * farHeight), null);
        Vector3f nearTop = Vector3f.add(centerNear, new Vector3f(upVector.x * nearHeight,
                upVector.y * nearHeight, upVector.z * nearHeight), null);
        Vector3f nearBottom = Vector3f.add(centerNear, new Vector3f(downVector.x * nearHeight,
                downVector.y * nearHeight, downVector.z * nearHeight), null);
        Vector4f[] points = new Vector4f[8];
        points[0] = calculateLightSpaceFrustumCorner(farTop, rightVector, farWidth);
        points[1] = calculateLightSpaceFrustumCorner(farTop, leftVector, farWidth);
        points[2] = calculateLightSpaceFrustumCorner(farBottom, rightVector, farWidth);
        points[3] = calculateLightSpaceFrustumCorner(farBottom, leftVector, farWidth);
        points[4] = calculateLightSpaceFrustumCorner(nearTop, rightVector, nearWidth);
        points[5] = calculateLightSpaceFrustumCorner(nearTop, leftVector, nearWidth);
        points[6] = calculateLightSpaceFrustumCorner(nearBottom, rightVector, nearWidth);
        points[7] = calculateLightSpaceFrustumCorner(nearBottom, leftVector, nearWidth);
        return points;
    }
 
    /**
     * @param startPoint	Start point of frustum
     * @param direction		Direction of frustum
     * @param width			Width of frustum
     * @return	Vector4f 	Light Space Frustum Corner
     * 
     * Calculates Ligh Space Frustum Corners
     */
    private Vector4f calculateLightSpaceFrustumCorner(Vector3f startPoint, Vector3f direction, float width) {
        Vector3f point = Vector3f.add(startPoint,
                new Vector3f(direction.x * width, direction.y * width, direction.z * width), null);
        Vector4f point4f = new Vector4f(point.x, point.y, point.z, 1f);
        Matrix4f.transform(lightViewMatrix, point4f, point4f);
        return point4f;
    }
 
    /**
     * @return Matrix4f	Camera Rotation Matrix
     * 
     * Gets and calculates camera rotation matrix
     */
    private Matrix4f calculateCameraRotationMatrix() {
        Matrix4f rotation = new Matrix4f();
        rotation.rotate((float) Math.toRadians(-cam.getYaw()), new Vector3f(0, 1, 0));
        rotation.rotate((float) Math.toRadians(-cam.getPitch()), new Vector3f(1, 0, 0));
        return rotation;
    }
 
    /**
     * Calculate width and height of shadow box
     */
    private void calculateWidthsAndHeights() {
        farWidth = (float) (SHADOW_DISTANCE * Math.tan(Math.toRadians(MasterRenderer.FOV)));
        nearWidth = (float) (MasterRenderer.NEAR_PLANE
                * Math.tan(Math.toRadians(MasterRenderer.FOV)));
        farHeight = farWidth / getAspectRatio();
        nearHeight = nearWidth / getAspectRatio();
    }
 
    /**
     * @return float	Aspect Ratio
     * 
     * Gets and calculates aspect ratio
     */
    private float getAspectRatio() {
        return (float) GlobalLock.WIDTH / GlobalLock.HEIGHT;
    }

}

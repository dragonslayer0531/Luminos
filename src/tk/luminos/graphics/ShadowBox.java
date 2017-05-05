package tk.luminos.graphics;

import tk.luminos.Application;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

/**
 * 
 * Box defining where shadows are rendered in the scene
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowBox {
	
	private static int WIDTH = Application.getValue("HEIGHT");
	private static int HEIGHT = Application.getValue("HEIGHT");
	
	private static final float OFFSET = 20;
    private static final Vector4 UP = new Vector4(0, 1, 0, 0);
    private static final Vector4 FORWARD = new Vector4(0, 0, -1, 0);
    public static float SHADOW_DISTANCE = 125;
 
    private float minX, maxX;
    private float minY, maxY;
    private float minZ, maxZ;
    private Matrix4 lightViewMatrix;
    private Camera cam;
 
    private float farHeight, farWidth, nearHeight, nearWidth;
 
    /**
     * Constructor		
     * 
     * @param lightViewMatrix	view matrix in the light position
     * @param camera			{@link Camera} to render with
     */
    public ShadowBox(Matrix4 lightViewMatrix, Camera camera) {
        this.lightViewMatrix = lightViewMatrix;
        this.cam = camera;
        calculateWidthsAndHeights();
    }
 
    /**
     * Update the ShadowBox's position and orientation
     */
    public void update() {
        Matrix4 rotation = calculateCameraRotationMatrix();
        Vector4 temp = Matrix4.transform(rotation, FORWARD, null);
        Vector3 forwardVector = new Vector3(temp.x, temp.y, temp.z);
 
        Vector3 toFar = new Vector3(forwardVector);
        toFar.scale(SHADOW_DISTANCE);
        Vector3 toNear = new Vector3(forwardVector);
        toNear.scale(MasterRenderer.NEAR_PLANE);
        Vector3 centerNear = Vector3.add(toNear, cam.getPosition(), null);
        Vector3 centerFar = Vector3.add(toFar, cam.getPosition(), null);
        Vector4[] points = calculateFrustumVertices(rotation, forwardVector, centerNear,
                centerFar);
 
        boolean first = true;
        for (Vector4 point : points) {
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
     * Gets and calculates the center of the shadow box
     * 
     * @return Vector3f		Center of ShadowBox
     */
    public Vector3 getCenter() {
        float x = (minX + maxX) / 2f;
        float y = (minY + maxY) / 2f;
        float z = (minZ + maxZ) / 2f;
        Vector4 cen = new Vector4(x, y, z, 1);
        Matrix4 invertedLight = new Matrix4();
        Matrix4.invert(lightViewMatrix, invertedLight);
        Vector4 temp = Matrix4.transform(invertedLight, cen, null);
        return new Vector3(temp.x, temp.y, temp.z);
    }
 
    /**
     * Gets width of box
     * 
     * @return width
     */
    public float getWidth() {
        return maxX - minX;
    }
 
    /**
     * Gets height of box
     * 
     * @return height
     */
    public float getHeight() {
        return maxY - minY;
    }
 
    /**
     * Gets length of box
     * 
     * @return float	length
     */
    public float getLength() {
        return maxZ - minZ;
    }

//***************************************Private Methods**************************************//
    
    /**
     * Gets the frustum vertices
     * 
     * @param rotation		Defines rotation of frustum
     * @param forwardVector	Defines forward vector of box
     * @param centerNear	Defines the center near position of box
     * @param centerFar		Defines the center far position of box
     * @return 	Array of Vector4f defining frustum vertices
     */
    private Vector4[] calculateFrustumVertices(Matrix4 rotation, Vector3 forwardVector,
            Vector3 centerNear, Vector3 centerFar) {
    	Vector4 temp = Matrix4.transform(rotation, UP, null);
        Vector3 upVector = new Vector3(temp.x, temp.y, temp.z);
        Vector3 rightVector = Vector3.cross(forwardVector, upVector, null);
        Vector3 downVector = new Vector3(-upVector.x, -upVector.y, -upVector.z);
        Vector3 leftVector = new Vector3(-rightVector.x, -rightVector.y, -rightVector.z);
        Vector3 farTop = Vector3.add(centerFar, new Vector3(upVector.x * farHeight,
                upVector.y * farHeight, upVector.z * farHeight), null);
        Vector3 farBottom = Vector3.add(centerFar, new Vector3(downVector.x * farHeight,
                downVector.y * farHeight, downVector.z * farHeight), null);
        Vector3 nearTop = Vector3.add(centerNear, new Vector3(upVector.x * nearHeight,
                upVector.y * nearHeight, upVector.z * nearHeight), null);
        Vector3 nearBottom = Vector3.add(centerNear, new Vector3(downVector.x * nearHeight,
                downVector.y * nearHeight, downVector.z * nearHeight), null);
        Vector4[] points = new Vector4[8];
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
     * Calculates Light Space Frustum Corners
     * 
     * @param startPoint	Start point of frustum
     * @param direction		Direction of frustum
     * @param width			Width of frustum
     * @return			 	Light Space Frustum Corner
     */
    private Vector4 calculateLightSpaceFrustumCorner(Vector3 startPoint, Vector3 direction, float width) {
        Vector3 point = Vector3.add(startPoint,
                new Vector3(direction.x * width, direction.y * width, direction.z * width), null);
        Vector4 point4f = new Vector4(point.x, point.y, point.z, 1f);
        Matrix4.transform(lightViewMatrix, point4f, point4f);
        return point4f;
    }
 
    /**
     * Gets and calculates camera rotation matrix
     * 
     * @return Matrix4f	Camera Rotation Matrix
     */
    private Matrix4 calculateCameraRotationMatrix() {
        Matrix4 rotation = new Matrix4();
        Matrix4.rotate((float) Math.toRadians(-cam.getYaw()), new Vector3(0, 1, 0), rotation, rotation);
        Matrix4.rotate((float) Math.toRadians(-cam.getPitch()), new Vector3(1, 0, 0), rotation, rotation);
        return rotation;
    }
 
    /**
     * Calculate width and height of shadow box
     */
    private void calculateWidthsAndHeights() {
        farWidth = (float) (OFFSET + SHADOW_DISTANCE * Math.tan(Math.toRadians(MasterRenderer.FOV)));
        nearWidth = (float) (OFFSET + MasterRenderer.NEAR_PLANE
                * Math.tan(Math.toRadians(MasterRenderer.FOV)));
        farHeight = farWidth / getAspectRatio();
        nearHeight = nearWidth / getAspectRatio();
    }
 
    /**
     * Gets and calculates aspect ratio
     * 
     * @return	Aspect Ratio
     */
    private float getAspectRatio() {
        return (float) WIDTH / HEIGHT;
    }

}

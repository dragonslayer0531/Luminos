package tk.luminos.maths;

import tk.luminos.physics.AABB;
import tk.luminos.physics.BoundingSphere;

/**
 * Creates frustum intersection object
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Frustum {
	
	public final static int SIZE = 24;
	
	static final int PLANE_NX = 0x0;
	static final int PLANE_PX = 0x1;
	static final int PLANE_NY = 0x2;
	static final int PLANE_PY = 0x3;
	static final int PLANE_NZ = 0x4;
	static final int PLANE_PZ = 0x5;
	
	static final int INTERSECT = -0x1;
	static final int INSIDE    = -0x2;
	static final int OUTSIDE   = -0x3;
	
	static final int PLANE_MASK_NX = 1 << PLANE_NX;
	static final int PLANE_MASK_PX = 1 << PLANE_PX;
	static final int PLANE_MASK_NY = 1 << PLANE_NY;
	static final int PLANE_MASK_PY = 1 << PLANE_PY;
	static final int PLANE_MASK_NZ = 1 << PLANE_NZ;
	static final int PLANE_MASK_PZ = 1 << PLANE_PZ;
	
	private float 	nxX, nxY, nxZ, nxW,
					pxX, pxY, pxZ, pxW,
					nyX, nyY, nyZ, nyW,
					pyX, pyY, pyZ, pyW,
					nzX, nzY, nzZ, nzW,
					pzX, pzY, pzZ, pzW;
	
	private final Vector4[] planes = new Vector4[6];
	{
		for (int i = 0; i < 6; i++) {
			planes[i] = new Vector4();
		}
	}
	
	/**
	 * Creates default frustum intersection object
	 */
	public Frustum() {
		
	}
	
	/**
	 * Creates frustum intersection object
	 * 
	 * @param m		matrix to set as data
	 */
	public Frustum(Matrix4 m) {
		set(m);
	}
	
	/**
	 * Sets frustum intersection data
	 * 
	 * @param m		matrix to set data of 
	 * @return		updated frustum intersection
	 */
	public Frustum set(Matrix4 m) {
		float invl;
		nxX = m.m03 + m.m00;
		nxY = m.m13 + m.m10;
		nxZ = m.m23 + m.m20;
		nxW = m.m33 + m.m30;
		invl = (float) (1.0 / Math.sqrt(nxX * nxX + nxY * nxY + nxZ * nxZ));
		nxX *= invl;
		nxY *= invl;
		nxZ *= invl;
		nxW *= invl;
		planes[0] = new Vector4(nxX, nxY, nxZ, nxW);
		
		pxX = m.m03 - m.m00;
		pxY = m.m13 - m.m10;
		pxZ = m.m23 - m.m20;
		pxW = m.m33 - m.m30;
		invl = (float) (1.0 / Math.sqrt(pxX * pxX + pxY * pxY + pxZ * pxZ));
		pxX *= invl;
		pxY *= invl;
		pxZ *= invl;
		pxW *= invl;
		planes[1] = new Vector4(pxX, pxY, pxZ, pxW);
		
		nyX = m.m03 + m.m00;
		nyY = m.m13 + m.m10;
		nyZ = m.m23 + m.m20;
		nyW = m.m33 + m.m30;
		invl = (float) (1.0 / Math.sqrt(nyX * nyX + nyY * nyY + nyZ * nyZ));
		nyX *= invl;
		nyY *= invl;
		nyZ *= invl;
		nyW *= invl;
		planes[2] = new Vector4(nyX, nyY, nyZ, nyW);
		
		pyX = m.m03 - m.m00;
		pyY = m.m13 - m.m10;
		pyZ = m.m23 - m.m20;
		pyW = m.m33 - m.m30;
		invl = (float) (1.0 / Math.sqrt(pyX * pyX + pyY * pyY + pyZ * pyZ));
		pyX *= invl;
		pyY *= invl;
		pyZ *= invl;
		pyW *= invl;
		planes[3] = new Vector4(pyX, pyY, pyZ, pyW);
		
		nzX = m.m03 + m.m00;
		nzY = m.m13 + m.m10;
		nzZ = m.m23 + m.m20;
		nzW = m.m33 + m.m30;
		invl = (float) (1.0 / Math.sqrt(nzX * nzX + nzY * nzY + nzZ * nzZ));
		nzX *= invl;
		nzY *= invl;
		nzZ *= invl;
		nzW *= invl;
		planes[4] = new Vector4(nzX, nzY, nzZ, nzW);
		
		pzX = m.m03 - m.m00;
		pzY = m.m13 - m.m10;
		pzZ = m.m23 - m.m20;
		pzW = m.m33 - m.m30;
		invl = (float) (1.0 / Math.sqrt(pzX * pzX + pzY * pzY + pzZ * pzZ));
		pzX *= invl;
		pzY *= invl;
		pzZ *= invl;
		pzW *= invl;
		planes[5] = new Vector4(pzX, pzY, pzZ, pzW);
		
		return this;
	}
	
	/**
	 * Checks if point is inside frustum
	 * 
	 * @param vec	vector representing point
	 * @return		is point inside
	 */
	public boolean point(Vector3 vec) {
		return  nxX * vec.x + nxY * vec.y + nxZ * vec.z + nxW >= 0 &&
				pxX * vec.x + pxY * vec.y + pxZ * vec.z + pxW >= 0 &&
				nyX * vec.x + nyY * vec.y + nyZ * vec.z + nyW >= 0 &&
				pyX * vec.x + pyY * vec.y + pyZ * vec.z + pyW >= 0 &&
				nzX * vec.x + nzY * vec.y + nzZ * vec.z + nzW >= 0 &&
				pzX * vec.x + pzY * vec.y + pzZ * vec.z + pzW >= 0;
	}
	
	/**
	 * Checks if bounding sphere is inside frustum
	 * 
	 * @param bs	bounding sphere
	 * @return		is inside
	 */
	public boolean boundingSphere(BoundingSphere bs) {
		Vector3 vec = bs.getLocation();
		return  nxX * vec.x + nxY * vec.y + nxZ * vec.z + nxW >= -bs.getRadius() &&
				pxX * vec.x + pxY * vec.y + pxZ * vec.z + pxW >= -bs.getRadius() &&
				nyX * vec.x + nyY * vec.y + nyZ * vec.z + nyW >= -bs.getRadius() &&
				pyX * vec.x + pyY * vec.y + pyZ * vec.z + pyW >= -bs.getRadius() &&
				nzX * vec.x + nzY * vec.y + nzZ * vec.z + nzW >= -bs.getRadius() &&
				pzX * vec.x + pzY * vec.y + pzZ * vec.z + pzW >= -bs.getRadius();
	}
	
	/**
	 * Checks if AABB is inside frustum
	 * 
	 * @param aabb		axis aligned bounding box
	 * @return			inside frustum
	 */
	public boolean aabb(AABB aabb) {
		float minX = aabb.getMinExtents().x;
		float minY = aabb.getMinExtents().y;
		float minZ = aabb.getMinExtents().z;
		float maxX = aabb.getMaxExtents().x;
		float maxY = aabb.getMaxExtents().y;
		float maxZ = aabb.getMaxExtents().z;
		
		return	nxX *(nxX < 0 ? minX : maxX) + nxY * (nxY < 0 ? minY : maxY) + (nxZ < 0 ? minZ : maxZ) >= -nxW &&
				pxX *(pxX < 0 ? minX : maxX) + pxY * (pxY < 0 ? minY : maxY) + (pxZ < 0 ? minZ : maxZ) >= -nxW &&
				nxX *(nyX < 0 ? minX : maxX) + nxY * (nyY < 0 ? minY : maxY) + (nyZ < 0 ? minZ : maxZ) >= -nxW &&
				pxX *(pyX < 0 ? minX : maxX) + pxY * (pyY < 0 ? minY : maxY) + (pyZ < 0 ? minZ : maxZ) >= -nxW &&
				nxX *(nzX < 0 ? minX : maxX) + nxY * (nzY < 0 ? minY : maxY) + (nzZ < 0 ? minZ : maxZ) >= -nxW &&
				pxX *(pzX < 0 ? minX : maxX) + pxY * (pzY < 0 ? minY : maxY) + (pzZ < 0 ? minZ : maxZ) >= -nxW;
	}
	
}

package com.luminos.graphics.render;

import static com.luminos.graphics.render.MasterRenderer.FAR_PLANE;
import static com.luminos.graphics.render.MasterRenderer.FOV;
import static com.luminos.graphics.render.MasterRenderer.NEAR_PLANE;
import static com.luminos.graphics.render.MasterRenderer.createProjectionMatrix;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.Entity;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.tools.Maths;

public class Frustum {

	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int TOP = 3;
	public static final int BACK = 4;
	public static final int FRONT = 5;


	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;


	private float[][] frustum = new float[6][4];


	private static FloatBuffer modlBuffer = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer projBuffer = BufferUtils.createFloatBuffer(16);

	private Matrix4f projectionMatrix = createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
	private boolean frustumCalculated = false;

	public void calculateFrustum(Entity entity, Camera camera) {	
		float[] modl = new float[16];
		float[] proj = new float[16];
		float[] clip = new float[16];



		modlBuffer.rewind();
		Matrix4f.mul(Maths.createViewMatrix(camera), entity.getModelMatrix(), null).store(modlBuffer);
		projBuffer.rewind();
		projectionMatrix.store(projBuffer);



		for(int i = 0; i < 16; i ++) {
			modl[i] = modlBuffer.get(i);
			proj[i] = projBuffer.get(i);
		}



		clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8] + modl[3] * proj[12];
		clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9] + modl[3] * proj[13];
		clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10] + modl[3] * proj[14];
		clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11] + modl[3] * proj[15];

		clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8] + modl[7] * proj[12];
		clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9] + modl[7] * proj[13];
		clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10] + modl[7] * proj[14];
		clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11] + modl[7] * proj[15];

		clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8] + modl[11] * proj[12];
		clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9] + modl[11] * proj[13];
		clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10] + modl[11] * proj[14];
		clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11] + modl[11] * proj[15];

		clip[12] = modl[12] * proj[0] + modl[13] * proj[4] + modl[14] * proj[8] + modl[15] * proj[12];
		clip[13] = modl[12] * proj[1] + modl[13] * proj[5] + modl[14] * proj[9] + modl[15] * proj[13];
		clip[14] = modl[12] * proj[2] + modl[13] * proj[6] + modl[14] * proj[10] + modl[15] * proj[14];
		clip[15] = modl[12] * proj[3] + modl[13] * proj[7] + modl[14] * proj[11] + modl[15] * proj[15];


		//Right
		frustum[RIGHT][A] = clip[ 3] - clip[ 0];
		frustum[RIGHT][B] = clip[ 7] - clip[ 4];
		frustum[RIGHT][C] = clip[11] - clip[ 8];
		frustum[RIGHT][D] = clip[15] - clip[12];
		normalizePlane(frustum, RIGHT);


		//Left
		frustum[LEFT][A] = clip[ 3] + clip[ 0];
		frustum[LEFT][B] = clip[ 7] + clip[ 4];
		frustum[LEFT][C] = clip[11] + clip[ 8];
		frustum[LEFT][D] = clip[15] + clip[12];
		normalizePlane(frustum, LEFT);


		//Bottom
		frustum[BOTTOM][A] = clip[ 3] + clip[ 1];
		frustum[BOTTOM][B] = clip[ 7] + clip[ 5];
		frustum[BOTTOM][C] = clip[11] + clip[ 9];
		frustum[BOTTOM][D] = clip[15] + clip[13];
		normalizePlane(frustum, BOTTOM);


		//Top
		frustum[TOP][A] = clip[ 3] - clip[ 1];
		frustum[TOP][B] = clip[ 7] - clip[ 5];
		frustum[TOP][C] = clip[11] - clip[ 9];
		frustum[TOP][D] = clip[15] - clip[13];
		normalizePlane(frustum, TOP);


		//Back
		frustum[BACK][A] = clip[ 3] - clip[ 2];
		frustum[BACK][B] = clip[ 7] - clip[ 6];
		frustum[BACK][C] = clip[11] - clip[10];
		frustum[BACK][D] = clip[15] - clip[14];
		normalizePlane(frustum, BACK);


		//Front
		frustum[FRONT][A] = clip[ 3] + clip[ 2];
		frustum[FRONT][B] = clip[ 7] + clip[ 6];
		frustum[FRONT][C] = clip[11] + clip[10];
		frustum[FRONT][D] = clip[15] + clip[14];
		normalizePlane(frustum, FRONT);


		frustumCalculated = true;
	}






	private void normalizePlane(float[][] frustum, int side) {
		float magnitude = (float) Math.sqrt(frustum[side][A] * frustum[side][A] + frustum[side][B] * frustum[side][B] + frustum[side][C] * frustum[side][D]);

		frustum[side][A] /= magnitude;
		frustum[side][B] /= magnitude;
		frustum[side][C] /= magnitude;
		frustum[side][D] /= magnitude;
	}






	public boolean pointInFrustum(Vector3f position) {
		if(!frustumCalculated) {
			return false;
		}
		
		int p;

		for( p = 0; p < 6; p++ )
			if( frustum[p][A] * position.x + frustum[p][B] * position.y + frustum[p][C] * position.z + frustum[p][D] <= -10 )
				return false;
		return true;
	}





}